create sequence roles_seq start with 1 increment by 1;
create table roles (id bigint not null, name varchar(255), role varchar(255), primary key (id));