package model

type MaintenanceType struct {
	ID          int    `db:"id_tipo_mant" json:"id_tipo_mant"`
	Name        string `db:"nombre_tipo" json:"nombre_tipo"`
	Description string `db:"descripcion" json:"descripcion,omitempty"`
	Category    string `db:"categoria" json:"categoria"`
	Priority    string `db:"prioridad" json:"prioridad,omitempty"`
}
