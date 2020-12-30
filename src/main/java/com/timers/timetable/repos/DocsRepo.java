package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DocumentByDay;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface DocsRepo extends CrudRepository<DocumentByDay,Long> {


    DocumentByDay findByDepartmentAndWorkdate(Department department, Date workDate);

}
