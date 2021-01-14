package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.employees.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepo extends CrudRepository<Employee,Long> {

    Employee findByFio(String fio);

    List<Employee> findEmployeesByDepartment_IdOrderByFio(Long dept_id);


    List<Employee> findAllByDepartment(Department department);

    Employee findByExtCodeAndFio(String extCode, String fio);
}
