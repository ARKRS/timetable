package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DocsRepo extends CrudRepository<DepartmentDoc, Long> {


    DepartmentDoc findByDepartmentAndWorkdate(Department department, Date workDate);

    List<DepartmentDoc> findAllByDocUploaded(Boolean documentUploaded);

    List<DepartmentDoc> findAllByWorkdateBetweenAndDepartmentEquals(Date startDate,Date endDate,Department department);

    List<DepartmentDoc> findAllByWorkdate(Date workdate);

 //   DepartmentDoc findByDoc_UUIDEquals(String doc_UUID);
    @Query(value = "SELECT docs.* from department_doc as docs where docs.doc_UUID = :doc_UUID",nativeQuery = true)
    List<DepartmentDoc> findByDoc_UUID(@Param("doc_UUID") String doc_UUID);

}
