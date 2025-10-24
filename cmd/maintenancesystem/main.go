package main

import (
    "fmt"
    "log"
    "net/http"

    "maintenancesystem/config"
    "maintenancesystem/internal/repository/db"
)

func main() {

    cfg := config.LoadConfig()

    database := db.Connect(cfg)
    defer database.Close()

    addr := fmt.Sprintf("127.0.0.1:%s", cfg.Port)
    log.Printf("Server started at http://%s", addr)


    // Start HTTP server
    if err := http.ListenAndServe(addr, nil); err != nil {
        log.Fatalf("Failed to start server: %v", err)
    }
}
