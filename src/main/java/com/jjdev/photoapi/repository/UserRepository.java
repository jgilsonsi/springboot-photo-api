package com.jjdev.photoapi.repository;

import com.jjdev.photoapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    public User findByEmail(String userEmail);

    @Query("SELECT user FROM User user WHERE user.id=:id")
    public User findUserById(@Param("id") Long id);

    public List<User> findByUsernameContaining(String username);
}