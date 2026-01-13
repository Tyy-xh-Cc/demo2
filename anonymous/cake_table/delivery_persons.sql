create table delivery_persons
(
    delivery_id      int auto_increment comment '配送员ID'
        primary key,
    name             varchar(50)                                                     not null comment '配送员姓名',
    phone            varchar(20)                                                     not null comment '联系电话',
    id_card          varchar(20)                                                     null comment '身份证号',
    vehicle_type     enum ('bicycle', 'motorcycle', 'car')                           null comment '交通工具类型',
    license_plate    varchar(20)                                                     null comment '车牌号',
    current_location point                                                           null comment '当前位置（空间坐标）',
    status           enum ('available', 'busy', 'offline') default 'offline'         null comment '配送员状态',
    rating           decimal(3, 2)                         default 0.00              null comment '评分',
    completed_orders int                                   default 0                 null comment '已完成订单数',
    created_at       timestamp                             default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '配送员信息表，存储配送员的个人信息和工作状态';

