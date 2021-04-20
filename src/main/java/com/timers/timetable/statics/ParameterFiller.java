package com.timers.timetable.statics;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.deptsmanagement.DeptWrapper;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.DeptService;
import com.timers.timetable.service.UserService;
import com.timers.timetable.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParameterFiller {



    public static void  fillModelParameters(Model model, UserService userService, DeptsRepo deptsRepo, DeptService deptService) {
        //TODO переделать это! Нужно department при авторизации получить
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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

        model.addAttribute("userAuthorized", curUser != null);
        if(curUser==null)
            return;

        model.addAttribute("isAdmin", curUser != null && curUser.isAdmin());


        Department department = deptsRepo.findTop1BySupervisor(curUser);

        //ArrayList<String> deptlist = new ArrayList<>();
        ArrayList<DeptWrapper> deptlist = new ArrayList<>();

        if (department == null){
            model.addAttribute("department", null);
            model.addAttribute("departmentextcode",null);
        }
        else {
            model.addAttribute("department", department.getDeptname());
            model.addAttribute("departmentextcode",department.getExtCode());
            deptlist.add(new DeptWrapper(department,false,0));
        }

        model.addAttribute("today", new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));
        model.addAttribute("startMonth", new Date());


        if (model.getAttribute("isAdmin")== Boolean.TRUE){

            List<DeptWrapper> deptList = deptService.getDeptsHierarchy();
            model.addAttribute("deptlist",deptList);

        }
        else
            model.addAttribute("deptlist",deptlist);


    }
}
