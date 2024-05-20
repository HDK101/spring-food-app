create table order_foods (
    food_id bigint not null,
    order_id bigint not null,
    primary key (food_id, order_id),
    foreign key (food_id) references foods,
    foreign key (order_id) references orders
);