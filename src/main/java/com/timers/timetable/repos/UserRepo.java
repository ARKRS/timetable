package com.timers.timetable.repos;

import com.timers.timetable.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsernameAndActive(String username, boolean isActive);



    User findByActivationCode(String code);

    void deleteById(Long id);
}
