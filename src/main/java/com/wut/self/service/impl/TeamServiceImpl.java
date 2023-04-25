package com.wut.self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wut.self.common.ErrorCode;
import com.wut.self.exception.BusinessException;
import com.wut.self.mapper.UserMapper;
import com.wut.self.mapper.UserTeamMapper;
import com.wut.self.model.domain.Team;
import com.wut.self.model.domain.User;
import com.wut.self.model.domain.UserTeam;
import com.wut.self.model.dto.TeamQuery;
import com.wut.self.model.enums.TeamStatusEnum;
import com.wut.self.model.request.TeamJoinRequest;
import com.wut.self.model.request.TeamUpdateRequest;
import com.wut.self.model.vo.TeamUserVo;
import com.wut.self.model.vo.UserVo;
import com.wut.self.service.TeamService;
import com.wut.self.mapper.TeamMapper;
import com.wut.self.service.UserTeamService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.wut.self.constant.UserConstant.ADMIN_ROLE;

/**
 * @author Administrator
 * description 针对表【team】的数据库操作Service实现
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserTeamMapper userTeamMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserTeamService userTeamService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public long addTeam(Team team, User loginUser) {
        final Long userId = loginUser.getId();
        // 1. 人数限制 1-8
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        // 2. 标题长度限制 20
        String teamName = team.getTeamName();
        if (StringUtils.isBlank(teamName) || teamName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        // 3. 描述长度限制 512（可以不设置）
        String description = team.getDescription();
        if (StringUtils.isBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        // 4. 队伍状态参数 0,1,2（默认为 0）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        if (status < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态码错误");
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByCode(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态码错误");
        }
        String teamPassword = team.getTeamPassword();
        if (statusEnum.equals(TeamStatusEnum.SECRET_STATUS)
                && (StringUtils.isBlank(teamPassword) || teamPassword.length() < 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
        }
        // 5. 超时时间检验（大于当前时间、可以不设置）
        Date expireTime = team.getExpireTime();
        if (expireTime != null && new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间设置有误");
        }
        // 6. 用户创建的队伍数目限制 5
        // todo 并发问题
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Long hasTeamNums = teamMapper.selectCount(queryWrapper);
        if (hasTeamNums >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍达到上线");
        }
        // 7. 创建队伍和队伍用户关系
        return createTeamTransaction(team, userId);
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = teamMapper.selectById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 仅允许管理员或者不是队伍的创建者修改队伍信息
        if (loginUser.getUserRole() != ADMIN_ROLE && !loginUser.getId().equals(oldTeam.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        Integer status = teamUpdateRequest.getStatus();
        String teamPassword = teamUpdateRequest.getTeamPassword();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByCode(status);

        // 如果想要更新队伍状态为加密,则需要传入队伍密码
        if (statusEnum.equals(TeamStatusEnum.SECRET_STATUS) && StringUtils.isBlank(teamPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果队伍状态更新为公开或者私有，则清空密码
        if (!statusEnum.equals(TeamStatusEnum.SECRET_STATUS)) {
            teamUpdateRequest.setTeamPassword("");
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamUpdateRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return this.updateById(team);
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
    public List<TeamUserVo> getTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 1. 组合查询条件(根据teamQuery中存在的查询条件拼接)
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            // 根据关键词查询
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("team_name", searchText).or().like("description", searchText));
            }
            String teamName = teamQuery.getTeamName();
            if (StringUtils.isNotBlank(teamName)) {
                queryWrapper.like("team_name", teamName);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("max_num", maxNum);
            }
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("user_id", userId);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusNums = TeamStatusEnum.getEnumByCode(status);
            if (statusNums == null) {
                statusNums = TeamStatusEnum.PUBLIC_STATUS;
            }
            // 仅允许管理员查询私有或者加密的用户
            if (!isAdmin && !statusNums.equals(TeamStatusEnum.PUBLIC_STATUS)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", statusNums.getStatusCode());
        }
        // 不展示已过期的队伍 expire_time is null or expire_time > now()
        queryWrapper.and(qw -> qw.isNull("expire_time").or().gt("expire_time", new Date()));

        List<Team> teamList = teamMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        // 2. 关联查询创建人的信息(实现方式1: 自定义Sql; 实现方式2: 使用api)
        // todo 关联查询队伍的所有用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userMapper.selectById(userId);
            // 用户信息、队伍信息脱敏处理
            TeamUserVo teamUserVo = new TeamUserVo();
            UserVo userVo = new UserVo();
            try {
                BeanUtils.copyProperties(teamUserVo, team);
                BeanUtils.copyProperties(userVo, user);
            } catch (Exception e) {
                throw new RuntimeException();
            }
            teamUserVo.setCreateUser(userVo);
            teamUserVoList.add(teamUserVo);
        }
        return teamUserVoList;
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long teamId = teamJoinRequest.getId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 队伍必须存在，只能加入未过期的队伍
        Team team = teamMapper.selectById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        if (team.getExpireTime() != null && team.getExpireTime().before(new Date())) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "队伍已过期");
        }

        // 不能加入私有队伍
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByCode(status);
        if (statusEnum == TeamStatusEnum.PRIVATE_STATUS) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "队伍为私密状态");
        }

        // 加入加密队伍，需要密码匹配
        String teamPassword = teamJoinRequest.getTeamPassword();
        if (statusEnum == TeamStatusEnum.SECRET_STATUS) {
            if (StringUtils.isBlank(teamPassword) || !teamPassword.equals(team.getTeamPassword())) {
                throw new BusinessException(ErrorCode.EXECUTE_FAIL, "密码传入错误");
            }
        }

        // 用户最多加入 5 个队伍
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        Long teamCounts = userTeamMapper.selectCount(queryWrapper);
        if (teamCounts >= 5) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "用户加入队伍已达上限");
        }

        // 只能加入未满的队伍
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        Long teamUserCounts = userTeamMapper.selectCount(queryWrapper);
        if(teamUserCounts >= team.getMaxNum()) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "队伍已满");
        }

        // 不能重复加入已经加入的队伍（幂等性）
        // todo 并发控制
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        queryWrapper.eq("user_id", userId);
        Long hasUserJoinTeam = userTeamMapper.selectCount(queryWrapper);
        if(hasUserJoinTeam > 0) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "用户已加入队伍");
        }

        // 新增队伍-用户关联信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    /**
     * 同时创建队伍和构建队伍、用户关系(使用事务保证原子性)
     *
     * @param team   队伍
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
        if (result == 0 || teamId == null) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "创建队伍失败");
        }
        // 2. 创建队伍、用户关系(保证事务完整性)
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(team.getUserId());
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        int insertRelation = userTeamMapper.insert(userTeam);
        if (insertRelation == 0) {
            throw new BusinessException(ErrorCode.EXECUTE_FAIL, "创建队伍、用户关系失败");
        }
        return teamId;
    }
}




