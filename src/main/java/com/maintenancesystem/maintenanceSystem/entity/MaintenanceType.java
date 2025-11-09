package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.MaintenanceCategory;
import com.maintenancesystem.maintenanceSystem.enums.PriorityLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_mant")
    private Integer idMaintenanceType;

    @Column(name = "nombre_tipo", nullable = false, length = 100)
    private String typeName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", columnDefinition = "ENUM('preventivo','correctivo') NOT NULL")
    private MaintenanceCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", columnDefinition = "ENUM('baja','media','alta')")
    private PriorityLevel priority;
}
