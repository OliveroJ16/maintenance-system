package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
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
    private final StringNormalizer stringNormalizer;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setFirstName(stringNormalizer.toTitleCase(user.getFirstName()));
        user.setLastName(stringNormalizer.toTitleCase(user.getLastName()));
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

    public void updateUser(Integer id, User user) {
        user.setFirstName(stringNormalizer.toTitleCase(user.getFirstName()));
        user.setLastName(stringNormalizer.toTitleCase(user.getLastName()));
        userRepository.updatePartial(
                id,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
