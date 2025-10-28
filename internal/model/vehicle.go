package model

import "time"

type Vehicle struct {
	ID             int       `db:"id_vehiculo" json:"id_vehiculo"`
	Plate          string    `db:"placa" json:"placa"`
	SerialNumber   string    `db:"numero_serie" json:"numero_serie"`
	Mileage        int       `db:"kilometraje" json:"kilometraje"`
	PurchaseDate   time.Time `db:"fecha_adquisicion" json:"fecha_adquisicion"`
	Status         string    `db:"estado_vehiculo" json:"estado_vehiculo"`
	FuelType       string    `db:"tipo_combustible" json:"tipo_combustible"`
	Brand          string    `db:"marca" json:"marca"`
	Model          string    `db:"modelo" json:"modelo"`
	VehicleType    string    `db:"tipo_vehiculo" json:"tipo_vehiculo"`
}
