package com.maintenancesystem.maintenanceSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleAssignmentId implements Serializable {

    @Column(name = "id_chofer")
    private Integer driverId;

    @Column(name = "id_vehiculo")
    private Integer vehicleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleAssignmentId that)) return false;
        return Objects.equals(driverId, that.driverId) &&
                Objects.equals(vehicleId, that.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, vehicleId);
    }
}
