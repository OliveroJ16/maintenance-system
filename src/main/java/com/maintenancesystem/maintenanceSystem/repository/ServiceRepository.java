package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Service;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    @Query("SELECT s FROM Service s WHERE s.workshop.idWorkshop = :idWorkshop")
    List<Service> findByWorkshopId(@Param("idWorkshop") Integer idWorkshop);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Service s
    SET
        s.serviceName = COALESCE(:serviceName, s.serviceName),
        s.description = COALESCE(:description, s.description),
        s.cost = COALESCE(:cost, s.cost),
        s.durationMinutes = COALESCE(:durationMinutes, s.durationMinutes),
        s.status = COALESCE(:status, s.status)
    WHERE s.idService = :idService
""")
    void updatePartial(
            @Param("idService") Integer idService,
            @Param("serviceName") String serviceName,
            @Param("description") String description,
            @Param("cost") BigDecimal cost,
            @Param("durationMinutes") Integer durationMinutes,
            @Param("status") Status status
    );


}
