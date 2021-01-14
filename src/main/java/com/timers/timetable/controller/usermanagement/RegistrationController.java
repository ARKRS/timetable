package com.timers.timetable.controller.usermanagement;

import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.UserService;
import com.timers.timetable.statics.ParameterFiller;
import com.timers.timetable.users.User;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.TemplateEngine;

import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptsRepo deptsRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String,Object> model){

        if(!userService.addUser(user)){
            model.put("errorUserExists","User exists");
            return "registration";
        }

        if(!user.getEmail().equals("")){
          model.put("message","На адрес " + user.getEmail() + " была отправлена \n"+
                  "ссылка для активации учетной записи");
        }

        return "login";
    }
    @GetMapping("/hello")
    public String hello(Model model){

        ParameterFiller.fillModelParameters(model,userService,deptsRepo);
        return "/hello";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){

        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Учетная запись успешно активирована!");
        }
        else {
            model.addAttribute("message", "Указанный код активации не найден!");
        }


        return "login";
    }

    @GetMapping("/login")
    public String login(Model model){

        //model.addAttribute("message","message to login");
        return "login";
    }

}
