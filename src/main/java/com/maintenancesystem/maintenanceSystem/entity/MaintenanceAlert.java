package com.maintenancesystem.maintenanceSystem.entity;

import com.maintenancesystem.maintenanceSystem.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "alerta_mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlert;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", columnDefinition = "ENUM('preventiva','correctiva') DEFAULT 'preventiva'")
    private AlertType alertType;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDate alertDate;

    @Column(name = "km_alerta")
    private Integer kmAlert;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_alerta", columnDefinition = "ENUM('notificada','atendida','vencida') DEFAULT 'notificada'")
    private AlertStatus alertStatus;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String message;

    @Column(name = "visto", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean viewed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conf_mant", nullable = true, foreignKey = @ForeignKey(name = "fk_alerta_configuracion"))
    private MaintenanceConfiguration maintenanceConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_mant")
    private MaintenanceType maintenanceType;

}
