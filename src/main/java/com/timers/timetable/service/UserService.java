package com.timers.timetable.service;


import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.users.Role;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Value("${hostname}")
    private  String hostname;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsernameAndActive(username,true);
    }

    public boolean addUser(User user) {

        User userFromDB = userRepo.findByUsernameAndActive(user.getUsername(),true);

        if (userFromDB!=null)
            return false;

        user.setEmail(user.getEmail().toLowerCase());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Добрый день, %s \n"+
                            "Добро пожаловать в систему учета рабочего времени Компании Таймерс \n"+
                            "Пожалуйста, перейдите по ссылке для активации вашей учетной записи \n"+
                            "%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(),"Activation code", message);
        }


        return true;
    }

    public User getUserByUsername(String username){
        User userFromDB = userRepo.findByUsernameAndActive(username,true);
        return userFromDB;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null){
            return false;
        }

        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public String updateUser(String username, User user, Map<String,String> form, Map<String,Object> model){

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

    public User findByActivationCode(String code) {

        User user = userRepo.findByActivationCode(code);

        return user;
//        if (user == null){
//            return false;
//        }
//
//        user.setActivationCode(null);
//        //user.setPassword(null);
//        userRepo.save(user);
//        return true;

    }

    public User findUserByEmail(String email){

        User user = userRepo.findByEmail(email);

        return user;

    }

    public void sendActivationCode(User user) {

        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Добрый день, %s \n"+

                            "пожалуйста, перейдите по ссылке для сброса пароля вашей учетной записи \n"+
                            "http://localhost:8080/restore/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(),"Ссылка для восстановления пароля", message);
        }


    }
    public void setNewPassword(User user){
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

    }

    public void simpleUpdateUser(User user){
        user.setActivationCode(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
