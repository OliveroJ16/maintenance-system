package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idService;

    @Column(name = "nombre_servicio", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "costo", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal cost;

    @Column(name = "tiempo_minutos")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('activo','inactivo') DEFAULT 'activo'")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_taller", nullable = false, foreignKey = @ForeignKey(name = "fk_servicio_taller"))
    private Workshop workshop;
}
