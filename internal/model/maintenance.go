package model

import "time"

type Maintenance struct {
	ID             int       `db:"id_mantenimiento" json:"id_mantenimiento"`
	ScheduledDate  time.Time `db:"fecha_programada" json:"fecha_programada"`
	ScheduledKM    int       `db:"km_programado" json:"km_programado"`
	ExecutedDate   time.Time `db:"fecha_ejecucion" json:"fecha_ejecucion,omitempty"`
	ExecutedKM     int       `db:"km_ejecucion" json:"km_ejecucion,omitempty"`
	Status         string    `db:"estado" json:"estado"`
	Description    string    `db:"descripcion" json:"descripcion,omitempty"`
	VehicleID      int       `db:"id_vehiculo" json:"id_vehiculo"`
	TypeID         int       `db:"id_tipo_mant" json:"id_tipo_mant"`
	WorkshopID     *int      `db:"id_taller" json:"id_taller,omitempty"`
	CreatedAt      time.Time `db:"fecha_creacion" json:"fecha_creacion"`
}
