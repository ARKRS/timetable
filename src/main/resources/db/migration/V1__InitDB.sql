create sequence hibernate_sequence start 1 increment 1;

create table department (
id int8 not null, 
deptname varchar(255), 
ext_code varchar(255), 
is_active boolean, 
user_id int8, primary key (id));

create table department_doc (
docid int8 not null, 
doc_uploaded boolean, 
doc_uuid varchar(255), 
workdate timestamp, 
department_id int8, primary key (docid));

create table doc_employees (
department_doc_docid int8 not null, 
status_col int4, 
employees_key int8 not null, 
primary key (department_doc_docid, employees_key));

create table employee (
employee_id int8 not null, 
ext_code varchar(255), 
fio varchar(255), 
id int8, 
primary key (employee_id));

create table user_role (
user_id int8 not null, 
roles varchar(255));

create table usr (
id int8 not null, 
activation_code varchar(255), 
active boolean not null, 
email varchar(255), 
password varchar(255), 
username varchar(255), 
primary key (id));

alter table if exists department 
add constraint user_id_id_fk
foreign key (user_id) references usr;

alter table if exists department_doc 
add constraint department_id_id_fk
foreign key (department_id) references department;

alter table if exists doc_employees 
add constraint employees_key_employee_id_fk
foreign key (employees_key) references employee;

alter table if exists doc_employees 
add constraint department_doc_docid_docid_fk
foreign key (department_doc_docid) references department_doc;

alter table if exists employee 
add constraint id_department_id_fk
foreign key (id) references department;

alter table if exists user_role 
add constraint user_id_id_fk
foreign key (user_id) references usr
