package com.wut.self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wut.self.model.domain.UserTeam;
import com.wut.self.service.UserTeamService;
import com.wut.self.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* description 针对表【user_team】的数据库操作Service实现
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




