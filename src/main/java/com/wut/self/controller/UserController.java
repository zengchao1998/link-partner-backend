package com.wut.self.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.common.BaseResponse;
import com.wut.self.common.ErrorCode;
import com.wut.self.exception.BusinessException;
import com.wut.self.model.domain.User;
import com.wut.self.model.request.UserLoginRequest;
import com.wut.self.model.request.UserRegisterRequest;
import com.wut.self.model.vo.UserVo;
import com.wut.self.service.UserService;
import com.wut.self.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.wut.self.constant.UserConstant.*;

/**
 * Author: zeng
 * Description: 用户接口(用户控制器)
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest requestParams) {
        if(requestParams == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = requestParams.getUserAccount();
        String userPassword = requestParams.getUserPassword();
        String checkPassword = requestParams.getCheckPassword();
        String validateCode = requestParams.getValidateCode();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, validateCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不完整");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, validateCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest requestParams, HttpServletRequest req) {
        if(requestParams == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = requestParams.getUserAccount();
        String userPassword = requestParams.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不完整");
        }
        User user = userService.userLogin(userAccount, userPassword, req);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销接口
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> logoutUser(HttpServletRequest req) {
        if(req == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Integer result = userService.userLogout(req);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户登录态接口
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest req) {
        Object userObj = req.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "当前用户未登录");
        }
        // 对于频繁变换的信息，可以考虑不直接从缓存中去，而是去查询数据库
        Long userId = currentUser.getId();
        User newCurrentUser = userService.getById(userId);
        // todo：如果用户状态异常，需要进行逻辑判断
        User safetyUser = userService.getSafetyUser(newCurrentUser);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 查询用户接口
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest req) {
        // 1. 鉴权，仅管理员可调用
        if(!userService.isAdmin(req)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "当前用户权限不足");
        }
        // 2. 执行查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        List<User> userList = userService.list(userQueryWrapper);
        // 3. 用户信息过滤
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 删除用户接口
     */
    @PostMapping ("/delete")
    public BaseResponse<Boolean> deleteUser(Long id, HttpServletRequest req) {
        // 鉴权，仅管理员可调用
        if(!userService.isAdmin(req)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "当前用户权限不足");
        }
        if(id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id输入错误");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagsNameList) {
        if(tagsNameList == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        List<User> userList = userService.searchUsersByTags(tagsNameList);
        return ResultUtils.success(userList);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest req) {
        // 校验参数是否为空
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User loginUser = userService.getLoginUser(req);
        Integer res = userService.updateUser(user, loginUser);
        return ResultUtils.success(res);
    }

    // todo 推荐多个用户
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageNum, long pageSize, HttpServletRequest req) {
        // 1. 获取登录用户
        User loginUser = userService.getLoginUser(req);
        // 2. 根据登录用户推荐
        Page<User> recommendUsers = userService.getRecommendUsers(pageNum, pageSize, loginUser);
        return ResultUtils.success(recommendUsers);
    }

    /**
     * 获取最推荐用户(top num)
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest req) {
        if(num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(req);
        List<User> users = userService.matchUsers(num, loginUser);
        return ResultUtils.success(users);
    }

}
