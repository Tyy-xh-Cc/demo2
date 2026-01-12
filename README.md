use cake_table;
CREATE TABLE delivery_persons
(
    delivery_id      INT AUTO_INCREMENT PRIMARY KEY COMMENT '配送员ID',
    name             VARCHAR(50)                                                     NOT NULL COMMENT '配送员姓名',
    phone            VARCHAR(20)                                                     NOT NULL COMMENT '联系电话',
    id_card          VARCHAR(20)                                                     NULL COMMENT '身份证号',
    vehicle_type     ENUM ('bicycle', 'motorcycle', 'car')                           NULL COMMENT '交通工具类型',
    license_plate    VARCHAR(20)                                                     NULL COMMENT '车牌号',
    current_location POINT                                                           NULL COMMENT '当前位置（空间坐标）',
    status           ENUM ('available', 'busy', 'offline') DEFAULT 'offline'         NULL COMMENT '配送员状态',
    rating           DECIMAL(3, 2)                         DEFAULT 0.00              NULL COMMENT '评分',
    completed_orders INT                                   DEFAULT 0                 NULL COMMENT '已完成订单数',
    created_at       TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) COMMENT = '配送员信息表，存储配送员的个人信息和工作状态';

-- 餐厅表
CREATE TABLE restaurants
(
    restaurant_id           INT AUTO_INCREMENT PRIMARY KEY COMMENT '餐厅ID',
    name                    VARCHAR(100)                                                         NOT NULL COMMENT '餐厅名称',
    description             TEXT                                                                 NULL COMMENT '餐厅描述',
    logo_url                VARCHAR(500)                                                         NULL COMMENT '餐厅Logo URL',
    cover_url               VARCHAR(500)                                                         NULL COMMENT '餐厅封面图URL',
    phone                   VARCHAR(20)                                                          NOT NULL COMMENT '餐厅联系电话',
    address                 VARCHAR(200)                                                         NOT NULL COMMENT '餐厅地址',
    location                POINT                                                                NOT NULL COMMENT '地理位置（空间坐标）',
    opening_hours           VARCHAR(100)                                                         NULL COMMENT '营业时间',
    min_order_amount        DECIMAL(10, 2)                             DEFAULT 0.00              NULL COMMENT '最低起送金额',
    delivery_fee            DECIMAL(10, 2)                             DEFAULT 0.00              NULL COMMENT '配送费',
    estimated_delivery_time INT                                                                  NULL COMMENT '预计配送时间(分钟)',
    rating                  DECIMAL(3, 2)                              DEFAULT 0.00              NULL COMMENT '餐厅评分',
    total_orders            INT                                        DEFAULT 0                 NULL COMMENT '总订单数',
    status                  ENUM ('open', 'closed', 'busy', 'resting') DEFAULT 'closed'          NULL COMMENT '餐厅状态',
    created_at              TIMESTAMP                                  DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间'
) COMMENT = '餐厅信息表，存储餐厅的基本信息、位置和营业状态';

-- 商品分类表
CREATE TABLE product_categories
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    restaurant_id INT           NOT NULL COMMENT '餐厅ID',
    name          VARCHAR(50)   NOT NULL COMMENT '分类名称',
    sort_order    INT DEFAULT 0 NULL COMMENT '排序顺序',
    CONSTRAINT product_categories_ibfk_1
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id)
            ON DELETE CASCADE
) COMMENT = '商品分类表，存储餐厅内的商品分类信息';

-- 商品表
CREATE TABLE products
(
    product_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    restaurant_id  INT                                                                NOT NULL COMMENT '餐厅ID',
    category_id    INT                                                                NULL COMMENT '分类ID',
    name           VARCHAR(100)                                                       NOT NULL COMMENT '商品名称',
    description    TEXT                                                               NULL COMMENT '商品描述',
    image_url      VARCHAR(500)                                                       NULL COMMENT '商品图片URL',
    price          DECIMAL(10, 2)                                                     NOT NULL COMMENT '商品价格',
    original_price DECIMAL(10, 2)                                                     NULL COMMENT '商品原价',
    stock          INT                                      DEFAULT -1                NULL COMMENT '库存（-1表示无限库存）',
    sales_count    INT                                      DEFAULT 0                 NULL COMMENT '销售数量',
    sort_order     INT                                      DEFAULT 0                 NULL COMMENT '排序顺序',
    status         ENUM ('available', 'sold_out', 'hidden') DEFAULT 'available'       NULL COMMENT '商品状态',
    created_at     TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    updated_at     TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT products_ibfk_1
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id)
            ON DELETE CASCADE
) COMMENT = '商品信息表，存储餐厅出售的商品详情';

