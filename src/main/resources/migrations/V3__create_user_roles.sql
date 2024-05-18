create table user_roles (
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id),
    foreign key (role_id) references roles,
    foreign key (user_id) references users
);