package com.fengshui.service;

import com.fengshui.entity.User;
import com.fengshui.repository.IUserRepository;
import com.fengshui.repository.UserRepository;

import java.util.List;

public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByID(int id) {
        return userRepository.findByID(id);
    }

    @Override
    public boolean save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    @Override
    public boolean delete(int id) {
        return userRepository.delete(id);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