-- 用户表
CREATE TABLE users
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username      VARCHAR(50)                                                     NOT NULL COMMENT '用户名',
    password_hash VARCHAR(255)                                                    NOT NULL COMMENT '密码哈希',
    phone         VARCHAR(20)                                                     NOT NULL COMMENT '手机号',
    email         VARCHAR(100)                                                    NULL COMMENT '邮箱',
    avatar_url    VARCHAR(500)                                                    NULL COMMENT '头像URL',
    balance       DECIMAL(10, 2)                        DEFAULT 0.00              NULL COMMENT '账户余额',
    created_at    TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    updated_at    TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status        ENUM ('active', 'inactive', 'banned') DEFAULT 'active'          NULL COMMENT '用户状态',
    CONSTRAINT email
        UNIQUE (email),
    CONSTRAINT phone
        UNIQUE (phone),
    CONSTRAINT username
        UNIQUE (username)
) COMMENT = '用户信息表，存储平台用户的基本信息和账户状态';

-- 地址表
CREATE TABLE addresses
(
    address_id    INT AUTO_INCREMENT PRIMARY KEY COMMENT '地址ID',
    user_id       INT                                  NOT NULL COMMENT '用户ID',
    name          VARCHAR(50)                          NOT NULL COMMENT '地址名称（如：家、公司）',
    receiver_name VARCHAR(50)                          NOT NULL COMMENT '收货人姓名',
    phone         VARCHAR(20)                          NOT NULL COMMENT '收货人电话',
    address       VARCHAR(200)                         NOT NULL COMMENT '详细地址',
    latitude      DECIMAL(10, 8)                       NULL COMMENT '纬度',
    longitude     DECIMAL(11, 8)                       NULL COMMENT '经度',
    is_default    TINYINT(1) DEFAULT 0                 NULL COMMENT '是否默认地址',
    created_at    TIMESTAMP  DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    CONSTRAINT addresses_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (user_id)
            ON DELETE CASCADE
) COMMENT = '用户地址表，存储用户的收货地址信息';

-- 购物车表
CREATE TABLE cart_items
(
    cart_item_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项ID',
    user_id        INT                                 NOT NULL COMMENT '用户ID',
    restaurant_id  INT                                 NOT NULL COMMENT '餐厅ID',
    product_id     INT                                 NOT NULL COMMENT '商品ID',
    quantity       INT       DEFAULT 1                 NOT NULL COMMENT '商品数量',
    specifications TEXT                                NULL COMMENT '规格选择（JSON格式）',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT unique_user_product
        UNIQUE (user_id, product_id),
    CONSTRAINT cart_items_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (user_id)
            ON DELETE CASCADE,
    CONSTRAINT cart_items_ibfk_2
        FOREIGN KEY (product_id) REFERENCES products (product_id)
            ON DELETE CASCADE
) COMMENT = '购物车表，存储用户加入购物车的商品信息';

-- 订单表
CREATE TABLE orders
(
    order_id         VARCHAR(32)                                                                                                              NOT NULL COMMENT '订单号'
        PRIMARY KEY,
    user_id          INT                                                                                                                      NOT NULL COMMENT '用户ID',
    restaurant_id    INT                                                                                                                      NOT NULL COMMENT '餐厅ID',
    total_amount     DECIMAL(10, 2)                                                                                                           NOT NULL COMMENT '商品总价',
    delivery_fee     DECIMAL(10, 2)                                                                                 DEFAULT 0.00              NULL COMMENT '配送费',
    discount_amount  DECIMAL(10, 2)                                                                                 DEFAULT 0.00              NULL COMMENT '优惠金额',
    final_amount     DECIMAL(10, 2)                                                                                                           NOT NULL COMMENT '实付金额',
    delivery_address VARCHAR(200)                                                                                                             NOT NULL COMMENT '配送地址',
    delivery_phone   VARCHAR(20)                                                                                                              NOT NULL COMMENT '配送电话',
    delivery_name    VARCHAR(50)                                                                                                              NOT NULL COMMENT '收货人姓名',
    note             TEXT                                                                                                                     NULL COMMENT '用户备注',
    status           ENUM ('pending', 'confirmed', 'preparing', 'delivering', 'completed', 'cancelled', 'refunded') DEFAULT 'pending'         NULL COMMENT '订单状态',
    payment_method   ENUM ('alipay', 'wechat', 'balance', 'cash')                                                                             NOT NULL COMMENT '支付方式',
    payment_status   ENUM ('pending', 'paid', 'refunded')                                                           DEFAULT 'pending'         NULL COMMENT '支付状态',
    paid_at          TIMESTAMP                                                                                                                NULL COMMENT '支付时间',
    completed_at     TIMESTAMP                                                                                                                NULL COMMENT '完成时间',
    cancelled_at     TIMESTAMP                                                                                                                NULL COMMENT '取消时间',
    created_at       TIMESTAMP                                                                                      DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    CONSTRAINT orders_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT orders_ibfk_2
        FOREIGN KEY (restaurant_id) REFERENCES restaurants (restaurant_id)
) COMMENT = '订单表，存储用户下单的完整订单信息';

