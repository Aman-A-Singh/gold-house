package com.goldhouse.server.service;

import com.goldhouse.server.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
