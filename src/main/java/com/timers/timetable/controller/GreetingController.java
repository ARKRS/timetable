package com.timers.timetable.controller;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.statics.ParameterFiller;
import com.timers.timetable.users.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class GreetingController {
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private UserRepo userRepo;

    private Model fillModelParameters(Model model) throws NotFoundException {
        //TODO переделать это! Нужно department при авторизации получить
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        if (auth==null) {
            model.addAttribute("userAuthorized",false);
            return model;
        }
            //throw new NotFoundException("Not authority");
        Object obj = auth.getPrincipal();

        String username = "";

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User curUser = userRepo.findByUsername(username);

        model.addAttribute("userAuthorized",curUser!=null);

        Department department = deptsRepo.findBySupervisor(curUser);

        if(department == null)
            model.addAttribute("department",username);
        else

            model.addAttribute("department",department.getDeptname());

        return model;
    }
    @GetMapping()
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws NotFoundException {

        model.addAttribute("name", name);

        model.addAttribute("today",new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));

        model = fillModelParameters(model);

        if (model.getAttribute("userAuthorized").equals(false)){
            return "redirect:/login";
        }

        return "/hello";
    }
    @GetMapping("/gotohello")
    public String gotohello(Model model){

        model = ParameterFiller.fillModelParameters(model,userRepo,deptsRepo);
        return "/hello";
    }

//    @GetMapping("/")
//    public String hello(Model model) throws NotFoundException {
//        model = fillModelParameters(model);
//        return "/hello";
//    }

}