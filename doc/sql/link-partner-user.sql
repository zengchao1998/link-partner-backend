create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    username      varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       null comment '登录账号',
    avatar_url    varchar(1024)                      null comment '头像',
    gender        tinyint                            null comment '性别',
    user_password varchar(256)                       not null comment '密码',
    phone         varchar(128)                       null comment '电话',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '用户状态',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '是否删除',
    user_role     int      default 0                 not null comment '0-普通用户; 1-管理员; 2-vip用户',
    validate_code varchar(256)                       null comment '用户校验码',
    tags          varchar(1024)                      null comment '标签列表 json',
    user_profile  varchar(512)                       null comment '个人简介'
);