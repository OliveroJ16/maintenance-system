package com.maintenancesystem.maintenanceSystem.repository;

import com.maintenancesystem.maintenanceSystem.entity.MaintenanceAlert;
import com.maintenancesystem.maintenanceSystem.enums.AlertStatus;
import com.maintenancesystem.maintenanceSystem.enums.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceAlertRepository extends JpaRepository<MaintenanceAlert, Integer> {

    /**
     * Obtiene todas las alertas ordenadas por estado (vencida > notificada > atendida)
     * y luego por fecha
     */
    @Query("""
        SELECT a FROM MaintenanceAlert a
        LEFT JOIN FETCH a.maintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle v
        LEFT JOIN FETCH mc.maintenanceType mt
        ORDER BY 
            CASE a.alertStatus
                WHEN 'VENCIDA' THEN 1
                WHEN 'NOTIFICADA' THEN 2
                WHEN 'ATENDIDA' THEN 3
            END,
            a.alertDate ASC
    """)
    List<MaintenanceAlert> findAllOrderByPriorityAndDate();

    /**
     * Obtiene alertas urgentes (vencidas o próximas a vencer)
     */
    @Query("""
        SELECT a FROM MaintenanceAlert a
        LEFT JOIN FETCH a.maintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle v
        LEFT JOIN FETCH mc.maintenanceType mt
        WHERE a.alertStatus = 'VENCIDA'
        OR (a.alertStatus = 'NOTIFICADA' AND a.alertDate BETWEEN :today AND :threshold)
        ORDER BY a.alertDate ASC
    """)
    List<MaintenanceAlert> findUrgentAlerts(@Param("today") LocalDate today,
                                            @Param("threshold") LocalDate threshold);

    /**
     * Cuenta alertas urgentes
     */
    @Query("""
        SELECT COUNT(a) FROM MaintenanceAlert a
        WHERE a.alertStatus = 'VENCIDA'
        OR (a.alertStatus = 'NOTIFICADA' AND a.alertDate BETWEEN :today AND :threshold)
    """)
    Long countUrgentAlerts(@Param("today") LocalDate today,
                           @Param("threshold") LocalDate threshold);

    /**
     * Obtiene alertas no vistas
     */
    @Query("""
        SELECT a FROM MaintenanceAlert a
        LEFT JOIN FETCH a.maintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle v
        LEFT JOIN FETCH mc.maintenanceType mt
        WHERE a.viewed = false
        ORDER BY a.alertDate ASC
    """)
    List<MaintenanceAlert> findByViewedFalseOrderByAlertDateAsc();

    /**
     * Cuenta alertas no vistas
     */
    Long countByViewedFalse();

    /**
     * Obtiene alertas por tipo
     */
    @Query("""
        SELECT a FROM MaintenanceAlert a
        LEFT JOIN FETCH a.maintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle v
        LEFT JOIN FETCH mc.maintenanceType mt
        WHERE a.alertType = :type
        ORDER BY a.alertDate ASC
    """)
    List<MaintenanceAlert> findByAlertTypeOrderByAlertDateAsc(@Param("type") AlertType type);

    /**
     * Obtiene alertas por estado
     */
    @Query("""
        SELECT a FROM MaintenanceAlert a
        LEFT JOIN FETCH a.maintenanceConfiguration mc
        LEFT JOIN FETCH mc.vehicle v
        LEFT JOIN FETCH mc.maintenanceType mt
        WHERE a.alertStatus = :status
        ORDER BY a.alertDate ASC
    """)
    List<MaintenanceAlert> findByAlertStatusOrderByAlertDateAsc(@Param("status") AlertStatus status);

    /**
     * Cuenta alertas por estado
     */
    Long countByAlertStatus(AlertStatus status);

    /**
     * Verifica si existe una alerta reciente para una configuración
     */
    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM MaintenanceAlert a
    WHERE a.maintenanceConfiguration.idMaintenanceConfig = :configId
    AND a.alertDate BETWEEN :startDate AND :endDate
""")
    boolean existsByConfigurationAndDateRange(Integer configId, LocalDate startDate, LocalDate endDate);


    /**
     * Marca todas las alertas como vistas
     */
    @Modifying
    @Query("UPDATE MaintenanceAlert a SET a.viewed = true WHERE a.viewed = false")
    void markAllAsViewed();

    /**
     * Actualiza alertas notificadas cuya fecha ya pasó a estado VENCIDA
     */
    @Modifying
    @Query("""
        UPDATE MaintenanceAlert a 
        SET a.alertStatus = 'VENCIDA'
        WHERE a.alertStatus = 'NOTIFICADA'
        AND a.alertDate < :today
    """)
    void updateExpiredAlerts(@Param("today") LocalDate today);

    /**
     * Obtiene alertas de un vehículo específico
     */
    @Query("""
    SELECT a FROM MaintenanceAlert a
    JOIN a.maintenanceConfiguration mc
    JOIN mc.vehicle v
    WHERE v.idVehicle = :vehicleId
    ORDER BY a.alertDate DESC
""")
    List<MaintenanceAlert> findByVehicleId(Integer vehicleId);


}