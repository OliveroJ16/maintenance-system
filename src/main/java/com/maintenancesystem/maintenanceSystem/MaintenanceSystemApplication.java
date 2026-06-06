package com.maintenancesystem.maintenanceSystem;

import com.maintenancesystem.maintenanceSystem.config.AdminSeedProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties(AdminSeedProperties.class)
@EnableScheduling
@SpringBootApplication
public class MaintenanceSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaintenanceSystemApplication.class, args);
	}
}
