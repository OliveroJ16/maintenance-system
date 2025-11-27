window.switchTab = function (tabId) {
    const tabs = ['tab-mantenimientos', 'tab-tipos', 'tab-config'];

    tabs.forEach(tab => {
        const element = document.getElementById(tab);
        if (element) element.style.display = 'none';
    });

    const selectedTab = document.getElementById(tabId);
    if (selectedTab) selectedTab.style.display = 'block';

    const shortTabId = tabId.split('-')[1];

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('btn-primary');
        btn.classList.add('btn-secondary');
    });

    const activeBtn = document.getElementById(`tabBtn-${shortTabId}`);
    if (activeBtn) {
        activeBtn.classList.remove('btn-secondary');
        activeBtn.classList.add('btn-primary');
    }
};

const maintenanceManager = new CRUDManager({
    entity: 'maintenance',
    entityName: 'mantenimiento',
    endpoint: '/maintenance',
    tableId: 'maintenanceTable',
    modalPrefix: 'maintenance'
});

// Funciones para mantenimientos
window.openMaintenanceModal = () => maintenanceManager.openModal();
window.closeMaintenanceModal = () => maintenanceManager.closeModal();
window.saveMaintenanceRecord = (e) => maintenanceManager.save(e);
window.updateMaintenanceRecord = (e) => maintenanceManager.update(e);
window.closeEditMaintenanceModal = () => maintenanceManager.closeEditModal();
window.confirmDeleteMaintenance = (id) => maintenanceManager.confirmDelete(id);
window.searchMaintenance = (value) => maintenanceManager.search(value);

window.editMaintenanceRecord = function (button) {
    const data = {
        idMaintenance: button.getAttribute('data-id'),
        scheduledDate: button.getAttribute('data-scheduleddate'),
        scheduledKm: button.getAttribute('data-scheduledkm'),
        executionDate: button.getAttribute('data-executiondate'),
        executionKm: button.getAttribute('data-executionkm'),
        status: button.getAttribute('data-status'),
        idVehicle: button.getAttribute('data-vehicle'),
        idMaintenanceType: button.getAttribute('data-type'),
        idWorkshop: button.getAttribute('data-workshop')
    };
    maintenanceManager.openEditModal(data);
};



//Tipos de mantenimiento

const maintenanceTypeManager = new CRUDManager({
    entity: 'maintenance',
    entityName: 'tipo de mantenimiento',
    endpoint: '/maintenance-type',
    tableId: 'maintenanceTypeTable',
    modalPrefix: 'type'
});

// Funciones para tipos
window.openTypeModal = () => maintenanceTypeManager.openModal();
window.closeTypeModal = () => maintenanceTypeManager.closeModal();
window.saveType = (e) => maintenanceTypeManager.save(e);
window.updateType = (e) => maintenanceTypeManager.update(e);
window.closeEditTypeModal = () => maintenanceTypeManager.closeEditModal();
window.confirmDeleteType = (id) => maintenanceTypeManager.confirmDelete(id);
window.searchMaintenanceTypes = (value) => maintenanceTypeManager.search(value);

window.editType = function (button) {
    const data = {
        idMaintenanceType: button.getAttribute('data-id'),
        typeName: button.getAttribute('data-name'),
        category: button.getAttribute('data-category'),
        priority: button.getAttribute('data-priority'),
        description: button.getAttribute('data-description')
    };

    maintenanceTypeManager.openEditModal(data);
};

const maintenanceConfigManager = new CRUDManager({
    entity: 'maintenance',
    entityName: 'configuración',
    endpoint: '/maintenance-config',
    tableId: 'configTable',
    modalPrefix: 'config'
});

// Funciones para configuraciones
window.openConfigModal = () => maintenanceConfigManager.openModal();
window.closeConfigModal = () => maintenanceConfigManager.closeModal();
window.saveConfig = (e) => maintenanceConfigManager.save(e);
window.updateConfig = (e) => maintenanceConfigManager.update(e);
window.closeEditConfigModal = () => maintenanceConfigManager.closeEditModal();
window.confirmDeleteConfig = (id) => maintenanceConfigManager.confirmDelete(id);
window.searchConfig = (value) => maintenanceConfigManager.search(value);

window.editConfig = function (button) {
    const data = {
        idConfig: button.getAttribute('data-id'),
        'vehicle.idVehicle': button.getAttribute('data-vehicle'),
        'maintenanceType.idMaintenanceType': button.getAttribute('data-type'),
        frequencyKm: button.getAttribute('data-km'),
        frequencyMonths: button.getAttribute('data-months'),
        description: button.getAttribute('data-description')
    };
    maintenanceConfigManager.openEditModal(data);
};

// Función para cargar servicios del taller seleccionado
window.loadWorkshopServices = async function(workshopId) {
    const servicesContainer = document.getElementById('servicesContainer');
    const servicesList = document.getElementById('servicesList');
    
    if (!workshopId) {
        servicesContainer.style.display = 'none';
        servicesList.innerHTML = '';
        return;
    }
    
    try {
        const response = await fetch(`/workshop/${workshopId}/services`);
        const services = await response.json();
        
        if (services.length === 0) {
            servicesList.innerHTML = '<p style="color: #666;">Este taller no tiene servicios registrados</p>';
        } else {
            servicesList.innerHTML = services.map(service => `
                <div style="display: flex; align-items: center; padding: 8px; border-bottom: 1px solid #eee;">
                    <input type="checkbox" 
                           name="services" 
                           value="${service.idService}" 
                           id="service_${service.idService}"
                           style="margin-right: 10px;">
                    <label for="service_${service.idService}" style="flex: 1; margin: 0; cursor: pointer;">
                        <strong>${service.serviceName}</strong>
                        <br>
                        <small style="color: #666;">${service.description || 'Sin descripción'}</small>
                        <br>
                        <span style="color: #2563eb; font-weight: 600;">$${service.cost}</span>
                        ${service.durationMinutes ? ` - ${service.durationMinutes} min` : ''}
                    </label>
                </div>
            `).join('');
        }
        
        servicesContainer.style.display = 'block';
    } catch (error) {
        console.error('Error loading services:', error);
        servicesList.innerHTML = '<p style="color: red;">Error al cargar servicios</p>';
    }
};