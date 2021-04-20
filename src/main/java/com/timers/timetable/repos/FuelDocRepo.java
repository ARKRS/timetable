package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
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

//    FuelDoc findByDoc_UUID(String doc_UUID);

    @Query(value = "SELECT doc.* from fuel_doc as doc where doc.doc_UUID = :doc_UUID", nativeQuery = true)
    List<FuelDoc> findByDoc_UUID(@Param("doc_UUID") String doc_UUID);


}
