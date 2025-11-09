package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "taller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taller")
    private Integer idWorkshop;

    @Column(name = "nombre_taller", nullable = false, length = 100)
    private String workshopName;

    @Column(name = "direccion", length = 200)
    private String address;

    @Column(name = "telefono", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "especialidad", length = 100)
    private String specialty;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('activo','inactivo') DEFAULT 'activo'")
    private Status status;
}

