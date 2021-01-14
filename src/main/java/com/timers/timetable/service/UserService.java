package com.timers.timetable.service;


import com.timers.timetable.repos.UserRepo;
import com.timers.timetable.users.Role;
import com.timers.timetable.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsernameAndActive(username,true);
    }

    public boolean addUser(User user) {

        User userFromDB = userRepo.findByUsernameAndActive(user.getUsername(),true);

        if (userFromDB!=null)
            return false;

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Добрый день, %s \n"+
                            "Добро пожаловать в систему учета рабочего времени Компании Таймерс \n"+
                            "Пожалуйста, перейдите по ссылке для активации вашей учетной записи \n"+
                            "http://localhost:8080/activate/%s",
                    user.getUsername(),
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
}
