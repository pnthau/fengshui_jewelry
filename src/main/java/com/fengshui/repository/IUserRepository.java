package com.fengshui.repository;

import com.fengshui.entity.User;
import java.util.List;

public interface IUserRepository {
    List<User> findAll();
    User findByID(int id);
    User findByUsername(String username);
    boolean save(User user);
    boolean update(User user);
    boolean delete(int id);
}
