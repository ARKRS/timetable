package com.timers.timetable.controller;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.deptsmanagement.DeptWrapper;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.DeptService;
import com.timers.timetable.service.UserService;
import com.timers.timetable.statics.ParameterFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GreetingController {
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;


    @GetMapping()
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);

        model.addAttribute("today", new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));

        ParameterFiller.fillModelParameters(model,userService,deptsRepo,deptService);

        if (model.getAttribute("userAuthorized").equals(false)) {
            return "redirect:/login";
        }

        return "hello";
    }

    @GetMapping("/gotohello")
    public String gotohello(Model model) {

        ParameterFiller.fillModelParameters(model, userService, deptsRepo, deptService);

        return "hello";
    }

    @PostMapping("/gotohello")
    public String postgoString(Model model, @RequestParam Map<String,String> form) throws ParseException {

        ParameterFiller.fillModelParameters(model, userService, deptsRepo,deptService);

        if (model.getAttribute("isAdmin") == Boolean.TRUE){
            Department dept_ = deptsRepo.findById(Long.parseLong(form.get("department_id"))).get();
            if(dept_!=null) {
                model.addAttribute("department", dept_.getDeptname());
                model.addAttribute("departmentextcode",  dept_.getExtCode());
            }
            else {
                model.addAttribute("department", null);
                model.addAttribute("departmentextcode",  null);
            }
            if (form.get("start_month")!=null)
                model.addAttribute("startMonth", new SimpleDateFormat("yyyy-MM-dd").parse(form.get("start_month")));
        }

        return "hello";

    }


}