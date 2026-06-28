package com.maintenancesystem.maintenanceSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Integer idMaintenance;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "km_programado", nullable = false)
    private Integer scheduledKm;

    @Column(name = "fecha_ejecucion")
    private LocalDate executionDate;

    @Column(name = "km_ejecucion")
    private Integer executionKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('PENDIENTE','EN_PROCESO','COMPLETADO','CANCELADO') DEFAULT 'PENDIENTE'")
    private MaintenanceStatus status = MaintenanceStatus.PENDIENTE;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mantenimiento_vehiculo"))
    @JsonIgnoreProperties({"maintenances", "assignments", "hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_mant", nullable = false,
            foreignKey = @ForeignKey(name = "fk_mantenimiento_tipo"))
    @JsonIgnoreProperties({"maintenances", "hibernateLazyInitializer", "handler"})
    private MaintenanceType maintenanceType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_taller",
            foreignKey = @ForeignKey(name = "fk_mantenimiento_taller"))
    @JsonIgnoreProperties({"maintenances", "hibernateLazyInitializer", "handler"})
    private Workshop workshop;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // ========== MÉTODOS DE CONVENIENCIA ==========

    @Transient
    public LocalDate getMaintenanceDate() {
        return executionDate != null ? executionDate : scheduledDate;
    }

    @Transient
    public Integer getKilometers() {
        return executionKm != null ? executionKm : scheduledKm;
    }

    @Transient
    public boolean isExecuted() {
        return executionDate != null;
    }

    @Transient
    public boolean isCompleted() {
        return status == MaintenanceStatus.COMPLETADO;
    }
}