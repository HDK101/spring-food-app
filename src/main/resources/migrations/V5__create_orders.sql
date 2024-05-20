create sequence orders_seq start with 1 increment by 1;
create table orders (
    id bigint not null,
    user_id bigint not null,
    foreign key (user_id) references users,
    primary key (id)
);