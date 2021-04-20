function checkStatusAndDisableTime(element) {
    // debugger;
    var index = element.name.substr(element.name.length - 4);

    var el_disabled = (element.value != "Отпросился в течение дня" && element.value != "Работа на 'Удаленке'" && element.value != "Опоздание на работу");

    document.getElementById("beginhour_" + index).disabled = el_disabled;
    document.getElementById("beginminutes_" + index).disabled = el_disabled;
    document.getElementById("endhour_" + index).disabled = el_disabled;
    document.getElementById("endminutes_" + index).disabled = el_disabled;

    if (el_disabled = true) {
        document.getElementById("beginhour_" + index).value = 0;
        document.getElementById("beginminutes_" + index).value = 0;
        document.getElementById("endhour_" + index).value = 0;
        document.getElementById("endminutes_" + index).value = 0;
    }


    /*alert(element);*/
}

function everyBodyAtWork(element){

    var elArray = [];
    var tmp = document.getElementsByTagName("select");

    /*var regex = new RegExp(/employeeStatus[0-9][0-9][0-9][0-9]/gm);*/
    for ( var i = 0; i < tmp.length; i++ ) {
        if ( (/employeeStatus\d\d\d\d/).test(tmp[i].name) ) {
            elArray.push(tmp[i]);
        }
    }

    for ( var i =0; i < elArray.length; i++){
        var select = elArray[i].value = 'Работает';
        checkStatusAndDisableTime(elArray[i]);
    }


}
