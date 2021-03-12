package com.timers.timetable.repos;

import com.timers.timetable.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsernameAndActive(String username, boolean isActive);


    Optional<User> findById(Long id);

    User findByActivationCode(String code);

    void deleteById(Long id);

    User findByEmail(String email);
}
