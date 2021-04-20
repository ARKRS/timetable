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

        //car data 12.03.2021
        jsonWriter.name("carmodel");
        jsonWriter.value(employee.getCarModel());
        jsonWriter.name("carnumber");
        jsonWriter.value(employee.getCarNumber());
        jsonWriter.name("carconsumption");
        jsonWriter.value(employee.getCarConsumption());
        jsonWriter.endObject();
    }

    @Override
    public Employee read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
