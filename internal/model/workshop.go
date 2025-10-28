package model

type Workshop struct {
	ID           int    `db:"id_taller" json:"id_taller"`
	Name         string `db:"nombre_taller" json:"nombre_taller"`
	Address      string `db:"direccion" json:"direccion,omitempty"`
	Phone        string `db:"telefono" json:"telefono"`
	Email        string `db:"email" json:"email,omitempty"`
	Specialty    string `db:"especialidad" json:"especialidad,omitempty"`
	Status       string `db:"estado" json:"estado"`
}
