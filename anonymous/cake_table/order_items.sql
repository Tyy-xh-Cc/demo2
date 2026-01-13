create table order_items
(
    order_item_id  int auto_increment comment '订单商品ID'
        primary key,
    order_id       varchar(32)    not null comment '订单号',
    product_id     int            not null comment '商品ID',
    product_name   varchar(100)   not null comment '商品名称',
    quantity       int            not null comment '商品数量',
    unit_price     decimal(10, 2) not null comment '商品单价',
    total_price    decimal(10, 2) not null comment '商品总价',
    specifications text           null comment '规格信息（JSON格式）',
    constraint order_items_ibfk_1
        foreign key (order_id) references orders (order_id)
            on delete cascade
)
    comment '订单商品表，存储订单中每个商品的详细信息';

