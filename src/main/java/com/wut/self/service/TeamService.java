package com.wut.self.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wut.self.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wut.self.model.domain.User;
import com.wut.self.model.dto.TeamQuery;

import java.util.List;

/**
* @author Administrator
* @description 针对表【team】的数据库操作Service
* @createDate 2023-04-24 12:38:09
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
     * @param team 队伍对象
     * @return 是否成功
     */
    boolean updateTeam(Team team);

    /**
     * 查询队伍信息
     * @param teamQuery 查询队伍信息的包装类
     * @return 队伍记录
     */
    Page<Team> getTeamsForPage(TeamQuery teamQuery);

    /**
     * 查询队伍信息(集合形式显示)
     * @param teamQuery 查询队伍信息的包装类
     * @return 队伍记录
     */
    List<Team> getTeams(TeamQuery teamQuery);
}
