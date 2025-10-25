package handler

import (
    "encoding/json"
    "fmt"
    "net/http"

    "maintenancesystem/internal/model"
    "maintenancesystem/internal/service"
)

type DriverHandler struct {
    driverService *service.DriverService
}

func NewDriverHandler(driverService *service.DriverService) *DriverHandler {
    return &DriverHandler{driverService: driverService}
}

func (handler *DriverHandler) RegisterDriver(response http.ResponseWriter, request *http.Request) {
    var driver model.Driver

    if err := json.NewDecoder(request.Body).Decode(&driver); err != nil {
        http.Error(response, fmt.Sprintf("Invalid request body: %v", err), http.StatusBadRequest)
        return
    }

    id, err := handler.driverService.RegisterDriver(&driver)
    if err != nil {
        http.Error(response, fmt.Sprintf("Error creating driver: %v", err), http.StatusInternalServerError)
        return
    }

    response.WriteHeader(http.StatusCreated)
    fmt.Fprintf(response, `{"message":"Driver created successfully","id":%d}`, id)
}
