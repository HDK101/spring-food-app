create sequence users_seq start with 1 increment by 1;
create table users (
    id bigint not null,
    login varchar(255),
    name varchar(255),
    password_hash varchar(255),
    primary key (id),
    unique(login)
);