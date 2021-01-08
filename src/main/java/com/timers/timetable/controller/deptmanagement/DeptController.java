package com.timers.timetable.controller.deptmanagement;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(value = "/add")
    public String addDept(@RequestParam String deptname,
                          @RequestParam ("selectedSupervisor") User user
    ){

        Department department = deptsRepo.findByDeptname(deptname);
        if (department==null)
            department = new Department();

        department.setDeptname(deptname);
        department.setSupervisor(user);

        deptsRepo.save(department);

        return "deptlist";
    }
}
