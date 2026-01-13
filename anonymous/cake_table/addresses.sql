create table addresses
(
    address_id    int auto_increment comment '地址ID'
        primary key,
    user_id       int                                  not null comment '用户ID',
    name          varchar(50)                          not null comment '地址名称（如：家、公司）',
    receiver_name varchar(50)                          not null comment '收货人姓名',
    phone         varchar(20)                          not null comment '收货人电话',
    address       varchar(200)                         not null comment '详细地址',
    latitude      decimal(10, 8)                       null comment '纬度',
    longitude     decimal(11, 8)                       null comment '经度',
    is_default    tinyint(1) default 0                 null comment '是否默认地址',
    created_at    timestamp  default CURRENT_TIMESTAMP null comment '创建时间',
    constraint addresses_ibfk_1
        foreign key (user_id) references users (user_id)
            on delete cascade
)
    comment '用户地址表，存储用户的收货地址信息';

