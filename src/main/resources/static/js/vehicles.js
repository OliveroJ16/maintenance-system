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

// -------------------------------------------
// ⭐ MODAL DE ASIGNACIÓN (ABRIR / CERRAR)
// -------------------------------------------
window.openAssignModal = function(button) {
    const vehicleId = button.getAttribute('data-id');
    const vehiclePlate = button.getAttribute('data-plate');
    
    document.getElementById('assignVehicleId').value = vehicleId;
    document.getElementById('assignVehiclePlate').value = vehiclePlate;
    
    // Fecha actual por defecto
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('assignmentDate').value = today;
    
    document.getElementById('assignDriverModal').classList.remove('hidden');
};

window.closeAssignModal = function() {
    document.getElementById('assignDriverModal').classList.add('hidden');
    document.getElementById('assignDriverForm').reset();
};

// Cerrar al hacer clic fuera
window.onclick = function(event) {
    const assignModal = document.getElementById('assignDriverModal');
    if (event.target === assignModal) {
        closeAssignModal();
    }
};

// -------------------------------------------
// ⭐ FUNCIÓN REAL DE ASIGNAR (AHORA SÍ CON FETCH)
// -------------------------------------------
window.assignVehicle = async function(event) {
    event.preventDefault();

    const form = document.getElementById('assignDriverForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/vehicle-assignments/assign', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeAssignModal();
            
            Swal.fire({
                title: '¡Conductor asignado!',
                text: 'La asignación se completó correctamente.',
                icon: 'success',
                confirmButtonColor: '#6366f1'
            });

            // Recargar SOLO la sección vehicles
            setTimeout(() => {
                loadSection('vehicles');
            }, 400);

        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo completar la asignación.',
                confirmButtonColor: '#6366f1'
            });
        }

    } catch (error) {
        console.error(error);
        Swal.fire({
            icon: 'error',
            title: 'Error de conexión',
            text: 'No se pudo contactar con el servidor.',
            confirmButtonColor: '#6366f1'
        });
    }
};
