create table products
(
    product_id     int auto_increment comment '商品ID'
        primary key,
    restaurant_id  int                                                                not null comment '餐厅ID',
    category_id    int                                                                null comment '分类ID',
    name           varchar(100)                                                       not null comment '商品名称',
    description    text                                                               null comment '商品描述',
    image_url      varchar(500)                                                       null comment '商品图片URL',
    price          decimal(10, 2)                                                     not null comment '商品价格',
    original_price decimal(10, 2)                                                     null comment '商品原价',
    stock          int                                      default -1                null comment '库存（-1表示无限库存）',
    sales_count    int                                      default 0                 null comment '销售数量',
    sort_order     int                                      default 0                 null comment '排序顺序',
    status         enum ('available', 'sold_out', 'hidden') default 'available'       null comment '商品状态',
    created_at     timestamp                                default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at     timestamp                                default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint products_ibfk_1
        foreign key (restaurant_id) references restaurants (restaurant_id)
            on delete cascade
)
    comment '商品信息表，存储餐厅出售的商品详情';

