package com.Manoj.user_service.repository;

import com.Manoj.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findById(int id);
    User findByEmail(String email);
    User findByUsername(String username);
}
