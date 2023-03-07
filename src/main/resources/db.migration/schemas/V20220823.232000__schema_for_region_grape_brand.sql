DROP TABLE IF EXISTS nations;

ALTER TABLE products DROP COLUMN grape_type;
ALTER TABLE products ADD COLUMN keywords varchar(255) default null after description;
ALTER TABLE categories ADD COLUMN keywords varchar(255) default null after description;
ALTER TABLE categories ADD COLUMN canonical text default null after description;

create table if not exists `regions`
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
    constraint          regions_pkey primary key (id),
    constraint          uk_regions_code unique (code)
);
CREATE INDEX `regions_is_deleted_IDX` USING BTREE ON regions(is_deleted);

create table if not exists `grapes`
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
    constraint          grapes_pkey primary key (id),
    constraint          uk_grapes_code unique (code)
    );
CREATE INDEX `grapes_is_deleted_IDX` USING BTREE ON grapes(is_deleted);

create table if not exists `brands`
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
    constraint          brands_pkey primary key (id),
    constraint          uk_brands_code unique (code)
    );
CREATE INDEX `brands_is_deleted_IDX` USING BTREE ON brands(is_deleted);

create table if not exists `nations`
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
    constraint          nations_pkey primary key (id),
    constraint          uk_nations_code unique (code)
    );
CREATE INDEX `nations_is_deleted_IDX` USING BTREE ON nations(is_deleted);

ALTER TABLE products ADD COLUMN region_id bigint default null after created_year;
ALTER TABLE products ADD COLUMN brand_id bigint default null after region_id;
ALTER TABLE products ADD COLUMN grape_id bigint default null after brand_id;
ALTER TABLE products ADD COLUMN nation_id bigint default null after grape_id;

ALTER TABLE products ADD CONSTRAINT fk_products_regions_region_id foreign key (region_id) references regions(id);
ALTER TABLE products ADD CONSTRAINT fk_products_grapes_grape_id foreign key (grape_id) references grapes(id);
ALTER TABLE products ADD CONSTRAINT fk_products_brands_brand_id foreign key (brand_id) references brands(id);
ALTER TABLE products ADD CONSTRAINT fk_products_nations_nation_id foreign key (nation_id) references nations(id);

CREATE INDEX `products_region_id_IDX` USING BTREE ON products(region_id);
CREATE INDEX `products_brand_id_IDX` USING BTREE ON products(brand_id);
CREATE INDEX `products_grape_id_IDX` USING BTREE ON products(grape_id);
CREATE INDEX `products_nation_id_IDX` USING BTREE ON products(nation_id);
