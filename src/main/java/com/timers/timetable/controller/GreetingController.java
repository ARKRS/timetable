package com.timers.timetable.controller;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.deptsmanagement.DeptsTree;
import com.timers.timetable.repos.DeptsRepo;
import com.timers.timetable.service.UserService;
import com.timers.timetable.statics.ParameterFiller;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GreetingController {
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private UserService userService;

    private Model fillModelParameters(Model model) {
        //TODO переделать это! Нужно department при авторизации получить
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            model.addAttribute("userAuthorized", false);
            return model;
        } else if (auth.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("userAuthorized", false);
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

        User curUser = userService.getUserByUsername(username);

        boolean isAdmin = curUser != null && curUser.isAdmin();

        model.addAttribute("userAuthorized", curUser != null);

        model.addAttribute("isAdmin", curUser != null && curUser.isAdmin());

        if (isAdmin == true){

            model.addAttribute("deptartmentTree",CreateTreeOfDepartments());
        }

        Department department = deptsRepo.findBySupervisor(curUser);

        if (department == null)
            model.addAttribute("department", null);
        else

            model.addAttribute("department", department.getDeptname());

        return model;
    }

    @GetMapping()
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {

        model.addAttribute("name", name);

        model.addAttribute("today", new SimpleDateFormat("dd.MM.yyy").format(Calendar.getInstance().getTime()));

        ParameterFiller.fillModelParameters(model,userService,deptsRepo);

        if (model.getAttribute("userAuthorized").equals(false)) {
            return "redirect:/login";
        }

        return "hello";
    }

    @GetMapping("/gotohello")
    public String gotohello(Model model) {

        ParameterFiller.fillModelParameters(model, userService, deptsRepo);
        return "hello";
    }

    @PostMapping("/gotohello")
    public String postgoString(Model model) {

        ParameterFiller.fillModelParameters(model, userService, deptsRepo);
        return "hello";

    }

    public HashMap<Department,DeptsTree<Department>> CreateTreeOfDepartments(){

        HashMap<BigInteger,Department> deptsCache;

        Department emptylink = new Department();
        deptsCache = new HashMap<BigInteger,Department>();

        List<Object[]> listofparents = deptsRepo.findAllParents();

        Hashtable<Department,Department> hashtable = new Hashtable<>();

        for (int i = 0; i < listofparents.size(); i++) {

            Object[] obj = listofparents.get(i);
            hashtable.put(
                    findDeptinCache(deptsCache, (BigInteger) listofparents.get(i)[0],emptylink),
                    findDeptinCache(deptsCache, (BigInteger) listofparents.get(i)[1],emptylink));
        }

        List<Object[]> listofchilds = deptsRepo.findAllChildren();
        for (int i = 0; i < listofchilds.size(); i++) {

            Object[] obj = listofchilds.get(i);
            hashtable.put(
                    findDeptinCache(deptsCache, (BigInteger) listofchilds.get(i)[0],emptylink),
                    findDeptinCache(deptsCache, (BigInteger) listofchilds.get(i)[1],emptylink));
        }


        Set<Department> set = hashtable.keySet();
        Iterator<Department> it = set.iterator();
        HashMap<Department,DeptsTree<Department>> maptree = new HashMap<>();
        while (it.hasNext()){
            Department curChild = it.next();
            Department curParent = hashtable.get(curChild);
            DeptsTree curTree = new DeptsTree(curParent);
            if (!maptree.containsKey(curParent)){
                DeptsTree d = curTree;
                d.addChild(curChild);
                maptree.put(curParent,d);
            }
            else {
                maptree.get(curParent).addChild(curChild);

            }
        }

        //last loop to make hierarchy
        DeptsTree<Department> emptytree = new DeptsTree<>(emptylink);
        Iterator<Map.Entry<Department,DeptsTree<Department>>> iter = maptree.entrySet().iterator();

        while (iter.hasNext()){
            Map.Entry<Department,DeptsTree<Department>> entry = iter.next();
            Department curParent = hashtable.get(entry.getKey());
            if(entry.getValue().getData().equals(emptylink)){
                continue;
            }
            if (curParent.equals(emptylink)){
                continue;
            }
            boolean need2add = true;
            for (DeptsTree<Department> child: maptree.get(curParent).getChildren()
            ) {
                if (child.getData().equals(entry.getValue().getData())){
                    child.addChildren(entry.getValue().getChildren());
                    need2add = false;
                    break;
                }
            }

            if (need2add==false){
                iter.remove();
            }

        }

        //тут остались только самые верхние группы и null - null удаляем.
        iter = maptree.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<Department, DeptsTree<Department>> entry = iter.next();
            if(entry.getValue().getData().equals(emptylink)){
                iter.remove();
            }
        }

        return maptree;

    }

    public Department findDeptinCache(HashMap<BigInteger, Department> cache, BigInteger id, Department emplylink){

        if (cache.containsKey(id)){
            return cache.get(id);
        }

        if (id.equals(BigInteger.valueOf(0))){
            cache.put(BigInteger.valueOf(0), emplylink);
            return cache.get(id);
        }


        Department dept = deptsRepo.findById(id.longValue()).get();
        cache.put(id,dept);
        return dept;
    }
}