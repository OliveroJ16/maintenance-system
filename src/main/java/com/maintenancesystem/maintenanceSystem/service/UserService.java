package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public void saveUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    public boolean updateUser(Integer id, User dto) {
        int rows = userRepository.updatePartial(
                id,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getRole()
        );
        return rows > 0;
    }

}
