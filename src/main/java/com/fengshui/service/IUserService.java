package com.fengshui.service;

import com.fengshui.entity.User;
import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findByID(int id);
    boolean save(User user);
    boolean update(User user);
    boolean delete(int id);
    User login(String username, String password);
}
