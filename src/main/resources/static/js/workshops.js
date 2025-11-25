const workshopsManager = new CRUDManager({
    entity: 'workshops',
    entityName: 'taller',
    endpoint: '/workshops',
    tableId: 'workshopsTable',
    modalPrefix: 'workshop'
});

// Funciones para talleres
window.confirmDeleteWorkshop = (id) => workshopsManager.confirmDelete(id);
window.openWorkshopModal = () => workshopsManager.openModal();
window.closeWorkshopModal = () => workshopsManager.closeModal();
window.saveWorkshop = (e) => workshopsManager.save(e);
window.updateWorkshop = (e) => workshopsManager.update(e);
window.closeEditWorkshopModal = () => workshopsManager.closeEditModal();
window.searchWorkshops = (value) => workshopsManager.search(value);

window.editWorkshop = function(button) {
    const workshopData = {
        idWorkshop: button.getAttribute('data-id'),
        workshopName: button.getAttribute('data-name'),
        address: button.getAttribute('data-address'),
        phone: button.getAttribute('data-phone'),
        email: button.getAttribute('data-email'),
        specialty: button.getAttribute('data-specialty'),
        status: button.getAttribute('data-status')
    };
    workshopsManager.openEditModal(workshopData);
};
// ============================================
// GESTIÓN DE SERVICIOS (usando CRUDManager)
// ============================================

const servicesManager = new CRUDManager({
    entity: 'services',
    entityName: 'servicio',
    endpoint: '/services',
    tableId: 'servicesModalTable',
    modalPrefix: 'service'
});

// Variable global para el taller actual
let currentWorkshopId = null;
let currentWorkshopName = '';

// ✅ ABRIR MODAL DE SERVICIOS Y CARGAR DINÁMICAMENTE
window.openServicesModal = async function(button) {
    currentWorkshopId = button.getAttribute('data-workshop-id');
    currentWorkshopName = button.getAttribute('data-workshop-name');
    
    // Mostrar modal
    const modal = document.getElementById('servicesManagementModal');
    modal.classList.remove('hidden');
    
    // Actualizar título
    document.getElementById('servicesModalTitle').textContent = `Servicios de ${currentWorkshopName}`;
    
    // Cargar servicios
    await loadServicesInManagementModal();
};

// ✅ CARGAR SERVICIOS SIN REDIRIGIR (fetch HTML del servidor)
async function loadServicesInManagementModal() {
    const content = document.getElementById('servicesManagementContent');
    content.innerHTML = '<div style="text-align: center; padding: 40px;"><p>Cargando servicios...</p></div>';
    
    try {
        // Hacer fetch al endpoint que retorna solo la tabla HTML
        const response = await fetch(`/workshops/${currentWorkshopId}/services`);
        const html = await response.text();
        
        content.innerHTML = html;
        
    } catch (error) {
        console.error('Error cargando servicios:', error);
        content.innerHTML = '<div style="text-align: center; padding: 40px; color: red;"><p>Error al cargar servicios</p></div>';
    }
}

// ✅ CERRAR MODAL DE GESTIÓN DE SERVICIOS
window.closeServicesManagementModal = function() {
    document.getElementById('servicesManagementModal').classList.add('hidden');
};

// Funciones para servicios
window.confirmDeleteService = async (id) => {
    Swal.fire({
        title: '¿Eliminar servicio?',
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
                const response = await fetch(`/services/delete/${id}`);
                if (response.ok) {
                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El servicio fue eliminado correctamente",
                        icon: "success",
                        confirmButtonColor: "#6366f1",
                        timer: 1500
                    });
                    // Recargar solo la tabla del modal
                    await loadServicesInManagementModal();
                }
            } catch (error) {
                console.error('Error:', error);
            }
        }
    });
};

window.closeServiceModal = () => servicesManager.closeModal();
window.closeEditServiceModal = () => servicesManager.closeEditModal();

// Búsqueda en el modal de gestión
window.searchServicesInManagementModal = function(value) {
    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#servicesModalTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};

// Abrir modal de crear servicio (con workshopId)
window.openServiceModal = function() {
    servicesManager.openModal();
    
    
    // Agregar workshopId al formulario
    const form = document.getElementById('serviceForm');
    let hiddenInput = form.querySelector('input[name="workshopId"]');
    if (!hiddenInput) {
        hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'workshopId';
        form.appendChild(hiddenInput);
    }
    hiddenInput.value = currentWorkshopId;
    
    // Actualizar título
    document.getElementById('serviceModalTitle').textContent = `Nuevo Servicio - ${currentWorkshopName}`;
};

// ✅ GUARDAR SERVICIO Y RECARGAR SOLO LA TABLA DEL MODAL
window.saveService = async function(event) {
    event.preventDefault();

    const form = document.getElementById('serviceForm');
    const formData = new FormData(form);
    
    if (!formData.get('workshopId') && currentWorkshopId) {
        formData.append('workshopId', currentWorkshopId);
    }

    try {
        const response = await fetch('/services', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            servicesManager.closeModal();
            Swal.fire({
                title: "¡Servicio guardado!",
                text: "El servicio fue agregado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
                timer: 1500
            });
            
            // ✅ Recargar solo la tabla dentro del modal
            await loadServicesInManagementModal();
        } else {
            servicesManager.showError("No se pudo guardar el servicio");
        }
    } catch (error) {
        console.error('Error:', error);
        servicesManager.showConnectionError();
    }
};

// Editar servicio
window.editService = function(button) {
    const serviceData = {
        idService: button.getAttribute('data-id'),
        serviceName: button.getAttribute('data-name'),
        description: button.getAttribute('data-description'),
        cost: button.getAttribute('data-cost'),
        durationMinutes: button.getAttribute('data-duration'),
        status: button.getAttribute('data-status')
    };
    servicesManager.openEditModal(serviceData);
};

// ✅ ACTUALIZAR SERVICIO Y RECARGAR SOLO LA TABLA DEL MODAL
window.updateService = async function(event) {
    event.preventDefault();

    const form = document.getElementById('editServiceForm');
    const formData = new FormData(form);
    const serviceId = formData.get('idService');

    try {
        const response = await fetch(`/services/update/${serviceId}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            servicesManager.closeEditModal();
            Swal.fire({
                title: "Actualizado",
                text: "El servicio fue actualizado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
                timer: 1500
            });
            
            // ✅ Recargar solo la tabla dentro del modal
            await loadServicesInManagementModal();
        } else {
            servicesManager.showError("No se pudo actualizar el servicio");
        }
    } catch (error) {
        console.error("Error:", error);
        servicesManager.showConnectionError();
    }
};