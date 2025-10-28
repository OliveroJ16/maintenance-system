package model

import "time"

type VehicleAssignment struct {
	DriverID       int       `db:"id_chofer" json:"id_chofer"`
	VehicleID      int       `db:"id_vehiculo" json:"id_vehiculo"`
	AssignmentDate time.Time `db:"fecha_asignacion" json:"fecha_asignacion"`
}
