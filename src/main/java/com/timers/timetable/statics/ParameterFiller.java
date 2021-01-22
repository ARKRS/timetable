package com.timers.timetable.statics;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.UserService;
import com.timers.timetable.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ParameterFiller {

    public static void fillModelParameters(Model model, UserService userService, DeptsRepo deptsRepo) {
        //TODO переделать это! Нужно department при авторизации получить
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
//        if (auth==null)
//            throw new NotFoundException("Not authority");
        Object obj = auth.getPrincipal();

        String username = "";

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User curUser = userService.getUserByUsername(username);

        model.addAttribute("userAuthorized",curUser!=null);

        model.addAttribute("isAdmin",curUser!=null ? curUser.isAdmin() : false);

        Department department = deptsRepo.findBySupervisor(curUser);

        if(department == null)
            model.addAttribute("department",null);
        else

            model.addAttribute("department",department.getDeptname());

        model.addAttribute("today",new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));

    }
}
