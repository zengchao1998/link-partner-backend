package com.wut.link.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wut.link.model.domain.Tag;
import com.wut.link.service.TagService;
import com.wut.link.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Zeng
* description 针对表【tag】的数据库操作Service实现
* createDate 2023-04-13 22:49:51
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

}




