package db

import (
    "database/sql"
    "fmt"
    "log"

    "maintenancesystem/config"

    _ "github.com/go-sql-driver/mysql"
)

func Connect(cfg *config.Config) *sql.DB {
    dsn := fmt.Sprintf("%s:%s@tcp(%s)/%s?parseTime=true",
        cfg.DBUser, cfg.DBPass, cfg.DBHost, cfg.DBName)

    db, err := sql.Open("mysql", dsn)
    if err != nil {
        log.Fatalf("Failed to open database connection: %v", err)
    }

    if err := db.Ping(); err != nil {
        log.Fatalf("Failed to connect to the database: %v", err)
    }

    log.Printf("Successfully connected to the database %s", cfg.DBName)
    return db
}
