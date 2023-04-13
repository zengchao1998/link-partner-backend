create table tag
(
    id          bigint auto_increment comment 'id'
        primary key,
    tag_name    varchar(256)                       null comment '标签名称',
    userId      bigint                             null comment '用户id',
    parentId    bigint                             null comment '父标签id',
    isParent    tinyint                            null comment '是否为父标签 0 - 1',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint  default 0                 not null comment '是否删除',
    constraint unique_tag_name
        unique (id)
)
    collate = utf8_bin;

create index index_userId
    on tag (userId);
