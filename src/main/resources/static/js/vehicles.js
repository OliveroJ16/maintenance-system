// ===============================
// ELIMINAR VEHÍCULO
// ===============================
window.confirmDeleteVehicle = function (id) {
    Swal.fire({
        title: "¿Eliminar vehículo?",
        text: "Esta acción no se puede revertir",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#e3342f",
        cancelButtonColor: "#6c757d",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar"
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`/vehiculos/delete/${id}`);

                if (response.ok) {
                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El vehículo fue eliminado correctamente",
                        icon: "success",
                        confirmButtonColor: "#6366f1",
                    });

                    setTimeout(() => {
                        loadSection('vehiculos');
                    }, 500);

                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "No se pudo eliminar el vehículo.",
                        confirmButtonColor: "#6366f1",
                    });
                }

            } catch (error) {
                console.error(error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: "No fue posible conectar con el servidor.",
                    confirmButtonColor: "#6366f1"
                });
            }
        }
    });
};


// ===============================
// ABRIR MODAL NUEVO VEHÍCULO
// ===============================
window.openVehicleModal = function () {
    const modal = document.getElementById('vehicleModal');
    if (modal) {
        modal.classList.remove('hidden');

        const title = document.getElementById('vehicleModalTitle');
        const form = document.getElementById('vehicleForm');

        if (title) title.textContent = 'Nuevo Vehículo';
        if (form) form.reset();

    } else {
        console.error('Modal de vehículos no encontrado');
    }
};


// ===============================
// CERRAR MODAL NUEVO VEHÍCULO
// ===============================
window.closeVehicleModal = function () {
    const modal = document.getElementById('vehicleModal');
    if (modal) modal.classList.add('hidden');
};


// ===============================
// EDITAR VEHÍCULO (ABRIR MODAL)
// ===============================
window.editVehicle = function(button) {
    // ✅ CORREGIDO: Los nombres ahora coinciden con el HTML
    const vehicleData = {
        idVehicle: button.getAttribute('data-id'),
        plate: button.getAttribute('data-plate'),
        serialNumber: button.getAttribute('data-serialnumber'),        // ✅ Corregido
        mileage: button.getAttribute('data-mileage'),
        acquisitionDate: button.getAttribute('data-acquisitiondate'),  // ✅ Corregido
        status: button.getAttribute('data-status'),
        fuelType: button.getAttribute('data-fueltype'),                // ✅ Corregido
        brand: button.getAttribute('data-brand'),
        model: button.getAttribute('data-model'),
        vehicleType: button.getAttribute('data-vehicletype')           // ✅ Corregido
    };

    openEditVehicleModal(vehicleData);
};


// ===============================
// MODAL DE EDICIÓN
// ===============================
window.openEditVehicleModal = function(vehicleData) {
    const modal = document.getElementById('editVehicleModal');
    if (!modal) {
        console.error('Modal de edición de vehículo no encontrado');
        return;
    }

    modal.classList.remove('hidden');

    const form = document.getElementById('editVehicleForm');
    if (form) {
        form.querySelector('input[name="idVehicle"]').value = vehicleData.idVehicle;
        form.querySelector('input[name="plate"]').value = vehicleData.plate;
        form.querySelector('input[name="serialNumber"]').value = vehicleData.serialNumber;
        form.querySelector('input[name="mileage"]').value = vehicleData.mileage;
        form.querySelector('input[name="acquisitionDate"]').value = vehicleData.acquisitionDate;
        form.querySelector('select[name="status"]').value = vehicleData.status;
        form.querySelector('select[name="fuelType"]').value = vehicleData.fuelType;
        form.querySelector('input[name="brand"]').value = vehicleData.brand;
        form.querySelector('input[name="model"]').value = vehicleData.model;
        form.querySelector('select[name="vehicleType"]').value = vehicleData.vehicleType;
    }
};


// ===============================
// CERRAR MODAL EDICIÓN
// ===============================
window.closeEditVehicleModal = function() {
    const modal = document.getElementById('editVehicleModal');
    if (modal) modal.classList.add('hidden');
};


// ===============================
// ACTUALIZAR VEHÍCULO
// ===============================
window.updateVehicle = async function (event) {
    event.preventDefault();

    const form = document.getElementById('editVehicleForm');
    const formData = new FormData(form);
    const vehicleId = formData.get('idVehicle');

    try {
        const response = await fetch(`/vehiculos/update/${vehicleId}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeEditVehicleModal();

            Swal.fire({
                title: "Actualizado",
                text: "El vehículo fue actualizado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1"
            });

            setTimeout(() => loadSection('vehiculos'), 500);

        } else {
            Swal.fire({
                title: "Error",
                text: "No se pudo actualizar el vehículo",
                icon: "error",
                confirmButtonColor: "#6366f1"
            });
        }

    } catch (error) {
        console.error("Error:", error);

        Swal.fire({
            title: "Error de conexión",
            text: "No se pudo conectar con el servidor",
            icon: "error",
            confirmButtonColor: "#6366f1"
        });
    }
};


// ===============================
// REGISTRAR VEHÍCULO
// ===============================
window.saveVehicle = async function (event) {
    event.preventDefault();

    const form = document.getElementById('vehicleForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/vehiculos', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeVehicleModal();

            Swal.fire({
                title: "¡Vehículo guardado!",
                text: "El registro se completó correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
            });

            setTimeout(() => loadSection('vehiculos'), 500);

        } else {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "No se pudo completar la operación.",
                confirmButtonColor: "#6366f1",
            });
        }

    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: "error",
            title: "Error de conexión",
            text: "No se pudo conectar con el servidor",
            confirmButtonColor: "#6366f1"
        });
    }
};


// ===============================
// BUSCADOR (IGNORA ACENTOS Y MAY/MIN)
// ===============================
window.searchVehicles = function (value) {
    const normalize = (str) =>
        str
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#vehiclesTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};