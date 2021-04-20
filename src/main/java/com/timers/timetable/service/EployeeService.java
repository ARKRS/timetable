package com.timers.timetable.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeAdapter;
import com.timers.timetable.repos.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EployeeService {

    public String getEmployeesWithCarData(EmployeeRepo employeeRepo) {

        GsonBuilder gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Employee.class, new EmployeeAdapter());

        Gson gson = gsonBuilder
                .create();

        List<Employee> result = new ArrayList<>();


        List<Employee> docList = employeeRepo.findAllByCarModelIsNotNullAndCarNumberIsNotNull();

        if (docList.size() > 0) {

        }

        for (Employee doc : docList
        ) {

            result.add(doc);
        }

        return gson.toJson(result);
    }


}
