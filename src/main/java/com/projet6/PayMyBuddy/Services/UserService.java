package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Iterable<User> getUser() {
        return userRepository.findAll();
    }
}
