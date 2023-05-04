create table team
(
    id            bigint auto_increment comment 'id'
        primary key,
    team_name     varchar(256)                       not null comment '队伍名称',
    description   varchar(1024)                      null comment '队伍描述',
    max_num       int      default 1                 not null comment '队伍最大人数',
    expire_time   datetime                           null comment '过期时间',
    user_id       bigint                             null comment '创建用户id(队长id)',
    status        int      default 0                 null comment '0-公开状态, 1-私有状态, 2-加密状态',
    team_password varchar(256)                       null comment '队伍申请密码',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '是否删除'
);

