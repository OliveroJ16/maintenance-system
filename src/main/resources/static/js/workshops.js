/* -----------------------------------------
   ELIMINAR WORKSHOP
----------------------------------------- */
window.confirmDeleteWorkshop = function (id) {
    Swal.fire({
        title: "¿Eliminar taller?",
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
                const response = await fetch(`/workshops/delete/${id}`);

                if (response.ok) {
                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El taller fue eliminado correctamente",
                        icon: "success",
                        confirmButtonColor: "#6366f1",
                    });

                    setTimeout(() => {
                        loadSection('workshops');
                    }, 500);

                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "No se pudo eliminar el taller.",
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

/* -----------------------------------------
   ABRIR MODAL DE REGISTRO
----------------------------------------- */
window.openWorkshopModal = function () {
    const modal = document.getElementById('workshopModal');
    if (modal) {
        modal.classList.remove('hidden');
        const form = document.getElementById('workshopForm');
        form.reset();
        document.getElementById('modalTitleWorkshop').textContent = 'Nuevo Taller';
    }
};

/* -----------------------------------------
   CERRAR MODAL DE REGISTRO
----------------------------------------- */
window.closeWorkshopModal = function () {
    const modal = document.getElementById('workshopModal');
    if (modal) modal.classList.add('hidden');
};

/* -----------------------------------------
   GUARDAR WORKSHOP
----------------------------------------- */
window.saveWorkshop = async function (event) {
    event.preventDefault();

    const form = document.getElementById('workshopForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/workshops', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeWorkshopModal();

            Swal.fire({
                title: "¡Taller guardado!",
                text: "El registro se completó correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
            });

            setTimeout(() => loadSection('workshops'), 500);

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

/* -----------------------------------------
   ABRIR MODAL DE EDICIÓN
----------------------------------------- */
window.editWorkshop = function (button) {

    const workshopData = {
        idWorkshop: button.getAttribute('data-id'),
        workshopName: button.getAttribute('data-name'),
        address: button.getAttribute('data-address'),
        phone: button.getAttribute('data-phone'),
        email: button.getAttribute('data-email'),
        specialty: button.getAttribute('data-specialty'),
        status: button.getAttribute('data-status')
    };

    openEditWorkshopModal(workshopData);
};


window.openEditWorkshopModal = function (data) {
    const modal = document.getElementById('editWorkshopModal');
    if (!modal) {
        console.error('Modal de edición no encontrado');
        return;
    }

    modal.classList.remove('hidden');

    const form = document.getElementById('editWorkshopForm');

    form.querySelector('input[name="idWorkshop"]').value = data.idWorkshop;
    form.querySelector('input[name="workshopName"]').value = data.workshopName;
    form.querySelector('input[name="address"]').value = data.address || '';
    form.querySelector('input[name="phone"]').value = data.phone;
    form.querySelector('input[name="email"]').value = data.email || '';
    form.querySelector('input[name="specialty"]').value = data.specialty || '';
    form.querySelector('select[name="status"]').value = data.status;
};

/* -----------------------------------------
   CERRAR MODAL DE EDICIÓN
----------------------------------------- */
window.closeEditWorkshopModal = function () {
    const modal = document.getElementById('editWorkshopModal');
    if (modal) modal.classList.add('hidden');
};

/* -----------------------------------------
   ACTUALIZAR WORKSHOP
----------------------------------------- */
window.updateWorkshop = async function (event) {
    event.preventDefault();

    const form = document.getElementById('editWorkshopForm');
    const formData = new FormData(form);
    const id = formData.get('idWorkshop');

    try {
        const response = await fetch(`/workshops/update/${id}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeEditWorkshopModal();

            Swal.fire({
                title: "Actualizado",
                text: "El taller fue actualizado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1"
            });

            setTimeout(() => loadSection('workshops'), 500);

        } else {
            Swal.fire({
                title: "Error",
                text: "No se pudo actualizar el taller",
                icon: "error",
                confirmButtonColor: "#6366f1"
            });
        }

    } catch (error) {
        console.error(error);
        Swal.fire({
            title: "Error",
            text: "No se pudo conectar con el servidor",
            icon: "error",
            confirmButtonColor: "#6366f1"
        });
    }
};

/* -----------------------------------------
   BUSCAR TALLERES
----------------------------------------- */
window.searchWorkshops = function (value) {

    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#workshopsTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};
