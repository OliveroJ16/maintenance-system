package model

type MaintenanceConfig struct {
	ID              int    `db:"id_conf_mant" json:"id_conf_mant"`
	FrequencyKM     int    `db:"frecuencia_km" json:"frecuencia_km,omitempty"`
	FrequencyMonths int    `db:"frecuencia_meses" json:"frecuencia_meses,omitempty"`
	Description     string `db:"descripcion" json:"descripcion,omitempty"`
	TypeID          int    `db:"id_tipo_mant" json:"id_tipo_mant"`
	VehicleID       int    `db:"id_vehiculo" json:"id_vehiculo"`
}