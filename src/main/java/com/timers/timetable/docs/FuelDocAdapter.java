package com.timers.timetable.docs;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

public class FuelDocAdapter implements JsonSerializer<FuelDoc> {


    @Override
    public JsonElement serialize(FuelDoc fuelDoc, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result = new JsonObject();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy");

        result.addProperty("workdate", sdf.format(fuelDoc.getWorkmonth()));
        result.addProperty("department", fuelDoc.getDepartment().getDeptname());
        result.addProperty("department_id", fuelDoc.getDepartment().getExtCode().trim());
        result.addProperty("UUID", fuelDoc.getDoc_UUID());

        JsonArray employeesjs = new JsonArray();


        List<FuelDocDetails> fuelDocDetails = fuelDoc.getFuelDocDetails();//getEmployees();

        for (FuelDocDetails  iter : fuelDocDetails
        ) {
            JsonObject jsonElement = new JsonObject();

            jsonElement.addProperty("fio", iter.getEmployee().getFio().trim());
            jsonElement.addProperty("extcode", iter.getEmployee().getExtCode().trim());
            jsonElement.addProperty("date", sdf.format(iter.getDateOfMonth()));
            jsonElement.addProperty("amount", iter.getAmount());

            employeesjs.add(jsonElement);
        }
        result.add("employees", employeesjs);

        employeesjs = new JsonArray();


        List<FuelDocCarDetails> fuelDocCarDetails = fuelDoc.getFuelDocCarDetails();//getEmployees();

        for (FuelDocCarDetails  iter : fuelDocCarDetails
        ) {
            JsonObject jsonElement = new JsonObject();

            jsonElement.addProperty("fio", iter.getEmployee().getFio().trim());
            jsonElement.addProperty("extcode", iter.getEmployee().getExtCode().trim());
            jsonElement.addProperty("startOdometerData", iter.getStartOdometerData());
            jsonElement.addProperty("endOdometerData", iter.getEndOdometerData());
            jsonElement.addProperty("tankreststart", iter.getTankreststart());
            jsonElement.addProperty("tankrestend", iter.getTankrestend());
            jsonElement.addProperty("fueltype", iter.getFuelType().toString());
            employeesjs.add(jsonElement);
        }
        result.add("cardetails", employeesjs);

        return result;
    }
}
