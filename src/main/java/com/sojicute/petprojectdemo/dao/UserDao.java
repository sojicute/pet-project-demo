package com.sojicute.petprojectdemo.dao;

import com.sojicute.petprojectdemo.domain.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    void save(User user);
}
