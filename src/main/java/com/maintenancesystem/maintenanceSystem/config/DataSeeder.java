package com.maintenancesystem.maintenanceSystem.config;

import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.enums.Role;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminSeedProperties props;

    @Override
    public void run(String... args) {

        if (!props.enabled()) {
            System.out.println("Seeder desactivado");
            return;
        }

        if (userRepository.findByUsername(props.username()).isPresent()) {
            System.out.println("Admin ya existe");
            return;
        }

        User admin = new User();
        admin.setUsername(props.username());
        admin.setFirstName("Admin");
        admin.setLastName("System");
        admin.setEmail(props.email());
        admin.setRole(Role.ADMINISTRADOR);
        admin.setRegistrationDate(LocalDateTime.now());

        admin.setPassword(passwordEncoder.encode(props.password()));

        userRepository.save(admin);

        System.out.println("Admin creado correctamente");
    }
}