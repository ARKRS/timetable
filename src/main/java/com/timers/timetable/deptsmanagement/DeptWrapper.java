package com.timers.timetable.deptsmanagement;

public class DeptWrapper {

        private Department department;
        private boolean isGroup;
        private int level;

        public DeptWrapper(Department department, boolean isGroup,int level){
            this.department = department;
            this.isGroup = isGroup;
            this.level = level;
        }

    public Department getDepartment() {
        return department;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return department.getDeptname() + " " + isGroup + " " + level;
    }
}
