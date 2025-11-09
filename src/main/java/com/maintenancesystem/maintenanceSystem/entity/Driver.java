package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.LicenseCategory;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "chofer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chofer")
    private Integer idDriver;

    @Column(name = "nombre", nullable = false, length = 50)
    private String firstName;

    @Column(name = "apellido", nullable = false, length = 50)
    private String lastName;

    @Column(name = "cedula", unique = true, length = 20)
    private String idCard;

    @Column(name = "telefono", length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_licencia", columnDefinition = "ENUM('A','B','C','D','E','F','G','H','I')")
    private LicenseCategory licenseCategory;

    @Column(name = "fecha_expiracion_licencia")
    private LocalDate licenseExpirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('activo','inactivo') DEFAULT 'activo'")
    private Status status = Status.ACTIVO;
}
