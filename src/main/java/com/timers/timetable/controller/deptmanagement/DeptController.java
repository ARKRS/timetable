package com.timers.timetable.controller.deptmanagement;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.repos.DeptsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptsRepo deptsRepo;

    @GetMapping
    public String deptsList(Model model){

        model.addAttribute("depts",deptsRepo.findAll());
        return "deptlist";
    }

    @PostMapping(value = "add")
    public String addDept(@RequestParam String deptname){

        Department department = deptsRepo.findByDeptname(deptname);
        if (department==null)
            return "deptlist";

        department = new Department();
        department.setDeptname(deptname);

        deptsRepo.save(department);

        return "deptlist";
    }
}
