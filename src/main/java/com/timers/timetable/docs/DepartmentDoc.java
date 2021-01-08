package com.timers.timetable.docs;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class DepartmentDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long docid;

    //@ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id",referencedColumnName = "id")
    private Department department;
    private Date workdate;
    private Boolean docUploaded;


    @ElementCollection
    @CollectionTable(name = "doc_employees")
    @MapKeyColumn(name = "employee_col")
    @Column(name = "status_col")
    private Map<Employee, EmployeeStatus> employees;

//    public DocumentByDay(Department department, Map<Employee, String> employees) {
//        this.department = department;
//
//    }

    public DepartmentDoc() {

    }
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getWorkdate() {
        return workdate;
    }

    public void setWorkdate(Date workdate) {
        this.workdate = workdate;
    }

    public Boolean getDocUploaded() {
        return docUploaded;
    }

    public void setDocUploaded(Boolean docUploaded) {
        this.docUploaded = docUploaded;
    }

    public Map<Employee, EmployeeStatus> getEmployees() {
        return employees;
    }

    public void setEmployees(Map<Employee, EmployeeStatus> employees) {
        this.employees = employees;
    }

}
