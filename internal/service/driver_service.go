package service

import (
	"fmt"
	"maintenancesystem/internal/model"
	"maintenancesystem/internal/repository"
	"maintenancesystem/internal/utils"
	"strings"
	"time"
)

type DriverService struct {
	driverRepository *repository.DriverRepository
}

func NewDriverService(repository *repository.DriverRepository) *DriverService {
	return &DriverService{driverRepository: repository}
}

func (service *DriverService) RegisterDriver(driver *model.Driver) (int64, error) {

	if err := validateDriver(driver); err != nil {
		return 0, err
	}

	driver.Name = utils.ToTitleCase(driver.Name)
	driver.LastName = utils.ToTitleCase(driver.LastName)

	id, err := service.driverRepository.RegisterDriver(driver)
	if err != nil {
		return 0, err
	}

	return id, nil
}

func validateDriver(driver *model.Driver) error {
	if driver.Name == "" {
		return fmt.Errorf("name is required")
	}

	if driver.LastName == "" {
		return fmt.Errorf("last name is required")
	}

	if driver.IDNumber == "" {
		return fmt.Errorf("ID number is required")
	}

	if driver.Email != "" && !strings.Contains(driver.Email, "@") {
		return fmt.Errorf("invalid email address")
	}

	if !driver.LicenseExpiry.IsZero() && driver.LicenseExpiry.Before(time.Now()) {
		return fmt.Errorf("license has already expired")
	}

	validCategories := map[string]bool{
		"A": true, "B": true, "C": true, "D": true,
		"E": true, "F": true, "G": true, "H": true, "I": true,
	}
	if driver.LicenseCategory != "" && !validCategories[driver.LicenseCategory] {
		return fmt.Errorf("invalid license category")
	}

	return nil
}
