package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.dto.request.UserRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.UserResponseDTO;
import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Integer id);
    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO);
    void deleteUser(Integer id);
}