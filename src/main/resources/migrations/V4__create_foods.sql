create sequence foods_seq start with 1 increment by 1;
create table foods (
    id bigint not null,
    name varchar(255),
    price_in_cents bigint not null,
    primary key (id),
    unique (id)
);