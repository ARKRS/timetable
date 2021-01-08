package com.timers.timetable.docs;

import com.google.gson.*;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeStatus;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DepartmentDocAdapter implements JsonSerializer<DepartmentDoc> {


    @Override
    public JsonElement serialize(DepartmentDoc departmentDoc, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result = new JsonObject();

        result.addProperty("workdate", new SimpleDateFormat("dd.mm.yyy").format(departmentDoc.getWorkdate()));
        result.addProperty("department", departmentDoc.getDepartment().getDeptname());

        JsonArray employeesjs = new JsonArray();

        Map <Employee, EmployeeStatus> employees =  departmentDoc.getEmployees();

        for (Map.Entry<Employee,EmployeeStatus> iter: employees.entrySet()
             ) {
            JsonObject jsonElement = new JsonObject();
            jsonElement.addProperty("fio",iter.getKey().getFio());
            jsonElement.addProperty("extcode", iter.getKey().getExtCode());
            jsonElement.addProperty("status",iter.getValue().toString());

            employeesjs.add(jsonElement);
        }
        result.add("employees", employeesjs);

        return result;
    }
}
