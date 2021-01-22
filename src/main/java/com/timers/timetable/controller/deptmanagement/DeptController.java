package com.timers.timetable.controller.deptmanagement;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.users.Role;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String deptsList(Model model){

        model.addAttribute("depts",deptsRepo.findAll());
        model.addAttribute("users",userRepo.findAll());

        return "deptlist";
    }
    @GetMapping("{dept}")
    public String userEditForm(@PathVariable Department dept, Model model){

        model.addAttribute("dept",dept);
        model.addAttribute("users",userRepo.findAll());
        return "deptEdit";
    }
    @PostMapping(value = "/add")
    public String addDept(@RequestParam String deptname,
                          @RequestParam String selectedSupervisor,//("selectedSupervisor") User user,
                          Model model
    ){

        Department department = deptsRepo.findByDeptname(deptname);
        if (department==null)
            department = new Department();
        if (selectedSupervisor.equals("null"))
            {
                department.setSupervisor(null);
            }
        else
            {
                Optional optuser = userRepo.findById(Long.parseLong(selectedSupervisor));
                User supervisor = (User) optuser.get();
                if (supervisor!=null){
                    department.setSupervisor(supervisor);
                }
              // department.setSupervisor((User) optuser);
            }

        department.setDeptname(deptname);
        //department.setSupervisor(user);

        deptsRepo.save(department);

        return "redirect:/depts";
    }
}
