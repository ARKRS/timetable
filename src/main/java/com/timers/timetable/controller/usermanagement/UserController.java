package com.timers.timetable.controller.usermanagement;

import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.service.UserService;
import com.timers.timetable.users.Role;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model){


        List<User> users = (List<User>) (userRepo.findAll());
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
                          @RequestParam Map<String,String> form){

        User newuser = new User();
        newuser.setRoles(new HashSet<>());

        return updateUser(username,newuser,form);
    }

   private String updateUser(String username, User user, Map<String,String> form){

       user.setUsername(username);

       Set<String> roles = Arrays.stream(Role.values())
               .map(Role::name)
               .collect(Collectors.toSet());

       user.getRoles().clear();

       user.setActive(form.get("active")==null? false:true);


       for (String key : form.keySet()) {

           if(roles.contains(key)){
               user.getRoles().add(Role.valueOf(key));
           }
       }

       userRepo.save(user);

       return "redirect:/user";

   }
    @PostMapping("saveuser")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") User user){

        return updateUser(username,user,form);
    }
}
