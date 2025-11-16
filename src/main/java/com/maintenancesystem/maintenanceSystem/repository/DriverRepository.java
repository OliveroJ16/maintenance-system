package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.enums.LicenseCategory;
import com.maintenancesystem.maintenanceSystem.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    @Modifying
    @Transactional
    @Query("""
    UPDATE Driver d
    SET 
        d.firstName = COALESCE(:firstName, d.firstName),
        d.lastName = COALESCE(:lastName, d.lastName),
        d.idCard = COALESCE(:idCard, d.idCard),
        d.phone = COALESCE(:phone, d.phone),
        d.email = COALESCE(:email, d.email),
        d.licenseCategory = COALESCE(:licenseCategory, d.licenseCategory),
        d.licenseExpirationDate = COALESCE(:licenseExpirationDate, d.licenseExpirationDate),
        d.status = COALESCE(:status, d.status)
    WHERE d.idDriver = :idDriver""")
    int updatePartial(
            @Param("idDriver") Integer idDriver,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("idCard") String idCard,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("licenseCategory") LicenseCategory licenseCategory,
            @Param("licenseExpirationDate") LocalDate licenseExpirationDate,
            @Param("status") Status status
    );

}
