create table if not exists fuel_doc
(
    fuel_doc_id   bigint not null,
    doc_uploaded  boolean,
    doc_uuid      varchar(255),
    workmonth     timestamp,
    department_id bigint not null,
    constraint fuel_doc_pkey
        primary key (fuel_doc_id),
    constraint fueldoc_department_id_fk
        foreign key (department_id) references department
);

create table if not exists fuel_doc_details
(
    fuel_doc_id          bigint    not null,
    amount               integer   not null,
    date_of_month        timestamp not null,
    employee_id          bigint    not null,
    fuel_doc_fuel_doc_id bigint,
    constraint fd_detail_emp_id
        foreign key (employee_id) references employee,
    constraint fueldoc_id_fk
        foreign key (fuel_doc_fuel_doc_id) references fuel_doc,
    constraint fk75fg72jalimmwx0ngej1fyvrw
        foreign key (fuel_doc_id) references fuel_doc
);

