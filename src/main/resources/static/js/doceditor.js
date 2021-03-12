function checkStatusAndDisableTime(element) {
    // debugger;
    var index = element.name.substr(element.name.length - 4);

    var el_disabled = (element.value != "Отпросился");

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
/*for fuel table*/
function recalculateStringTotals(){
    var empcode     = "";
    var total = 0;
    var maxdates = document.getElementById("maxdates").value;
    for (var i = 2; i <document.getElementById("fuel_table").rows.length; i++){
        var collection = document.getElementById("fuel_table").rows[i].children;
        for (var a = 0; a<collection.length;a++){
            if((/employeecode*/).test(collection.item(a).id)){
                empcode = collection.item(a).id.substr(collection.item(a).id.lastIndexOf("_")+1);
                break;
            }
        }
        total = 0;
        for (var j = 1; j<maxdates;j++){
            var elem = document.getElementById("amount_"+ ("000" + j).slice(-2) + "__" + empcode);
            if (elem!=null){
                var parsed = parseInt(elem.value);
                if (!isNaN(parsed)){
                    total = total + parseInt(elem.value);
                }
            }
        }
        document.getElementById("total_"+empcode).value = total;
        if (total!=0){
            document.getElementById("car_model_"+empcode).setAttribute("required",true);
            document.getElementById("car_number_"+empcode).setAttribute("required",true);
            document.getElementById("car_consumption_"+empcode).setAttribute("required",true);
        }
        else {
            document.getElementById("car_model_"+empcode).removeAttribute("required");
            document.getElementById("car_number_"+empcode).removeAttribute("required");
            document.getElementById("car_consumption_"+empcode).removeAttribute("required");
        }
    }
}