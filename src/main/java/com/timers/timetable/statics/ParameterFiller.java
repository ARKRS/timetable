package com.timers.timetable.statics;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDocAdapter;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.UserService;
import com.timers.timetable.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ParameterFiller {

    public static void fillModelParameters(Model model, UserService userService, DeptsRepo deptsRepo) {
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

        Department department = deptsRepo.findBySupervisor(curUser);

        //ArrayList<String> deptlist = new ArrayList<>();
        Map<String,String> deptlist = new HashMap<>();

        if (department == null){
            model.addAttribute("department", null);
            model.addAttribute("departmentextcode",null);
        }
        else {
            model.addAttribute("department", department.getDeptname());
            model.addAttribute("departmentextcode",department.getExtCode());
            deptlist.put(department.getExtCode(),department.getDeptname());
        }

        model.addAttribute("today", new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));
        model.addAttribute("startMonth", new Date());
        //RA добавляем для администратора все подразделения для просмотра табеля.
        //Для обычного пользователя добавляем только его подразделение.


        if (curUser!=null && curUser.isAdmin()){
            //Iterable<Department> depts = deptsRepo.findAll();
            List<Department> depts = deptsRepo.findAllByExtCodeIsNotNullOrderByDeptname();

            for (Department d: depts
                 ) {
                if (!deptlist.containsKey(d.getExtCode())){
                    deptlist.put(d.getExtCode(),d.getDeptname());
                }
            }

        }
        model.addAttribute("deptlist",deptlist);

    }
}
