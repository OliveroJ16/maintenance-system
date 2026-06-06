package com.maintenancesystem.maintenanceSystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.seed.admin")
public record AdminSeedProperties(
        boolean enabled,
        String username,
        String password,
        String email
) {}