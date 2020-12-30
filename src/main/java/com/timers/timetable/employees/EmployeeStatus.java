package com.timers.timetable.employees;

public enum EmployeeStatus {

    WORK,ABSENT,UNDEF;


    @Override
    public String toString() {

        String result;

        switch (this){
            case WORK: result = "Работает";
                break;
            case UNDEF: result = "Неизвестно";
                break;
            case ABSENT: result = "Отсутствует";
                break;
            default:
                result = "UNDEFINED";
        }

        return result;

    }

    public static EmployeeStatus fromRussian(String s){

        EmployeeStatus status;

        switch (s){
            case "Работает" : status = WORK;
            break;
            case "Неизвестно" : status = UNDEF;
            break;
            case "Отсутствует" : status = ABSENT;
            break;
            default: status = UNDEF;
        }
        return status;
    }

}
