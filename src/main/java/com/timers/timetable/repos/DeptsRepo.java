package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.users.User;
import org.springframework.data.repository.CrudRepository;

public interface DeptsRepo extends CrudRepository<Department,Long> {

    Department findByDeptname(String deptname);

    Department findBySupervisor(User supervisor);

}
