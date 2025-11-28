package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.service.MaintenanceAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para generar y actualizar alertas automáticamente
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AlertScheduler {

    private final MaintenanceAlertService alertService;

    /**
     * Genera alertas preventivas diariamente a las 6:00 AM
     * Cron: segundo minuto hora día mes día-semana
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void generateDailyAlerts() {
        log.info("Iniciando generación automática de alertas preventivas...");
        try {
            alertService.generatePreventiveAlerts();
            log.info("Alertas preventivas generadas exitosamente");
        } catch (Exception e) {
            log.error("Error al generar alertas preventivas: {}", e.getMessage());
        }
    }

    /**
     * Actualiza estados de alertas vencidas diariamente a las 1:00 AM
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void updateExpiredAlerts() {
        log.info("Iniciando actualización de alertas vencidas...");
        try {
            alertService.updateExpiredAlerts();
            log.info("Alertas vencidas actualizadas exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar alertas vencidas: {}", e.getMessage());
        }
    }

    /**
     * Verifica alertas cada 4 horas durante el día (para alertas más precisas)
     * Se ejecuta a las 8:00, 12:00, 16:00, 20:00
     */
    @Scheduled(cron = "0 0 8,12,16,20 * * *")
    //@Scheduled(cron = "*/10 * * * * *")
    public void checkAlertsFrequently() {
        log.info("Verificación frecuente de alertas...");
        try {
            alertService.generatePreventiveAlerts();
            alertService.updateExpiredAlerts();
            log.info("Verificación de alertas completada");
        } catch (Exception e) {
            log.error("Error en verificación frecuente de alertas: {}", e.getMessage());
        }
    }
}