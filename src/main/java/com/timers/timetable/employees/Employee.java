package com.timers.timetable.employees;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "carmodel")
    private String carModel;

    @Column(name = "carnumber")
    private String carNumber;

    @Column(name = "carconsumption")
    private Integer carConsumption;

    @ManyToOne(fetch = FetchType.EAGER)
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

    public String getCarModel() {        return carModel;    }

    public void setCarModel(String carModel) {        this.carModel = carModel;    }

    public String getCarNumber() {        return carNumber;    }

    public void setCarNumber(String carNumber) {        this.carNumber = carNumber;    }

    public Integer getCarConsumption() {        return carConsumption;    }

    public void setCarConsumption(Integer carConsumption) {        this.carConsumption = carConsumption;    }
}
