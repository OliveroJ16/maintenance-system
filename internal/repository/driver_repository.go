package repository

import (
    "database/sql"
    "maintenancesystem/internal/model"
)

type DriverRepository struct {
    database *sql.DB
}

func NewDriverRepository(database *sql.DB) *DriverRepository {
    return &DriverRepository{database: database}
}

func (repository *DriverRepository) RegisterDriver(driver *model.Driver) (int64, error) {
    query := `
        INSERT INTO chofer
        (nombre, apellido, cedula, telefono, email, categoria_licencia, fecha_expiracion_licencia, estado)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `
    result, err := repository.database.Exec(
        query,
        driver.Name,
        driver.LastName,
        driver.IDNumber,
        driver.Phone,
        driver.Email,
        driver.LicenseCategory,
        driver.LicenseExpiry,
        driver.Status,
    )
    if err != nil {
        return 0, err
    }
    return result.LastInsertId()
}
