package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;

    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public void saveUser(User user) {
        user.setRegistrationDate(LocalDateTime.now());
        userRepository.save(user);
    }

}
