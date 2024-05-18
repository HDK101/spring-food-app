create table roles (id bigint not null, name varchar(255), role varchar(255), primary key (id));
create sequence roles_seq start with 1 increment by 1 owned by roles.id;
insert into roles(id, name, role) values(nextval('roles_seq'), 'Client', 'CLIENT');
insert into roles(id, name, role) values(nextval('roles_seq'), 'Admin', 'ADMIN');
