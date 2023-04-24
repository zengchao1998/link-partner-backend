package com.wut.self.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.common.BaseResponse;
import com.wut.self.common.ErrorCode;
import com.wut.self.exception.BusinessException;
import com.wut.self.model.domain.Team;
import com.wut.self.model.domain.User;
import com.wut.self.model.dto.TeamQuery;
import com.wut.self.model.request.TeamAddRequest;
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
@CrossOrigin(origins = {"http://127.0.0.1:5173"}, allowCredentials = "true")
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
    public BaseResponse<Boolean> deleteTeam(Long teamId) {
        if(teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        if(!teamService.removeById(teamId)) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team) {
        if(team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if(!teamService.updateTeam(team)) {
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

    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {
        if(teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Team> list = teamService.getTeams(teamQuery);
        return ResultUtils.success(list);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if(teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Team> page = teamService.getTeamsForPage(teamQuery);
        return ResultUtils.success(page);
    }
}
