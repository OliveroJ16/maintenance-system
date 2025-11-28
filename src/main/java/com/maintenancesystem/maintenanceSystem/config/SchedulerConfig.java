package com.maintenancesystem.maintenanceSystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuración para habilitar tareas programadas (Scheduled Tasks)
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Esta clase habilita el uso de @Scheduled en la aplicación
}