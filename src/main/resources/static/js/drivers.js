window.confirmDeleteDriver = function (id) {
    Swal.fire({
        title: "¿Eliminar chofer?",
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
                const response = await fetch(`/choferes/delete/${id}`);

                if (response.ok) {
                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El chofer fue eliminado correctamente",
                        icon: "success",
                        confirmButtonColor: "#6366f1",
                    });

                    setTimeout(() => {
                        loadSection('choferes');
                    }, 500);

                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "No se pudo eliminar el chofer.",
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


// =============================================================
// MODAL PARA REGISTRO
// =============================================================
window.openDriverModal = function () {
    const modal = document.getElementById('driverModal');
    if (modal) {
        modal.classList.remove('hidden');

        const title = document.getElementById('driverModalTitle');
        const form = document.getElementById('driverForm');

        if (title) title.textContent = 'Nuevo Chofer';
        if (form) form.reset();

    } else {
        console.error('Modal de choferes no encontrado');
    }
}

window.closeDriverModal = function () {
    const modal = document.getElementById('driverModal');
    if (modal) modal.classList.add('hidden');
};


window.editDriver = function (button) {
    const driverData = {
        idDriver: button.getAttribute('data-id'),
        firstName: button.getAttribute('data-firstname'),
        lastName: button.getAttribute('data-lastname'),
        idCard: button.getAttribute('data-idcard'),
        phone: button.getAttribute('data-phone'),
        email: button.getAttribute('data-email'),
        licenseCategory: button.getAttribute('data-license'),
        licenseExpirationDate: button.getAttribute('data-expdate'),
        status: button.getAttribute('data-status')
    };

    openEditDriverModal(driverData);
};

window.openEditDriverModal = function (data) {
    const modal = document.getElementById('editDriverModal');
    if (!modal) return console.error('Modal de edición no encontrado');

    modal.classList.remove('hidden');

    const form = document.getElementById('editDriverForm');

    form.querySelector('input[name="idDriver"]').value = data.idDriver;
    form.querySelector('input[name="firstName"]').value = data.firstName;
    form.querySelector('input[name="lastName"]').value = data.lastName;
    form.querySelector('input[name="idCard"]').value = data.idCard;
    form.querySelector('input[name="phone"]').value = data.phone;
    form.querySelector('input[name="email"]').value = data.email;

    form.querySelector('select[name="licenseCategory"]').value = data.licenseCategory;

    form.querySelector('input[name="licenseExpirationDate"]').value = data.licenseExpirationDate;

    form.querySelector('select[name="status"]').value = data.status;
};


window.closeEditDriverModal = function () {
    const modal = document.getElementById('editDriverModal');
    if (modal) modal.classList.add('hidden');
};


window.updateDriver = async function (event) {
    event.preventDefault();

    const form = document.getElementById('editDriverForm');
    const formData = new FormData(form);
    const driverId = formData.get('idDriver');

    try {
        const response = await fetch(`/choferes/update/${driverId}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeEditDriverModal();

            Swal.fire({
                title: "Actualizado",
                text: "El chofer fue actualizado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1"
            });

            setTimeout(() => loadSection('choferes'), 500);

        } else {
            Swal.fire({
                title: "Error",
                text: "No se pudo actualizar el chofer",
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


window.saveDriver = async function (event) {
    event.preventDefault();

    const form = document.getElementById('driverForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/choferes', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeDriverModal();

            Swal.fire({
                title: "¡Chofer guardado!",
                text: "El registro se completó correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
            });

            setTimeout(() => loadSection('choferes'), 500);

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


window.searchDrivers = function (value) {
    const normalize = (str) =>
        str
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#driversTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};
