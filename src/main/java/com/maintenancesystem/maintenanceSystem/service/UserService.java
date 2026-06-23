package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.repository.DriverRepository;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import com.maintenancesystem.maintenanceSystem.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final StringNormalizer stringNormalizer;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @Transactional
    public User saveUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setFirstName(stringNormalizer.toTitleCase(user.getFirstName()));
        user.setLastName(stringNormalizer.toTitleCase(user.getLastName()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(Integer id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        existingUser.setFirstName(stringNormalizer.toTitleCase(user.getFirstName()));
        existingUser.setLastName(stringNormalizer.toTitleCase(user.getLastName()));
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setUsername(user.getUsername());

        if (user.getDriver() != null && user.getDriver().getIdDriver() != null) {
            Driver driver = driverRepository.findById(user.getDriver().getIdDriver())
                    .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
            existingUser.setDriver(driver);
        } else {
            existingUser.setDriver(null);
        }

        return userRepository.save(existingUser);
    }
}