package config

import (
    "github.com/joho/godotenv"
    "log"
    "os"
)

type Config struct {
    DBUser string
    DBPass string
    DBHost string
    DBName string
    Port   string
}

func LoadConfig() *Config {
    err := godotenv.Load()
    if err != nil {
        log.Println("No .env file found, using system environment variables")
    }

    return &Config{
        DBUser: os.Getenv("DB_USER"),
        DBPass: os.Getenv("DB_PASS"),
        DBHost: os.Getenv("DB_HOST"),
        DBName: os.Getenv("DB_NAME"),
        Port:   os.Getenv("PORT"),
    }
}
