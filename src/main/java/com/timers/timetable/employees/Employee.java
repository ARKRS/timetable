package com.timers.timetable.employees;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.timers.timetable.deptsmanagement.Department;

import javax.persistence.*;

@Entity
@JsonAutoDetect
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employee_id;


    private String fio;

    private String extCode;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "department_id")
    @ManyToOne(fetch = FetchType.EAGER)
    //TODO переименовать колонку в department_id
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonIgnore
    private Department department;


    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }



}
