const vehiclesManager = new CRUDManager({
    entity: 'vehicles',
    entityName: 'vehículo',
    endpoint: '/vehicles',
    tableId: 'vehiclesTable',
    modalPrefix: 'vehicle'
});


window.confirmDeleteVehicle = (id) => vehiclesManager.confirmDelete(id);
window.openVehicleModal = () => vehiclesManager.openModal();
window.closeVehicleModal = () => vehiclesManager.closeModal();
window.saveVehicle = (e) => vehiclesManager.save(e);
window.updateVehicle = (e) => vehiclesManager.update(e);
window.closeEditVehicleModal = () => vehiclesManager.closeEditModal();
window.searchVehicles = (value) => vehiclesManager.search(value);

window.editVehicle = function(button) {
    const vehicleData = {
        idVehicle: button.getAttribute('data-id'),
        plate: button.getAttribute('data-plate'),
        serialNumber: button.getAttribute('data-serialnumber'),
        mileage: button.getAttribute('data-mileage'),
        acquisitionDate: button.getAttribute('data-acquisitiondate'),
        status: button.getAttribute('data-status'),
        fuelType: button.getAttribute('data-fueltype'),
        brand: button.getAttribute('data-brand'),
        model: button.getAttribute('data-model'),
        vehicleType: button.getAttribute('data-vehicletype')
    };
    vehiclesManager.openEditModal(vehicleData);
};