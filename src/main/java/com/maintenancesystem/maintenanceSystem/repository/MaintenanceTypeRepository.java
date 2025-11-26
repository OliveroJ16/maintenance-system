package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceType;
import com.maintenancesystem.maintenanceSystem.enums.MaintenanceCategory;
import com.maintenancesystem.maintenanceSystem.enums.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceType, Integer> {

    @Modifying
    @Transactional
    @Query("""
    UPDATE MaintenanceType mt
    SET
        mt.typeName = COALESCE(:typeName, mt.typeName),
        mt.description = COALESCE(:description, mt.description),
        mt.category = COALESCE(:category, mt.category),
        mt.priority = COALESCE(:priority, mt.priority)
    WHERE mt.idMaintenanceType = :id
""")
    void updatePartial(
            @Param("id") Integer id,
            @Param("typeName") String typeName,
            @Param("description") String description,
            @Param("category") MaintenanceCategory category,
            @Param("priority") PriorityLevel priority
    );

}