-- 配送记录表
CREATE TABLE deliveries
(
    delivery_record_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '配送记录ID',
    order_id           VARCHAR(32)                                                                        NOT NULL COMMENT '订单号',
    delivery_id        INT                                                                                NOT NULL COMMENT '配送员ID',
    pickup_time        TIMESTAMP                                                                          NULL COMMENT '取餐时间',
    delivered_time     TIMESTAMP                                                                          NULL COMMENT '送达时间',
    status             ENUM ('assigned', 'picked_up', 'delivered', 'cancelled') DEFAULT 'assigned'        NULL COMMENT '配送状态',
    created_at         TIMESTAMP                                                DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    CONSTRAINT unique_order_delivery
        UNIQUE (order_id),
    CONSTRAINT deliveries_ibfk_1
        FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT deliveries_ibfk_2
        FOREIGN KEY (delivery_id) REFERENCES delivery_persons (delivery_id)
) COMMENT = '配送记录表，存储订单的配送状态和配送员信息';

-- 订单商品表
CREATE TABLE order_items
(
    order_item_id  INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单商品ID',
    order_id       VARCHAR(32)    NOT NULL COMMENT '订单号',
    product_id     INT            NOT NULL COMMENT '商品ID',
    product_name   VARCHAR(100)   NOT NULL COMMENT '商品名称',
    quantity       INT            NOT NULL COMMENT '商品数量',
    unit_price     DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    total_price    DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
    specifications TEXT           NULL COMMENT '规格信息（JSON格式）',
    CONSTRAINT order_items_ibfk_1
        FOREIGN KEY (order_id) REFERENCES orders (order_id)
            ON DELETE CASCADE
) COMMENT = '订单商品表，存储订单中每个商品的详细信息';
ALTER TABLE delivery_persons COMMENT = '配送员信息表，存储配送员的个人信息和工作状态';
CREATE TABLE banners (
    banner_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '轮播图ID',
    title VARCHAR(100) NOT NULL COMMENT '轮播图标题',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    link_url VARCHAR(500) NULL COMMENT '跳转链接（可为商品、餐厅、活动等）',
    link_type ENUM('product', 'restaurant', 'promotion', 'external', 'none') DEFAULT 'none' COMMENT '链接类型',
    link_id INT NULL COMMENT '关联ID（商品ID、餐厅ID等）',
    sort_order INT DEFAULT 0 COMMENT '排序（数字越小越靠前）',
    status ENUM('active', 'inactive', 'deleted') DEFAULT 'active' COMMENT '状态',
    start_time TIMESTAMP NULL COMMENT '开始显示时间',
    end_time TIMESTAMP NULL COMMENT '结束显示时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '轮播图表，存储首页轮播图信息';
-- 餐厅表
ALTER TABLE restaurants COMMENT = '餐厅信息表，存储餐厅的基本信息、位置和营业状态';

-- 商品分类表
ALTER TABLE product_categories COMMENT = '商品分类表，存储餐厅内的商品分类信息';

-- 商品表
ALTER TABLE products COMMENT = '商品信息表，存储餐厅出售的商品详情';

-- 用户表
ALTER TABLE users COMMENT = '用户信息表，存储平台用户的基本信息和账户状态';

-- 地址表
ALTER TABLE addresses COMMENT = '用户地址表，存储用户的收货地址信息';

-- 购物车表
ALTER TABLE cart_items COMMENT = '购物车表，存储用户加入购物车的商品信息';

-- 订单表
ALTER TABLE orders COMMENT = '订单表，存储用户下单的完整订单信息';

-- 配送记录表
ALTER TABLE deliveries COMMENT = '配送记录表，存储订单的配送状态和配送员信息';

-- 订单商品表
ALTER TABLE order_items COMMENT = '订单商品表，存储订单中每个商品的详细信息';
