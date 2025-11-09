package com.maintenancesystem.maintenanceSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion_mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conf_mant")
    private Integer idMaintenanceConfig;

    @Column(name = "frecuencia_km")
    private Integer frequencyKm;

    @Column(name = "frecuencia_meses")
    private Integer frequencyMonths;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_mant", nullable = false, foreignKey = @ForeignKey(name = "fk_configuracion_tipo_mantenimiento"))
    private MaintenanceType maintenanceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", nullable = false, foreignKey = @ForeignKey(name = "fk_configuracion_vehiculo"))
    private Vehicle vehicle;
}
