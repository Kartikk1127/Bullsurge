package com.Kartikey.bullsurge.backend.service;

import com.Kartikey.bullsurge.backend.model.User;
import com.Kartikey.bullsurge.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public void addUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }
}
