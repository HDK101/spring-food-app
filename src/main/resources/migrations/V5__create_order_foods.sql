create table order_foods (
    food_id bigint not null,
    user_id bigint not null,
    primary key (food_id, user_id),
    foreign key (food_id) references foods,
    foreign key (user_id) references users
);