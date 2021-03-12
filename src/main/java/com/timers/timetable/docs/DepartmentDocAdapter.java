package com.timers.timetable.docs;

import com.google.gson.*;
import com.timers.timetable.employees.Employee;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DepartmentDocAdapter implements JsonSerializer<DepartmentDoc> {


    @Override
    public JsonElement serialize(DepartmentDoc departmentDoc, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result = new JsonObject();

        result.addProperty("workdate", new SimpleDateFormat("dd.MM.yyy").format(departmentDoc.getWorkdate()));
        result.addProperty("department", departmentDoc.getDepartment().getDeptname());
        result.addProperty("department_id", departmentDoc.getDepartment().getExtCode().trim());
        result.addProperty("UUID", departmentDoc.getDoc_UUID());

        JsonArray employeesjs = new JsonArray();


        Map<Employee, DepartmentDoc.AbsentPeriod> employees = departmentDoc.getAbsentPeriods();//getEmployees();

        for (Map.Entry<Employee, DepartmentDoc.AbsentPeriod> iter : employees.entrySet()
        ) {
            JsonObject jsonElement = new JsonObject();
            DepartmentDoc.AbsentPeriod absentPeriod = iter.getValue();
            jsonElement.addProperty("fio", iter.getKey().getFio().trim());
            jsonElement.addProperty("extcode", iter.getKey().getExtCode().trim());
            jsonElement.addProperty("status", absentPeriod.getEmployeeStatus().toString());
            jsonElement.addProperty("beginhour", absentPeriod.getBeginhour());
            jsonElement.addProperty("beginminutes", absentPeriod.getBeginminutes());

            jsonElement.addProperty("endhour", absentPeriod.getEndhour());
            jsonElement.addProperty("endminutes", absentPeriod.getEndminutes());

            employeesjs.add(jsonElement);
        }
        result.add("employees", employeesjs);

        return result;
    }
}
