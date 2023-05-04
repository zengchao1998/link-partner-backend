package com.wut.self.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.common.BaseResponse;
import com.wut.self.common.DeleteRequest;
import com.wut.self.common.ErrorCode;
import com.wut.self.exception.BusinessException;
import com.wut.self.model.domain.Team;
import com.wut.self.model.domain.User;
import com.wut.self.model.dto.TeamQuery;
import com.wut.self.model.request.*;
import com.wut.self.model.vo.TeamUserVo;
import com.wut.self.service.TeamService;
import com.wut.self.service.UserService;
import com.wut.self.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zeng
 */
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequestParams, HttpServletRequest req) {
        if(teamAddRequestParams == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User loginUser = userService.getLoginUser(req);
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamAddRequestParams);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest req) {
        if(deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User loginUser = userService.getLoginUser(req);
        boolean res = teamService.deleteTeam(deleteRequest, loginUser);
        if(!res) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest req) {
        if(teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User loginUser = userService.getLoginUser(req);
        if(!teamService.updateTeam(teamUpdateRequest, loginUser)) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(Long teamId) {
        if(teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team res = teamService.getById(teamId);
        if(res == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(res);
    }

    // todo 展示队伍已加入人数
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVo>> listTeams(TeamQuery teamQuery, HttpServletRequest req) {
        boolean isAdmin = userService.isAdmin(req);
        // 根据条件获取队伍信息
        List<TeamUserVo> list = teamService.getTeams(teamQuery, isAdmin);
        // 从队伍id集合中判断当前用户是否加入
        try {
            // 如果登录，需要判断当前登录用户是否加入队伍
            User loginUser = userService.getLoginUser(req);
            list = teamService.aboutTeams(loginUser, list);
        } catch (Exception e) {
            // 如果没有登录直接显示未加入队伍
            list.forEach(teamUserVo -> {
                teamUserVo.setHasJoin(false);
                teamUserVo.setTeamNum(0L);
            });
        }
        return ResultUtils.success(list);
    }

    // todo 分页查询队伍
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery, HttpServletRequest req) {
        if(teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(req);
        Page<Team> page = teamService.getTeamsForPage(teamQuery, isAdmin);
        return ResultUtils.success(page);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest req) {
        if(teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(req);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        if(!result) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "加入失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest req) {
        if(teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User loginUser = userService.getLoginUser(req);
        boolean res = teamService.quitTeam(teamQuitRequest, loginUser);
        if(!res) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL);
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/joined")
    public BaseResponse<List<TeamUserVo>> getJoinedTeams(HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        List<TeamUserVo> joinedTeams = teamService.getJoinedTeams(loginUser);
        return ResultUtils.success(joinedTeams);
    }

    @GetMapping("/owner")
    public BaseResponse<List<Team>> getOwnerTeams(HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        List<Team> ownerTeams = teamService.getOwnerTeams(loginUser);
        return ResultUtils.success(ownerTeams);
    }
}
