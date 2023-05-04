package com.wut.self.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wut.self.model.domain.User;
import com.wut.self.model.dto.TeamQuery;
import com.wut.self.common.DeleteRequest;
import com.wut.self.model.request.TeamJoinRequest;
import com.wut.self.model.request.TeamQuitRequest;
import com.wut.self.model.request.TeamUpdateRequest;
import com.wut.self.model.vo.TeamUserVo;

import java.util.List;

/**
* @author Administrator
* description 针对表【team】的数据库操作Service
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team 队伍实体
     * @return 是否成功
     */
    long addTeam(Team team, User loginUser);

    /**
     * 更新队伍信息
     *
     * @param teamUpdateRequest 队伍对象
     * @param loginUser 当前登录用户
     * @return 是否成功
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 查询队伍信息
     *
     * @param teamQuery 查询队伍信息的包装类
     * @param isAdmin 是否为管理员
     * @return 队伍记录
     */
    Page<Team> getTeamsForPage(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 查询队伍信息(集合形式显示)
     *
     * @param teamQuery 查询队伍信息的包装类
     * @param isAdmin 是否为管理员
     * @return 队伍记录
     */
    List<TeamUserVo> getTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 用户加入队伍
     * @param teamJoinRequest 加入的队伍信息
     * @param loginUser 当前登录用户
     * @return 是否成功
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 删除队伍
     * @param deleteRequest 队伍id
     * @param loginUser 当前用户
     * @return 是否成功
     */
    boolean deleteTeam(DeleteRequest deleteRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest 退出队伍的信息
     * @param loginUser 登录用户
     * @return 是否成功
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 查询当前用户已经加入的队伍
     * @param loginUser 登录用户
     * @return 加入的队伍列表
     */
    List<TeamUserVo> getJoinedTeams(User loginUser);

    /**
     * 查询当前用户创建的队伍
     * @param loginUser 登录用户
     * @return 创建的队伍列表
     */
    List<Team> getOwnerTeams(User loginUser);

    /**
     * 判断当前用户是否加入teamList中的队伍
     * 判断当前队伍的人数
     * @param loginUser 当前用户
     * @param teamList 队伍列表
     * @return 设置是否 hasJoin 状态的队伍列表，设置队伍人数的队伍列表
     */
    List<TeamUserVo> aboutTeams(User loginUser, List<TeamUserVo> teamList);
}
