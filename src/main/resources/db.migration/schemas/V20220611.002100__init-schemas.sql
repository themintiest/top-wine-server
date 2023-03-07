create table if not exists `accounts`
(
    id           bigint not null auto_increment,
    email        varchar(100) NOT NULL,
    password     varchar(255) NOT NULL,
    status       varchar(10) NOT NULL DEFAULT 'ACTIVE',
    username     varchar(100) NOT NULL,
    created_by   varchar(255),
    created_time timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by   varchar(255),
    updated_time timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint accounts_pkey primary key (id),
    constraint uk_account_email unique (email),
    constraint uk_account_username unique (username)
)ENGINE=InnoDB AUTO_INCREMENT=3;
CREATE INDEX `accounts_status_IDX` USING BTREE ON wines.accounts (status);

create table if not exists `roles`
(
    id          bigint not null auto_increment,
    description text,
    name        varchar(50),
    is_system   boolean not null default true,
    constraint roles_pkey primary key (id),
    constraint uk_role_name unique (name)
)ENGINE=InnoDB AUTO_INCREMENT=3;

create table if not exists `accounts_roles`
(
    id bigint       not null auto_increment,
    account_id      bigint not null,
    role_id         bigint not null,
    constraint      accounts_roles_pkey primary key (id),
    constraint      fk_accounts_roles_role_id foreign key (role_id) references roles(id),
    constraint      fk_accounts_roles_account_id foreign key (account_id) references accounts(id)
)ENGINE=InnoDB AUTO_INCREMENT=5;

create table if not exists `profiles`
(
    id              bigint not null auto_increment,
    avatar_url      text,
    email           varchar(100),
    first_name      varchar(255),
    last_name       varchar(255),
    username        varchar(100),
    account_id      bigint not null,
    created_by      varchar(255),
    created_time    timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by      varchar(255),
    updated_time    timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint      users_pkey primary key (id),
    constraint      uk_user_email unique (email),
    constraint      uk_user_username unique (username),
    constraint      fk_users_accounts_account_id foreign key (account_id) references accounts(id)
)ENGINE=InnoDB AUTO_INCREMENT=3;

create table if not exists `categories`
(
    id                  bigint not null auto_increment,
    name                varchar(255),
    code                varchar(255),
    description         text,
    parent_id           bigint null,
    image               text,
    seo_title           varchar(255),
    seo_description     text,
    robot_tag           varchar(255),
    is_deleted          tinyint(1),
    created_by          varchar(255),
    created_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by          varchar(255),
    updated_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint          categories_pkey primary key (id),
    constraint          uk_categories_code unique (code),
    constraint          fk_categories_categories_parent_id foreign key (parent_id) references categories(id)
);
CREATE INDEX `categories_is_deleted_IDX` USING BTREE ON categories(is_deleted);

create table if not exists `products`
(
    id                  bigint not null auto_increment,
    name                varchar(255),
    code                varchar(255),
    image               text,
    description         text,
    seo_title           varchar(255),
    seo_description     text,
    canonical           text,
    robot_tag           varchar(255),
    price               NUMERIC(19, 2),
    discount            NUMERIC(19, 2),
    quantity            int not null default 0,
    sold                int not null default 0,
    grape_type          varchar(100),
    concentration       double not null default 0,
    capacity            int not null default 0,
    created_year        int null,
    is_deleted          tinyint(1),
    created_by          varchar(255),
    created_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    updated_by          varchar(255),
    updated_time        timestamp not null DEFAULT CURRENT_TIMESTAMP,
    constraint          products_pkey primary key (id),
    constraint          uk_products_code unique (code)
);
CREATE INDEX `products_is_deleted_IDX` USING BTREE ON products(is_deleted);

create table if not exists `product_images`
(
    id              bigint not null auto_increment,
    image           text,
    product_id      bigint not null,
    constraint      product_images_pkey primary key (id),
    constraint      fk_product_images_products_product_id foreign key (product_id) references products(id)
);
CREATE INDEX `product_images_product_id_IDX` USING BTREE ON product_images(product_id);

create table if not exists `product_categories`
(
    id              bigint not null auto_increment,
    product_id      bigint not null,
    category_id     bigint not null,
    constraint      products_categories_pk primary key (id),
    constraint      uk_products_categories_product_id_category_id unique (product_id, category_id),
    constraint      fk_product_categories_products_product_id foreign key (product_id) references products(id),
    constraint      fk_product_categories_categories_category_id foreign key (category_id) references categories(id)
);

create table if not exists `nations`
(
    id              bigint not null auto_increment,
    name            varchar(255) not null,
    flag            text,
    description     text,
    constraint      countries_pk primary key (id)
);