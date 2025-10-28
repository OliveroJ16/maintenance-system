package model

type Service struct {
	ID          int     `db:"id_servicio" json:"id_servicio"`
	Name        string  `db:"nombre_servicio" json:"nombre_servicio"`
	Description string  `db:"descripcion" json:"descripcion,omitempty"`
	Cost        float64 `db:"costo" json:"costo"`
	DurationMin int     `db:"tiempo_minutos" json:"tiempo_minutos,omitempty"`
	Status      string  `db:"estado" json:"estado"`
	WorkshopID  int     `db:"id_taller" json:"id_taller"`
}
