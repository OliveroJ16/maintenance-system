package com.maintenancesystem.maintenanceSystem.service;

import com.maintenancesystem.maintenanceSystem.enums.MaintenanceStatus;
import com.maintenancesystem.maintenanceSystem.enums.VehicleStatus;
import com.maintenancesystem.maintenanceSystem.pdf.CostReportService;
import com.maintenancesystem.maintenanceSystem.pdf.MaintenanceReportService;
import com.maintenancesystem.maintenanceSystem.pdf.StatisticsReportService;
import com.maintenancesystem.maintenanceSystem.pdf.VehicleReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final VehicleReportService vehicleReportService;
    private final MaintenanceReportService maintenanceReportService;
    private final CostReportService costReportService;
    private final StatisticsReportService statisticsReportService;

    public ByteArrayInputStream generateVehiclesAssignmentsPDF(VehicleStatus status) {
        return vehicleReportService.generateVehiclesAssignmentsPDF(status);
    }

    public ByteArrayInputStream generateMaintenancesPDF(LocalDate startDate, LocalDate endDate,
                                                        MaintenanceStatus status, Integer vehicleId) {
        return maintenanceReportService.generateMaintenancesPDF(startDate, endDate, status, vehicleId);
    }

    public ByteArrayInputStream generateCostsPDF(LocalDate startDate, LocalDate endDate, Integer workshopId) {
        return costReportService.generateCostsPDF(startDate, endDate, workshopId);
    }

    public ByteArrayInputStream generateStatisticsPDF() {
        return statisticsReportService.generateStatisticsPDF();
    }
}