create table orders
(
    order_id         varchar(32)                                                                                                              not null comment '订单号'
        primary key,
    user_id          int                                                                                                                      not null comment '用户ID',
    restaurant_id    int                                                                                                                      not null comment '餐厅ID',
    total_amount     decimal(10, 2)                                                                                                           not null comment '商品总价',
    delivery_fee     decimal(10, 2)                                                                                 default 0.00              null comment '配送费',
    discount_amount  decimal(10, 2)                                                                                 default 0.00              null comment '优惠金额',
    final_amount     decimal(10, 2)                                                                                                           not null comment '实付金额',
    delivery_address varchar(200)                                                                                                             not null comment '配送地址',
    delivery_phone   varchar(20)                                                                                                              not null comment '配送电话',
    delivery_name    varchar(50)                                                                                                              not null comment '收货人姓名',
    note             text                                                                                                                     null comment '用户备注',
    status           enum ('pending', 'confirmed', 'preparing', 'delivering', 'completed', 'cancelled', 'refunded') default 'pending'         null comment '订单状态',
    payment_method   enum ('alipay', 'wechat', 'balance', 'cash')                                                                             not null comment '支付方式',
    payment_status   enum ('pending', 'paid', 'refunded')                                                           default 'pending'         null comment '支付状态',
    paid_at          timestamp                                                                                                                null comment '支付时间',
    completed_at     timestamp                                                                                                                null comment '完成时间',
    cancelled_at     timestamp                                                                                                                null comment '取消时间',
    created_at       timestamp                                                                                      default CURRENT_TIMESTAMP null comment '创建时间',
    constraint orders_ibfk_1
        foreign key (user_id) references users (user_id),
    constraint orders_ibfk_2
        foreign key (restaurant_id) references restaurants (restaurant_id)
)
    comment '订单表，存储用户下单的完整订单信息';

