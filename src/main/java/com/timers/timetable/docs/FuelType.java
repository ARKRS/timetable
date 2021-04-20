package com.timers.timetable.docs;

public enum FuelType {
    AI_92,
    AI_95,
    DIESEL;


    @Override
    public String toString() {
        switch (this){
            case AI_92: return "АИ-92";
            case AI_95: return "АИ-95";
            case DIESEL: return "Диз.";
            default: return "";
        }
    }
    public static FuelType fromString(String str){
        switch (str){
            case "АИ-92": return AI_92;
            case "АИ-95": return AI_95;
            case "Диз.": return DIESEL;
            default: return null;
        }
    }


}

