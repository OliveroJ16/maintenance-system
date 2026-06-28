package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.VehicleRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.VehicleResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Vehicle;
import com.maintenancesystem.maintenanceSystem.enums.VehicleStatus;
import com.maintenancesystem.maintenanceSystem.mapper.VehicleMapper;
import com.maintenancesystem.maintenanceSystem.repository.VehicleRepository;
import com.maintenancesystem.maintenanceSystem.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    @Override
    public VehicleResponseDTO saveVehicle(VehicleRequestDTO request) {
        Vehicle vehicle = vehicleMapper.toEntity(request);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponseDTO(savedVehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicle(Integer id, VehicleRequestDTO request) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        Vehicle tempVehicle = vehicleMapper.toEntity(request);
        vehicleRepository.updatePartial(
                id,
                tempVehicle.getPlate(),
                tempVehicle.getSerialNumber(),
                tempVehicle.getMileage(),
                tempVehicle.getAcquisitionDate(),
                tempVehicle.getStatus(),
                tempVehicle.getFuelType(),
                tempVehicle.getBrand(),
                tempVehicle.getModel(),
                tempVehicle.getVehicleType()
        );
        Vehicle updatedVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        return vehicleMapper.toResponseDTO(updatedVehicle);
    }

    @Override
    public VehicleResponseDTO getVehicleById(Integer id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
        return vehicleMapper.toResponseDTO(vehicle);
    }

    @Override
    public void deleteVehicle(Integer id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public long countActiveVehicles() {
        return vehicleRepository.countByStatus(VehicleStatus.ACTIVO);
    }
}