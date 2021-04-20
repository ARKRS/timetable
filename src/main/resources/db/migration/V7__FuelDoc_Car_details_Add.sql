create table fuel_docs_cars_details (
    fuel_doc_id int8 not null,
    employee_id int8 not null,
    end_odometer_data int4 not null,
    fuel_doc_fuel_doc_id int8,
    start_odometer_data int4 not null);

alter table if exists
    fuel_docs_cars_details add constraint
    fd_detail_emp_id foreign key (employee_id)
    references employee;

alter table if exists
    fuel_docs_cars_details add constraint
    fuelDoc_id_fk foreign key (fuel_doc_fuel_doc_id)
    references fuel_doc;

alter table if exists
    fuel_docs_cars_details add constraint
    FK2awx69o2lcp0e2x747uo9c9t0 foreign key (fuel_doc_id)
    references fuel_doc;