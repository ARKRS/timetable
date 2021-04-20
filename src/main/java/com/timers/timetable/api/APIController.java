package com.timers.timetable.api;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.docs.FuelDoc;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.repos.*;
import com.timers.timetable.service.DocService;
import com.timers.timetable.service.EployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class APIController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DocsRepo docsRepo;
    @Autowired
    private DeptsRepo deptsRepo;

    @Autowired
    private FuelDocRepo fuelDocRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    @RequestMapping (value = "/getempcardata",
            produces = "application/json")
    @ResponseBody
    public String getEmployeesCarData(@RequestParam String username, @RequestParam String password){

        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "User not found";
        }

        String str = new EployeeService().getEmployeesWithCarData(employeeRepo);

        return str;
    }
    
    @RequestMapping(value = "/getdocs",
            produces = "application/json")
    @ResponseBody
    public String getDocs(@RequestParam String username, @RequestParam String password) {

        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "User not found";
        }

        String str = new DocService().getDocs(docsRepo);

        return str;
    }

    @RequestMapping(value = "/getfueldocs",
            produces = "application/json")
    @ResponseBody
    public String getFuelDocs(@RequestParam String username, @RequestParam String password) {

        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "User not found";
        }

        String str = new DocService().getFuelDocs(fuelDocRepo);

        return str;
    }

    @RequestMapping(value = "/loadempsanddepts", method = RequestMethod.POST, consumes = "application/json")
    public String putsEmployeesWithDepartments(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody String body) {
        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "jsonerror";
        }

        Gson gson = new Gson();
        Map<Emptmpl, Deptmpl> list = gson.fromJson(body, new TypeToken<Map<Emptmpl, Deptmpl>>() {
        }.getType());
        for (Map.Entry<Emptmpl, Deptmpl> element : list.entrySet()
        ) {

            try {
                Emptmpl emp = element.getKey();
                Deptmpl dep = element.getValue();
                Employee employee = employeeRepo.findByExtCode(emp.extcode);
                Department department = deptsRepo.findByExtCode(dep.extcode);
                employee.setDepartment(department);
                employeeRepo.save(employee);
            } catch (Exception e) {
                System.out.println("Ошибка обновления putsEmployeesWithDepartments на key.extcode: " + element.getKey().extcode +
                        " key.fio "+ element.getKey().fio +
                        " val.extcode: "+ element.getValue().extcode +
                        " val.deptname: "+ element.getValue().deptname);

                System.out.println(e.getLocalizedMessage());
                continue;
                //return "jsonerror";
            }

        }
        return "jsonok";
    }

    @RequestMapping(value = "/loadconfirms", method = RequestMethod.POST, consumes = "application/json")
    public String loadConfirmations(@RequestParam String username, @RequestParam String password, @RequestBody String body,@RequestParam String doctype) {
        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "jsonerror";
        }
        Gson gson = new Gson();
        ArrayList<LinkedTreeMap<String,String>> list = gson.fromJson(body, (Type) List.class);
        Iterator<LinkedTreeMap<String,String>> it = list.iterator();
        while (it.hasNext()){
            LinkedTreeMap<String,String> id = it.next();
            Long doc_id;
            if (doctype.equals("departmentdoc")) {

                List<DepartmentDoc> docs =  docsRepo.findByDoc_UUID(id.get("doc_UUID")); //findById(doc_id);
                for (DepartmentDoc doc: docs
                     ) {
                   // doc.setDocUploaded(true);

                    docsRepo.save(doc);
                }
            }
            else if (doctype.equals("fueldoc")){

                List<FuelDoc> docs =fuelDocRepo.findByDoc_UUID(id.get("doc_UUID"));//findById(doc_id);
                for (FuelDoc doc : docs
                     ) {
                   // doc.setDocUploaded(true);
                    fuelDocRepo.save(doc);
                }

            }
            else {
                doc_id = null;
            }


        }
        return "jsonok";
    }
    @RequestMapping(value = "/loademps", method = RequestMethod.POST, consumes = "application/json")
    public String putEmployees(@RequestParam String username, @RequestParam String password, @RequestBody String body) {

        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "jsonerror";
        }

        Gson gson = new Gson();
        ArrayList<Emptmpl> list = gson.fromJson(body, new TypeToken<List<Emptmpl>>() {
        }.getType());
        for (Emptmpl empel : list
        ) {

            Employee employee = employeeRepo.findByExtCodeAndFio(empel.extcode, empel.fio);
            if (employee == null) {
                employee = new Employee();
                employee.setExtCode(empel.extcode);
                employee.setFio(empel.fio);
                employee.setCarNumber(empel.carnumber);
                employee.setCarModel(empel.carmodel);
                employee.setCarConsumption(10);
                employeeRepo.save(employee);

            }
            else{
                employee.setFio(empel.fio);
                employee.setCarNumber(empel.carnumber);
                employee.setCarModel(empel.carmodel);
                employee.setCarConsumption(10);
                employeeRepo.save(employee);

            }

        }
        return "jsonok";
    }

    @RequestMapping(value = "/loaddepts", method = RequestMethod.POST, consumes = "application/json")
    public String putDepts(@RequestParam String username, @RequestParam String password, @RequestBody String body) {

        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "jsonerror";
        }

        Gson gson = new Gson();

        ArrayList<Deptmpl> list = gson.fromJson(body, new TypeToken<List<Deptmpl>>() {
        }.getType());
        for (Deptmpl deptel : list
        ) {
            Department department = deptsRepo.findByExtCodeAndDeptname(deptel.extcode, deptel.deptname);

            if (department == null) {

                department = new Department();
            }

            department.setDeptname(deptel.deptname);
            department.setExtCode(deptel.extcode);

            if (!deptel.parentextcode.equals("")){
                department.setParent(deptsRepo.findByExtCode(deptel.parentextcode));
            }
            deptsRepo.save(department);

            List<Employee> employeeList = employeeRepo.findAllByDepartment(department);
            for ( Employee emp_: employeeList
            ) {
                emp_.setDepartment(null);
                employeeRepo.save(emp_);
            }

        }
        return "jsonok";
    }

    private class Emptmpl {
        private String fio;
        private String extcode;
        private String carmodel;
        private String carnumber;
        private Integer carconsumption;

    }

    private class Deptmpl {
        private String deptname;
        private String extcode;
        private String parentextcode;

    }

}
