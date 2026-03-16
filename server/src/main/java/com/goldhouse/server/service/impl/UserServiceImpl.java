package com.goldhouse.server.service.impl;

import com.goldhouse.server.model.User;
import com.goldhouse.server.repository.UserRepository;
import com.goldhouse.server.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return userRepository.findUserById(id);
    }
}
