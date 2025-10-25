package model

import "time"

type Driver struct {
    ID              int       `json:"id_chofer"`
    Name            string    `json:"nombre"`
    LastName        string    `json:"apellido"`
    IDNumber        string    `json:"cedula,omitempty"`
    Phone           string    `json:"telefono,omitempty"`
    Email           string    `json:"email,omitempty"`
    LicenseCategory string    `json:"categoria_licencia,omitempty"`
    LicenseExpiry   time.Time `json:"fecha_expiracion_licencia"`
    Status          string    `json:"estado"`
}
