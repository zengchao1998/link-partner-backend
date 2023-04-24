package com.wut.self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wut.self.common.ErrorCode;
import com.wut.self.exception.BusinessException;
import com.wut.self.mapper.UserTeamMapper;
import com.wut.self.model.domain.Team;
import com.wut.self.model.domain.User;
import com.wut.self.model.domain.UserTeam;
import com.wut.self.model.dto.TeamQuery;
import com.wut.self.model.enums.TeamStatusEnum;
import com.wut.self.service.TeamService;
import com.wut.self.mapper.TeamMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author Administrator
* @description 针对表【team】的数据库操作Service实现
* @createDate 2023-04-24 12:38:09
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserTeamMapper userTeamMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public long addTeam(Team team, User loginUser) {
        final Long userId = loginUser.getId();
        // 1. 人数限制 1-8
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if(maxNum < 1 || maxNum > 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        // 2. 标题长度限制 20
        String teamName = team.getTeamName();
        if(StringUtils.isBlank(teamName) || teamName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        // 3. 描述长度限制 512（可以不设置）
        String description = team.getDescription();
        if(StringUtils.isBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        // 4. 队伍状态参数 0,1,2（默认为 0）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        if(status < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态码错误");
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByCode(status);
        if(statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态码错误");
        }
        String teamPassword = team.getTeamPassword();
        if (statusEnum.equals(TeamStatusEnum.SECRET_STATUS)
                && (StringUtils.isBlank(teamPassword) || teamPassword.length() < 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
        }
        // 5. 超时时间检验（大于当前时间、可以不设置）
        Date expireTime = team.getExpireTime();
        if(expireTime != null && new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间设置有误");
        }
        // 6. 用户创建的队伍数目限制 5
        // todo 并发问题
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Long hasTeamNums = teamMapper.selectCount(queryWrapper);
        if(hasTeamNums >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍达到上线");
        }
        // 7. 创建队伍和队伍用户关系
        return createTeamTransaction(team, userId);
    }

    @Override
    public boolean updateTeam(Team team) {
        Long id = team.getId();
        if(id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = teamMapper.selectById(id);
        if(oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int res = teamMapper.updateById(team);
        return res == 1;
    }

    @Override
    public Page<Team> getTeamsForPage(TeamQuery teamQuery) {
        Team team = new Team();
        try {
            // 对象关系映射
            BeanUtils.copyProperties(team, teamQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 自动根据team中的内容进行查询
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);

        long pageSize = teamQuery.getPageSize();
        long pageNum = teamQuery.getPageNum();
        Page<Team> teamPage = new Page<>(pageSize, pageNum);
        return teamMapper.selectPage(teamPage, queryWrapper);
    }

    @Override
    public List<Team> getTeams(TeamQuery teamQuery) {
        Team team = new Team();
        try {
            // 对象关系映射
            BeanUtils.copyProperties(team, teamQuery);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 自动根据team中的内容进行查询
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        return teamMapper.selectList(queryWrapper);
    }

    /**
     * 同时创建队伍和构建队伍、用户关系(使用事务保证原子性)
     * @param team 队伍
     * @param userId 用户id
     * @return 创建成功的队伍id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Long createTeamTransaction(Team team, Long userId) {
        // 1. 创建队伍
        // todo 事务代码优化
        team.setId(null);
        team.setUserId(userId);
        int result = teamMapper.insert(team);
        Long teamId = team.getId();
        if(result == 0 || teamId == null) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "创建队伍失败");
        }
        // 2. 创建队伍、用户关系(保证事务完整性)
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(team.getUserId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        int insertRelation = userTeamMapper.insert(userTeam);
        if(insertRelation == 0) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "创建队伍、用户关系失败");
        }
        return teamId;
    }
}




