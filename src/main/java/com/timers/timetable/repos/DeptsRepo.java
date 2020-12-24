package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import org.springframework.data.repository.CrudRepository;

public interface DeptsRepo extends CrudRepository<Department,Long> {

    Department findByDeptname(String deptname);

}
