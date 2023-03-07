create table if not exists `bookings`
(
    id                  bigint not null auto_increment,
    code                varchar(8),
    customer_name       varchar(255) not null,
    customer_email      varchar(255),
    customer_phone      varchar(11) not null,
    address             text,
    booking_amount      NUMERIC(19, 2) not null default 0,
    shipping_fee        NUMERIC(19, 2) not null default 0,
    other_fee           NUMERIC(19, 2) not null default 0,
    tax                 NUMERIC(19, 2) not null default 0,
    discount_amount     NUMERIC(19, 2) not null default 0,
    total_amount        NUMERIC(19, 2) not null default 0,
    promotion_code      varchar(50),
    customer_note       varchar(255),
    payment_method_code varchar(20),
    payment_status      varchar(20),
    booking_status      varchar(20),
    is_deleted          tinyint(1),
    payment_time        timestamp,
    completed_time      timestamp,
    cancelled_time      timestamp,
    created_by          varchar(255),
    created_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by          varchar(255),
    updated_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint          bookings_pkey primary key (id)
);
CREATE INDEX `bookings_is_deleted_IDX` USING BTREE ON bookings(is_deleted);
CREATE INDEX `bookings_payment_status_IDX` USING BTREE ON bookings(payment_status);
CREATE INDEX `bookings_booking_status_IDX` USING BTREE ON bookings(booking_status);

create table if not exists booking_products
(
    id                      bigint not null auto_increment,
    booking_id              bigint not null,
    product_id              bigint not null,
    product_name            varchar(255),
    product_code            varchar(255),
    product_image           text,
    product_description     text,
    product_price           NUMERIC(19, 2) not null default 0,
    quantity                int not null default 0,
    total_price             NUMERIC(19, 2) not null default 0,
    product_discount        NUMERIC(19, 2) null,
    product_created_year    int null,
    constraint              booking_products_pkey primary key(id),
    constraint              uk_booking_products_booking_id_product_id unique (booking_id, product_id),
    constraint              fk_booking_products_products_product_id foreign key (product_id) references products(id),
    constraint              fk_booking_products_bookings_booking_id foreign key (product_id) references bookings(id)
);