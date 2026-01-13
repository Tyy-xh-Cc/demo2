create table deliveries
(
    delivery_record_id int auto_increment comment '配送记录ID'
        primary key,
    order_id           varchar(32)                                                                        not null comment '订单号',
    delivery_id        int                                                                                not null comment '配送员ID',
    pickup_time        timestamp                                                                          null comment '取餐时间',
    delivered_time     timestamp                                                                          null comment '送达时间',
    status             enum ('assigned', 'picked_up', 'delivered', 'cancelled') default 'assigned'        null comment '配送状态',
    created_at         timestamp                                                default CURRENT_TIMESTAMP null comment '创建时间',
    constraint unique_order_delivery
        unique (order_id),
    constraint deliveries_ibfk_1
        foreign key (order_id) references orders (order_id),
    constraint deliveries_ibfk_2
        foreign key (delivery_id) references delivery_persons (delivery_id)
)
    comment '配送记录表，存储订单的配送状态和配送员信息';

