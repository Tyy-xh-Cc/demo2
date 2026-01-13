create table cart_items
(
    cart_item_id   int auto_increment comment '购物车项ID'
        primary key,
    user_id        int                                 not null comment '用户ID',
    restaurant_id  int                                 not null comment '餐厅ID',
    product_id     int                                 not null comment '商品ID',
    quantity       int       default 1                 not null comment '商品数量',
    specifications text                                null comment '规格选择（JSON格式）',
    created_at     timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint unique_user_product
        unique (user_id, product_id),
    constraint cart_items_ibfk_1
        foreign key (user_id) references users (user_id)
            on delete cascade,
    constraint cart_items_ibfk_2
        foreign key (product_id) references products (product_id)
            on delete cascade
)
    comment '购物车表，存储用户加入购物车的商品信息';

