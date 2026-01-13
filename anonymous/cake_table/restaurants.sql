create table restaurants
(
    restaurant_id           int auto_increment comment '餐厅ID'
        primary key,
    name                    varchar(100)                                                         not null comment '餐厅名称',
    description             text                                                                 null comment '餐厅描述',
    logo_url                varchar(500)                                                         null comment '餐厅Logo URL',
    cover_url               varchar(500)                                                         null comment '餐厅封面图URL',
    phone                   varchar(20)                                                          not null comment '餐厅联系电话',
    address                 varchar(200)                                                         not null comment '餐厅地址',
    location                point                                                                null comment '地理位置（空间坐标）',
    opening_hours           varchar(100)                                                         null comment '营业时间',
    min_order_amount        decimal(10, 2)                             default 0.00              null comment '最低起送金额',
    delivery_fee            decimal(10, 2)                             default 0.00              null comment '配送费',
    estimated_delivery_time int                                                                  null comment '预计配送时间(分钟)',
    rating                  decimal(3, 2)                              default 0.00              null comment '餐厅评分',
    total_orders            int                                        default 0                 null comment '总订单数',
    status                  enum ('open', 'closed', 'busy', 'resting') default 'closed'          null comment '餐厅状态',
    created_at              timestamp                                  default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '餐厅信息表，存储餐厅的基本信息、位置和营业状态';

