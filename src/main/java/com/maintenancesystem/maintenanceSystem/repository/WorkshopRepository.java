package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Workshop;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE Workshop w
        SET
            w.workshopName = COALESCE(:workshopName, w.workshopName),
            w.address = COALESCE(:address, w.address),
            w.phone = COALESCE(:phone, w.phone),
            w.email = COALESCE(:email, w.email),
            w.specialty = COALESCE(:specialty, w.specialty),
            w.status = COALESCE(:status, w.status)
        WHERE w.idWorkshop = :idWorkshop
    """)
    void updatePartial(
            @Param("idWorkshop") Integer idWorkshop,
            @Param("workshopName") String workshopName,
            @Param("address") String address,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("specialty") String specialty,
            @Param("status") Status status
    );
}
