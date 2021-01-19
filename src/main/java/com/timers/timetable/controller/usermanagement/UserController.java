package com.timers.timetable.controller.usermanagement;

import com.timers.timetable.service.UserService;
import com.timers.timetable.users.Role;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model){


        List<User> users = (List<User>) (userService.findAll());
        model.addAttribute("users",users);


        return "/userList";

    }
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){

        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/adduser")
    public String addUser(@RequestParam String username,
                          @RequestParam Map<String,String> form, Map<String,Object> model){

        User newuser = new User();
        newuser.setRoles(new HashSet<>());

        return userService.updateUser(username,newuser,form, model);
    }

    @PostMapping("saveuser")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user,
            Map<String ,Object> model){

        return userService.updateUser(username,user,form,model);
    }
}
