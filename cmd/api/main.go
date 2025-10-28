package main

import (
    "fmt"
    "log"
    "net/http"

    "maintenancesystem/config"
    "maintenancesystem/internal/repository/db"

    "maintenancesystem/internal/repository"
    "maintenancesystem/internal/service"
    "maintenancesystem/internal/handler"
)

func main() {

    cfg := config.LoadConfig()

    database := db.Connect(cfg)
    defer database.Close()

    driverRepository := repository.NewDriverRepository(database)
    driverService := service.NewDriverService(driverRepository)
    driverHandler := handler.NewDriverHandler(driverService)

    router := http.NewServeMux()
    router.HandleFunc("/drivers", driverHandler.Drivers)

    address := fmt.Sprintf("0.0.0.0:%s", cfg.Port)
    log.Printf("Server started at http://%s", address)
    if err := http.ListenAndServe(address, router); err != nil {
        log.Fatalf("Failed to start server: %v", err)
    }
}
