package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Integer idVehicle;

    @Column(name = "placa", nullable = false, unique = true, length = 20)
    private String plate;

    @Column(name = "numero_serie", nullable = false, length = 50)
    private String serialNumber;

    @Column(name = "kilometraje", columnDefinition = "INT DEFAULT 0")
    private Integer mileage = 0;

    @Column(name = "fecha_adquisicion")
    private LocalDate acquisitionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_vehiculo", columnDefinition = "ENUM('activo','inactivo','mantenimiento') DEFAULT 'activo'")
    private VehicleStatus status = VehicleStatus.ACTIVO;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustible", columnDefinition = "ENUM('gasolina','diesel','electrico','hibrido','otro')")
    private FuelType fuelType;

    @Column(name = "marca", length = 50)
    private String brand;

    @Column(name = "modelo", length = 50)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", columnDefinition = "ENUM('sedán','SUV','camioneta','van','camión','otro') NOT NULL")
    private VehicleType vehicleType;
}
