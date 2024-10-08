create schema loading
        create table parcels (
        id bigserial primary key,
        name varchar(50) not null unique,
        symbol char(1) not null unique,
        box bytea not null unique
    );