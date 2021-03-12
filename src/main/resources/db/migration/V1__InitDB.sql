create sequence
    hibernate_sequence
    start 1 increment 1;

create table department
    (id int8 not null,
    deptname varchar(255),
    ext_code varchar(255),
    is_active boolean,
    user_id int8,
    primary key (id)
    );

create table department_doc (
    docid int8 not null,
    doc_uploaded boolean,
    doc_uuid varchar(255),
    workdate timestamp,
    department_id int8,
    primary key (docid)
                            );

create table doc_employees (
    department_doc_docid int8 not null,
    status_col int4,
    employees_key int8 not null,
    primary key (department_doc_docid, employees_key)
                           );

create table doc_employees_new (
    department_doc_docid int8 not null,
    beginhour int4 not null,
    beginminutes int4 not null,
    tz_status int4,
    endhour int4 not null,
    endminutes int4 not null,
    absent_periods_key int8 not null,
    primary key (department_doc_docid, absent_periods_key)
                               );

create table employee (
    employee_id int8 not null,
    ext_code varchar(255),
    fio varchar(255),
    id int8,
    primary key (employee_id)
                      );

create table user_role (
    user_id int8 not null,
    roles varchar(255)
                       );
create table usr (
    id int8 not null,
    activation_code varchar(255),
    active boolean not null,
    email varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
                 );

alter table if exists
    department add constraint
        FKnfmwrwn82fbgvpkrp4d0ucf3x
        foreign key (user_id) references usr;

alter table if exists
    department_doc add constraint
    FKijcc4gueupoqimflk9nk9e192 foreign key
    (department_id) references department;

alter table if exists
    doc_employees add constraint
    FK3vdxj0r8c852c8b9xkyt5i4f6 foreign key
    (employees_key) references employee;

alter table if exists
    doc_employees add constraint
        FKsyyqliyi4lwplnmveakvhsrq6 foreign key
            (department_doc_docid) references department_doc;

alter table if exists
    doc_employees_new add constraint
        FKmfbt1fx4tme0hrfqwi2wjor2s foreign key
            (absent_periods_key) references employee;

alter table if exists
    doc_employees_new add constraint
        FKhfpr6a1ndu8tighgyo5j85clj foreign key
            (department_doc_docid) references department_doc;

alter table if exists
    employee add constraint
        FK6imirm13gp5u48ewsmuawj9u foreign key
            (id) references department;

alter table if exists
    user_role add constraint
        FKfpm8swft53ulq2hl11yplpr5 foreign key
            (user_id) references usr;