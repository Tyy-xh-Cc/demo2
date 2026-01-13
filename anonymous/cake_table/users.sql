create table users
(
    user_id       int auto_increment comment '用户ID'
        primary key,
    username      varchar(50)                                                     not null comment '用户名',
    password_hash varchar(255)                                                    not null comment '密码哈希',
    phone         varchar(20)                                                     not null comment '手机号',
    email         varchar(100)                                                    null comment '邮箱',
    avatar_url    varchar(500)                                                    null comment '头像URL',
    balance       decimal(10, 2)                        default 0.00              null comment '账户余额',
    created_at    timestamp                             default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at    timestamp                             default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    status        enum ('active', 'inactive', 'banned') default 'active'          null comment '用户状态',
    constraint email
        unique (email),
    constraint phone
        unique (phone),
    constraint username
        unique (username)
)
    comment '用户信息表，存储平台用户的基本信息和账户状态';

