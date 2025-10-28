package model

import "time"

type MaintenanceAlert struct {
	ID           int       `db:"id_alerta" json:"id_alerta"`
	Type         string    `db:"tipo_alerta" json:"tipo_alerta"`
	Date         time.Time `db:"fecha_alerta" json:"fecha_alerta"`
	KMAlert      int       `db:"km_alerta" json:"km_alerta,omitempty"`
	Status       string    `db:"estado_alerta" json:"estado_alerta"`
	Message      string    `db:"mensaje" json:"mensaje,omitempty"`
	Seen         bool      `db:"visto" json:"visto"`
	ConfigID     int       `db:"id_conf_mant" json:"id_conf_mant"`
}
