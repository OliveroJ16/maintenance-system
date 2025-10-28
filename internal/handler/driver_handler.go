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

func (handler *DriverHandler) Drivers(response http.ResponseWriter, request *http.Request) {
	response.Header().Set("Content-Type", "application/json")

	switch request.Method {
	case http.MethodPost:
        handler.registerDriver(response, request)
	default:
		response.WriteHeader(http.StatusMethodNotAllowed)
		json.NewEncoder(response).Encode(map[string]string{
			"error": "Method not allowed",
		})
	}
}

func (handler *DriverHandler) registerDriver(response http.ResponseWriter, request *http.Request) {
	var driver model.Driver
	if err := json.NewDecoder(request.Body).Decode(&driver); err != nil {
		response.WriteHeader(http.StatusBadRequest)
		json.NewEncoder(response).Encode(map[string]string{
			"error": fmt.Sprintf("Invalid request body: %v", err),
		})
		return
	}

	id, err := handler.driverService.RegisterDriver(&driver)
	if err != nil {
		response.WriteHeader(http.StatusInternalServerError)
		json.NewEncoder(response).Encode(map[string]string{
			"error": fmt.Sprintf("Error creating driver: %v", err),
		})
		return
	}

	response.WriteHeader(http.StatusCreated)
	json.NewEncoder(response).Encode(map[string]interface{}{
		"message": "Driver created successfully",
		"id":      id,
	})
}
