package db

import (
    "database/sql"
    "fmt"
    "log"

    "maintenancesystem/config"

    _ "github.com/go-sql-driver/mysql"
)

func Connect(cfg *config.Config) *sql.DB {
    connectionURL := fmt.Sprintf("%s:%s@tcp(%s)/%s?parseTime=true", cfg.DBUser, cfg.DBPass, cfg.DBHost, cfg.DBName)

    database, err := sql.Open("mysql", connectionURL)
    if err != nil {
        log.Fatalf("Failed to open database connection: %v", err)
    }

    if err := database.Ping(); err != nil {
        log.Fatalf("Failed to connect to the database: %v", err)
    }

    log.Printf("Successfully connected to the database %s", cfg.DBName)
    return database
}
