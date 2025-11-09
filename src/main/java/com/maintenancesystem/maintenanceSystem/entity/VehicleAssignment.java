package com.maintenancesystem.maintenanceSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignacion_vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAssignment {

    @EmbeddedId
    private VehicleAssignmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("driverId")
    @JoinColumn(name = "id_chofer", foreignKey = @ForeignKey(name = "fk_asignacion_chofer"), nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vehicleId")
    @JoinColumn(name = "id_vehiculo", foreignKey = @ForeignKey(name = "fk_asignacion_vehiculo"), nullable = false)
    private Vehicle vehicle;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate assignmentDate;
}
