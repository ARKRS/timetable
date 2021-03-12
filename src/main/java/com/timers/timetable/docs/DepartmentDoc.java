package com.timers.timetable.docs;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
public class DepartmentDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long docid;

    //@ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
    private Date workdate;
    private Boolean docUploaded;
    private String doc_UUID;


    @ElementCollection
    @CollectionTable(name = "doc_employees")
    @MapKeyColumn(name = "employee_col")
    @Column(name = "status_col")
    private Map<Employee, EmployeeStatus> employees;

    @ElementCollection
    @CollectionTable(name = "doc_employees_new")
    @MapKeyColumn(name = "employee_col")
    private Map<Employee, AbsentPeriod> absentPeriods;

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

    public String getDoc_UUID() {
        return doc_UUID;
    }

    public void setDoc_UUID(String doc_UUID) {
        this.doc_UUID = doc_UUID;
    }

    public Map<Employee, AbsentPeriod> getAbsentPeriods() {
        return absentPeriods;
    }

    public void setAbsentPeriods(Map<Employee, AbsentPeriod> absentPeriods) {
        this.absentPeriods = absentPeriods;
    }

    @Embeddable
    public static class AbsentPeriod {


        protected int beginhour;
        protected int endhour;
        protected int beginminutes;
        protected int endminutes;
        @Column(name = "tz_status")
        protected EmployeeStatus employeeStatus;

        public AbsentPeriod() {
        }

        public AbsentPeriod(int beginhour, int endhour, int beginminutes, int endminutes, EmployeeStatus employeeStatus) {
            this.beginhour = beginhour;
            this.endhour = endhour;
            this.beginminutes = beginminutes;
            this.endminutes = endminutes;
            this.employeeStatus = employeeStatus;
        }

        public int getBeginhour() {
            return beginhour;
        }

        public void setBeginhour(int beginhour) {
            this.beginhour = beginhour;
        }

        public int getEndhour() {
            return endhour;
        }

        public void setEndhour(int endhour) {
            this.endhour = endhour;
        }

        public int getBeginminutes() {
            return beginminutes;
        }

        public void setBeginminutes(int beginminutes) {
            this.beginminutes = beginminutes;
        }

        public int getEndminutes() {
            return endminutes;
        }

        public void setEndminutes(int endminutes) {
            this.endminutes = endminutes;
        }

        public EmployeeStatus getEmployeeStatus() {
            return employeeStatus;
        }

        public void setEmployeeStatus(EmployeeStatus employeeStatus) {
            this.employeeStatus = employeeStatus;
        }
    }


}


