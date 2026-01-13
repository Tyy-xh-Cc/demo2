create table product_categories
(
    category_id   int auto_increment comment '分类ID'
        primary key,
    restaurant_id int           not null comment '餐厅ID',
    name          varchar(50)   not null comment '分类名称',
    sort_order    int default 0 null comment '排序顺序',
    constraint product_categories_ibfk_1
        foreign key (restaurant_id) references restaurants (restaurant_id)
            on delete cascade
)
    comment '商品分类表，存储餐厅内的商品分类信息';

