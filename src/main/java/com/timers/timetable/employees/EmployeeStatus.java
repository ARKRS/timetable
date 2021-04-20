package com.timers.timetable.employees;

public enum EmployeeStatus {

    WORK,           //Работает
    ABSENT,         //Не работает (без уважительной причины)
    PART_ABSENT,    //Отпрашивающиеся сотрудники в течение рабочего дня
    UNDEF,          //не известно
    DAYOFF,         //выходной
    VACATION,       //отпуск
    LEAVEWITHOUTPAY,//отпуск без содержания
    LATE,           //опоздание
    BUISNESSTRIP,   //командировка
    EMPLOYEESEARCH, //поиск сотрудника
    REMOTE,         //На удаленке
    SICKLEAVE,      //Больничный лист
    DISMISSED;      //Уволен



    public static EmployeeStatus fromRussian(String s) {

        switch (s) {
            case "Работает":                    return WORK;
            case "Неизвестно":                  return UNDEF;
            case "Отсутствие (без ув.причины)": return ABSENT;
            case "Отпросился в течение дня":    return PART_ABSENT;
            case "Выходной":                    return DAYOFF;
            case "Отпуск":                      return VACATION;
            case "Отпуск без содержания":       return LEAVEWITHOUTPAY;
            case "Опоздание на работу":         return LATE;
            case "Командировка":                return BUISNESSTRIP;
            case "Поиск сотрудника":            return EMPLOYEESEARCH;
            case "Работа на 'Удаленке'":        return REMOTE;
            case "Больничный":                  return SICKLEAVE;
            case "Уволен":                      return DISMISSED;
            default:                            return UNDEF;
        }
    }

    @Override
    public String toString() {

        switch (this) {
            case WORK:              return "Работает";
            case UNDEF:             return "Неизвестно";
            case ABSENT:            return "Отсутствие (без ув.причины)";
            case PART_ABSENT:       return "Отпросился в течение дня";
            case DAYOFF:            return "Выходной";
            case VACATION:          return "Отпуск";
            case LEAVEWITHOUTPAY:   return "Отпуск без содержания";
            case LATE:              return "Опоздание на работу";
            case BUISNESSTRIP:      return  "Командировка";
            case EMPLOYEESEARCH:    return "Поиск сотрудника";
            case REMOTE:            return "Работа на 'Удаленке'";
            case SICKLEAVE:         return "Больничный";
            case DISMISSED:         return "Уволен";
            default:                return "UNDEFINED";
        }
    }

    public String toShortString(){

        switch (this) {
            case WORK:              return "Р";
            case UNDEF:             return "Н/Д";
            case ABSENT:            return "ОТС";
            case PART_ABSENT:       return "ОТПРОС.";
            case DAYOFF:            return "ВЫХ";
            case VACATION:          return "ОТПУСК";
            case LEAVEWITHOUTPAY:   return "Б/С";
            case LATE:              return "ОПОЗД";
            case BUISNESSTRIP:      return "КОМ-КА";
            case EMPLOYEESEARCH:    return "ПОИСК.СОТР";
            case REMOTE:            return "УД-КА";
            case SICKLEAVE:         return "БОЛЬН";
            case DISMISSED:         return "Уволен";
            default:                return "?";
        }
    }

    public String getColor(){
        String result;

        switch (this) {
            case WORK:
                result = "799b4f";
                break;
            case UNDEF:
                result = "ffffff";
                break;
            case ABSENT:
                result = "fdb0b0";
                break;
            case PART_ABSENT:
                result = "baddf6";
                break;
            case DAYOFF:
                result = "FF0000";
                break;
            case DISMISSED:
                result = "FF0000";
                break;
            case VACATION:
                result = "E2E91B";
                break;
            case LEAVEWITHOUTPAY:
                result = "E2E91B";
                break;
            case LATE:
                result = "baddf6";
                break;
            case BUISNESSTRIP:
                result = "799b4f";
                break;
            case EMPLOYEESEARCH:
                result ="799b4f";
                break;
            case REMOTE:
                result = "799b4f";
                break;
            case SICKLEAVE:
                result = "baddf6";
                break;
            default:
                result = "ffffff";
        }

        return result;

    }


}
