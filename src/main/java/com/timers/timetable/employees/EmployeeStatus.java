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
    REMOTE;         //На удаленке



    public static EmployeeStatus fromRussian(String s) {

        EmployeeStatus status;

        switch (s) {
            case "Работает":
                status = WORK;
                break;
            case "Неизвестно":
                status = UNDEF;
                break;
            case "Отсутствие (без ув.причины)":
                status = ABSENT;
                break;
            case "Отпросился в течение дня":
                status = PART_ABSENT;
                break;
            case "Выходной":
                status = DAYOFF;
                break;
            case "Отпуск":
                status = VACATION;
                break;
            case "Отпуск без содержания":
                status = LEAVEWITHOUTPAY;
                break;
            case "Опоздание на работу":
                status = LATE;
                break;
            case "Командировка":
                status = BUISNESSTRIP;
                break;
            case "Поиск сотрудника":
                status = EMPLOYEESEARCH;
                break;
            case "Работа на 'Удаленке'":
                status = REMOTE;
                break;
            default:
                status = UNDEF;
        }
        return status;
    }

    @Override
    public String toString() {

        String result;

        switch (this) {
            case WORK:
                result = "Работает";
                break;
            case UNDEF:
                result = "Неизвестно";
                break;
            case ABSENT:
                result = "Отсутствие (без ув.причины)";
                break;
            case PART_ABSENT:
                result = "Отпросился в течение дня";
                break;
            case DAYOFF:
                result = "Выходной";
                break;
            case VACATION:
                result = "Отпуск";
                break;
            case LEAVEWITHOUTPAY:
                result = "Отпуск без содержания";
                break;
            case LATE:
                result = "Опоздание на работу";
                break;
            case BUISNESSTRIP:
                result = "Командировка";
                break;
            case EMPLOYEESEARCH:
                result ="Поиск сотрудника";
                break;
            case REMOTE:
                result = "Работа на 'Удаленке'";
                break;
            default:
                result = "UNDEFINED";
        }

        return result;

    }

    public String toShortString(){
        String result;

        switch (this) {
            case WORK:
                result = "Р";
                break;
            case UNDEF:
                result = "Н/Д";
                break;
            case ABSENT:
                result = "ОТСУТС.У/П";
                break;
            case PART_ABSENT:
                result = "ОТПРОС.";
                break;
            case DAYOFF:
                result = "ВЫХ";
                break;
            case VACATION:
                result = "ОТПУСК";
                break;
            case LEAVEWITHOUTPAY:
                result = "Б/С";
                break;
            case LATE:
                result = "ОПОЗД";
                break;
            case BUISNESSTRIP:
                result = "КОМ-КА";
                break;
            case EMPLOYEESEARCH:
                result ="ПОИСК.СОТР";
                break;
            case REMOTE:
                result = "УДАЛЕНКА";
                break;

            default:
                result = "?";
        }

        return result;

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

            default:
                result = "ffffff";
        }

        return result;

    }


}
