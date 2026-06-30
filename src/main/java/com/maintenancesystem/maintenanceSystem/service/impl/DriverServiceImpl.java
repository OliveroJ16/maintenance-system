package com.maintenancesystem.maintenanceSystem.service.impl;

import com.maintenancesystem.maintenanceSystem.dto.request.DriverRequestDTO;
import com.maintenancesystem.maintenanceSystem.dto.response.DriverResponseDTO;
import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.mapper.DriverMapper;
import com.maintenancesystem.maintenanceSystem.repository.DriverRepository;
import com.maintenancesystem.maintenanceSystem.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public List<DriverResponseDTO> getAllDriver() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponseDTO getDriverById(Integer id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + id));
        return driverMapper.toResponseDTO(driver);
    }

    @Override
    @Transactional
    public DriverResponseDTO saveDriver(DriverRequestDTO driverRequestDTO) {
        Driver driver = driverMapper.toEntity(driverRequestDTO);
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toResponseDTO(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponseDTO updateDriver(Integer id, DriverRequestDTO driverRequestDTO) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado con id: " + id));

        driverMapper.updateEntityFromRequest(driverRequestDTO, existingDriver);

        driverRepository.updatePartial(
                id,
                existingDriver.getFirstName(),
                existingDriver.getLastName(),
                existingDriver.getIdCard(),
                existingDriver.getPhone(),
                existingDriver.getEmail(),
                existingDriver.getLicenseCategory(),
                existingDriver.getLicenseExpirationDate(),
                existingDriver.getStatus()
        );

        return driverMapper.toResponseDTO(existingDriver);
    }

    @Override
    public void deleteDriver(Integer id) {
        if (!driverRepository.existsById(id)) {
            throw new RuntimeException("Conductor no encontrado con id: " + id);
        }
        driverRepository.deleteById(id);
    }
}