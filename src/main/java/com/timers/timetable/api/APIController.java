package com.timers.timetable.api;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.repos.DocsRepo;
import com.timers.timetable.repos.EmployeeRepo;
import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;

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
    private EmployeeRepo employeeRepo;

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
                Employee employee = employeeRepo.findByExtCodeAndFio(emp.extcode, emp.fio);
                Department department = deptsRepo.findByExtCodeAndDeptname(dep.extcode, dep.deptname);
                employee.setDepartment(department);
                employeeRepo.save(employee);
            } catch (Exception e) {

                e.printStackTrace();
                return "jsonerror";
            }

        }
        return "jsonok";
    }

    @RequestMapping(value = "/loadconfirms", method = RequestMethod.POST, consumes = "application/json")
    public String loadConfirmations(@RequestParam String username, @RequestParam String password, @RequestBody String body) {
        if (userRepo.findByUsernameAndActive(username, true) == null) {
            return "jsonerror";
        }
        Gson gson = new Gson();
        ArrayList<LinkedTreeMap<String,String>> list = gson.fromJson(body, (Type) List.class);
        Iterator<LinkedTreeMap<String,String>> it = list.iterator();
        while (it.hasNext()){
            LinkedTreeMap<String,String> id = it.next();
            Long doc_id = docsRepo.findByDoc_UUID(id.get("doc_UUID"));
            if(doc_id!=null) {
                Optional<DepartmentDoc> doc = docsRepo.findById(doc_id);
                if (doc != null) {
                    DepartmentDoc  doc1= doc.get();
                    doc1.setDocUploaded(true);
                    docsRepo.save(doc1);
                }
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


        }
        return "jsonok";
    }

    private class Emptmpl {
        private String fio;
        private String extcode;
    }

    private class Deptmpl {
        private String deptname;
        private String extcode;
        private String parentextcode;

    }

}
