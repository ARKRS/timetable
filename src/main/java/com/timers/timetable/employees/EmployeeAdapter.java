package com.timers.timetable.employees;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class EmployeeAdapter extends TypeAdapter<Employee> {
    @Override
    public void write(JsonWriter jsonWriter, Employee employee) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("employee_id");
        jsonWriter.value(employee.getEmployee_id());
        jsonWriter.name("fio");
        jsonWriter.value(employee.getFio());
        jsonWriter.name("extcode");
        jsonWriter.value(employee.getExtCode());
        jsonWriter.endObject();
    }

    @Override
    public Employee read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
