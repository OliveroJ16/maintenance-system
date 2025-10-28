package model

import "time"

type User struct {
	ID           int       `db:"id_usuario" json:"id_usuario"`
	Username     string    `db:"nombre_usuario" json:"nombre_usuario"`
	Name         string    `db:"nombre" json:"nombre"`
	LastName     string    `db:"apellido" json:"apellido"`
	Password     string    `db:"contrasena" json:"contrasena"`
	Role         string    `db:"rol" json:"rol"`
	Email        string    `db:"email" json:"email,omitempty"`
	RegisteredAt time.Time `db:"fecha_registro" json:"fecha_registro"`
	DriverID     *int      `db:"id_chofer" json:"id_chofer,omitempty"`
}