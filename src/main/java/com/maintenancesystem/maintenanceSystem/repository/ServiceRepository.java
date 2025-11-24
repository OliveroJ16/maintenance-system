package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    @Query("SELECT s FROM Service s WHERE s.workshop.idWorkshop = :idWorkshop")
    List<Service> findByWorkshopId(@Param("idWorkshop") Integer idWorkshop);

}
