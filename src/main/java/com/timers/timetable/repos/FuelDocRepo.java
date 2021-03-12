package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.docs.FuelDoc;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FuelDocRepo extends CrudRepository<FuelDoc,Long> {

    FuelDoc findByDepartmentAndWorkmonth(Department department, Date workDate);

    List<FuelDoc> findAllByDocUploaded(Boolean documentUploaded);

    List<FuelDoc> findAllByWorkmonthBetweenAndDepartmentEquals(Date startDate,Date endDate,Department department);

    @Query("SELECT docid from DepartmentDoc where doc_UUID = :doc_UUID")
    Long findByDoc_UUID(@Param("doc_UUID") String doc_UUID);

}
