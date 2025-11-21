package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.Driver;
import com.maintenancesystem.maintenanceSystem.entity.User;
import com.maintenancesystem.maintenanceSystem.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
    UPDATE User u 
    SET 
        u.firstName = COALESCE(:firstName, u.firstName),
        u.lastName = COALESCE(:lastName, u.lastName),
        u.email = COALESCE(:email, u.email),
        u.role = COALESCE(:role, u.role)
    WHERE u.idUser = :idUser
""")
    void updatePartial(
            @Param("idUser") Integer idUser,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("role") Role role
    );

}

