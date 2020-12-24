package com.timers.timetable.repos;

import com.timers.timetable.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
