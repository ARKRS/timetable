package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DocsRepo extends CrudRepository<DepartmentDoc,Long> {


    DepartmentDoc findByDepartmentAndWorkdate(Department department, Date workDate);

    List<DepartmentDoc> findAllByDocUploaded(Boolean documentUploaded);

}
