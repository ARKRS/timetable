package com.timers.timetable.deptsmanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timers.timetable.users.User;

import javax.persistence.*;

@Entity
public class Department implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String deptname;
    private Boolean isActive;
    private String extCode;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "parent")
    private Department parent;

    public Department() {

    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Object o) {

        if (o instanceof Department) {
            boolean depthmore = getDepth(this,0)>getDepth((Department) o,0);
            return Boolean.compare(false,depthmore);
        }
        else {
            return 1;
        }
  }

    public static class DeptSingleton extends Department{

        private Department department;

        public Department DeptSingleton(){

            if (this.department==null){
                this.department = new Department();
            }

            return department;
        }
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptname='" + deptname + '\'' +
                '}';
    }

    public static int getDepth(Department department,int depth){
        //int depth = 0;
        if(department.getParent()==null)
            return depth;
        else {
            getDepth(department.getParent(),depth++);
        }
        return depth;
    }


}
