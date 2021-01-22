package com.timers.timetable.controller.timetablemanagement;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeStatus;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.repos.DocsRepo;
import com.timers.timetable.repos.EmployeeRepo;
import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.service.DocService;
import com.timers.timetable.users.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/docs")
public class DocController {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private DocsRepo docsRepo;
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String createDoc(Model model) throws NotFoundException, ParseException {

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        if (auth==null)
            throw new NotFoundException("Not authority");
        Object obj = auth.getPrincipal();

        String username = "";

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User curUser = userRepo.findByUsernameAndActive(username,true);

        //Date today = Date.from(LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()).atStartOfDay().toInstant(ZoneOffset.UTC));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy");
        Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));

        Department department = deptsRepo.findBySupervisor(curUser);

        if(department==null){

            return "redirect:/gotohello";
        }

        List<Employee> employees = employeeRepo.findEmployeesByDepartment_IdOrderByFio(department.getId());

        DepartmentDoc curDoc = docsRepo.findByDepartmentAndWorkdate(department,today);

        List<Map<Employee, EmployeeStatus>> mapList = new ArrayList<Map<Employee,EmployeeStatus>>();
        for ( Employee emp: employees
        ) {
            Map<Employee,EmployeeStatus> map1 = new HashMap<Employee,EmployeeStatus>();
            if (curDoc!=null){
                EmployeeStatus curStatus = curDoc.getEmployees().get(emp);
                if (curStatus!=null){

                    map1.put(emp,curStatus);
                }
                else {
                    map1.put(emp,EmployeeStatus.UNDEF);
                }

            }
            else {
                map1.put(emp,EmployeeStatus.UNDEF);
            }


            mapList.add(map1);
        }

        model.addAttribute("department_id",department.getId());
        model.addAttribute("dapartment_name",department.getDeptname());

        model.addAttribute("today",new SimpleDateFormat("dd.MM.yyy").format(today));
        model.addAttribute("username",curUser);
        model.addAttribute("employees",mapList);

        return "doceditor";
    }


    @PostMapping("/savedoc")
    public String saveDoc(@RequestParam Map<String,String> form) {

        Optional departmentOptional = deptsRepo.findById(Long.parseLong(form.get("department_id")));

        Date workdate;

        try {
            workdate = new SimpleDateFormat("dd.MM.yyy").parse(form.get("today"));
        }catch (ParseException e){
            return "hello";
        }

        Department department = (Department) departmentOptional.get();
        DepartmentDoc curDoc = docsRepo.findByDepartmentAndWorkdate((Department) departmentOptional.get(),workdate);

        if (curDoc==null){
            curDoc = new DepartmentDoc();
            curDoc.setDocUploaded(false);
            curDoc.setDepartment(department);
            curDoc.setWorkdate(workdate);
            curDoc.setDoc_UUID(UUID.randomUUID().toString());
        }
        else {
            if (curDoc.getDoc_UUID()==null){
                curDoc.setDoc_UUID(UUID.randomUUID().toString());
            }
        }

        Map<Employee,EmployeeStatus> employeeStatusMap = new HashMap<>();

        for (Map.Entry<String,String> val: form.entrySet()
             ) {

            if (val.getKey().contains("employeeStatus")){

                Long emp_id = Long.parseLong(val.getKey().replace("employeeStatus",""));

                Optional emp_optional = employeeRepo.findById(emp_id);

                EmployeeStatus employeeStatus = EmployeeStatus.fromRussian(val.getValue());
                employeeStatusMap.put((Employee) emp_optional.get(),employeeStatus);
            }
        }
        curDoc.setEmployees(employeeStatusMap);
        docsRepo.save(curDoc);

        //model = ParameterFiller.fillModelParameters(model,userRepo,deptsRepo);

        return "redirect:/gotohello";
    }
}
