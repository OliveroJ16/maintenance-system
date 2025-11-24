// ============================================
// GESTIÓN DE TALLERES (usando CRUDManager)
// ============================================

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
    tableId: 'servicesTable',
    modalPrefix: 'service'
});

// Variable global para el taller actual
let currentWorkshopId = null;
let currentWorkshopName = '';

// Inicializar si estamos en vista de servicios
document.addEventListener('DOMContentLoaded', function() {
    const serviceSection = document.querySelector('[data-workshop-id]');
    if (serviceSection) {
        currentWorkshopId = serviceSection.getAttribute('data-workshop-id');
        currentWorkshopName = serviceSection.getAttribute('data-workshop-name');
    }
});

// Funciones para servicios
window.confirmDeleteService = (id) => servicesManager.confirmDelete(id);
window.closeServiceModal = () => servicesManager.closeModal();
window.closeEditServiceModal = () => servicesManager.closeEditModal();
window.searchServicesInModal = (value) => servicesManager.search(value);

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

// Guardar servicio (con workshopId)
window.saveService = async function(event) {
    event.preventDefault();

    const form = document.getElementById('serviceForm');
    const formData = new FormData(form);
    
    // Asegurar que workshopId esté en el FormData
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
                confirmButtonColor: "#6366f1"
            });
            setTimeout(() => window.location.reload(), 500);
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

// Actualizar servicio
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
                confirmButtonColor: "#6366f1"
            });
            setTimeout(() => window.location.reload(), 500);
        } else {
            servicesManager.showError("No se pudo actualizar el servicio");
        }
    } catch (error) {
        console.error("Error:", error);
        servicesManager.showConnectionError();
    }
};