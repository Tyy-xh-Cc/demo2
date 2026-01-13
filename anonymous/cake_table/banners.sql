create table banners
(
    banner_id  int auto_increment comment '轮播图ID'
        primary key,
    title      varchar(100)                                                                              not null comment '轮播图标题',
    image_url  varchar(500)                                                                              not null comment '图片URL',
    link_url   varchar(500)                                                                              null comment '跳转链接（可为商品、餐厅、活动等）',
    link_type  enum ('product', 'restaurant', 'promotion', 'external', 'none') default 'none'            null comment '链接类型',
    link_id    int                                                                                       null comment '关联ID（商品ID、餐厅ID等）',
    sort_order int                                                             default 0                 null comment '排序（数字越小越靠前）',
    status     enum ('active', 'inactive', 'deleted')                          default 'active'          null comment '状态',
    start_time timestamp                                                                                 null comment '开始显示时间',
    end_time   timestamp                                                                                 null comment '结束显示时间',
    created_at timestamp                                                       default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at timestamp                                                       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '轮播图表，存储首页轮播图信息';

