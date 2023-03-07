
create table if not exists `product_types`
(
    id                  bigint not null auto_increment,
    name                varchar(255),
    code                varchar(255),
    description         text,
    image               text,
    seo_title           varchar(255),
    seo_description     text,
    robot_tag           varchar(255),
    canonical           text,
    keywords            varchar(255),
    is_deleted          tinyint(1),
    created_by          varchar(255),
    created_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by          varchar(255),
    updated_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint          product_types_pkey primary key (id),
    constraint          uk_product_types_code unique (code)
);

ALTER TABLE products ADD COLUMN product_code varchar(255) default null after code;
ALTER TABLE products ADD CONSTRAINT uk_products_product_code unique (product_code);

ALTER TABLE products ADD COLUMN product_type_id bigint default null after region_id;
ALTER TABLE products ADD CONSTRAINT fk_products_product_type_product_type_id foreign key (product_type_id) references product_types(id);