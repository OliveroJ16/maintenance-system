package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.UserRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.UserResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.mapper.UserMapper;
import com.maintenancesystem.maintenanceSystem.repository.DriverRepository;
import com.maintenancesystem.maintenanceSystem.repository.UserRepository;
import com.maintenancesystem.maintenanceSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        if (userRequestDTO.driverId() != null) {
            Driver driver = driverRepository.findById(userRequestDTO.driverId())
                    .orElseThrow(() -> new RuntimeException("Chofer no encontrado con ID: " + userRequestDTO.driverId()));
            user.setDriver(driver);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        userMapper.updateEntityFromRequest(userRequestDTO, existingUser);
        if (userRequestDTO.driverId() != null) {
            Driver driver = driverRepository.findById(userRequestDTO.driverId())
                    .orElseThrow(() -> new RuntimeException("Chofer no encontrado con ID: " + userRequestDTO.driverId()));
            existingUser.setDriver(driver);
        } else {
            existingUser.setDriver(null);
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}