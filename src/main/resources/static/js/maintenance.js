window.switchTab = function(tabId) {
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
    entity: 'mantenimientos',
    entityName: 'mantenimiento',
    endpoint: '/mantenimientos',
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

window.editMaintenanceRecord = function(button) {
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


const maintenanceTypeManager = new CRUDManager({
    entity: 'tipos-mantenimiento',
    entityName: 'tipo de mantenimiento',
    endpoint: '/tipos-mantenimiento',
    tableId: 'typeTable',
    modalPrefix: 'type'
});

// Funciones para tipos
window.openTypeModal = () => maintenanceTypeManager.openModal();
window.closeTypeModal = () => maintenanceTypeManager.closeModal();
window.saveType = (e) => maintenanceTypeManager.save(e);
window.updateType = (e) => maintenanceTypeManager.update(e);
window.closeEditTypeModal = () => maintenanceTypeManager.closeEditModal();
window.confirmDeleteType = (id) => maintenanceTypeManager.confirmDelete(id);

window.editType = function(button) {
    const data = {
        idMaintenanceType: button.getAttribute('data-id'),
        name: button.getAttribute('data-name'),
        category: button.getAttribute('data-category'),
        priority: button.getAttribute('data-priority')
    };
    maintenanceTypeManager.openEditModal(data);
};

const maintenanceConfigManager = new CRUDManager({
    entity: 'configuraciones-mantenimiento',
    entityName: 'configuración',
    endpoint: '/configuraciones-mantenimiento',
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

window.editConfig = function(button) {
    const data = {
        idConfig: button.getAttribute('data-id'),
        idVehicle: button.getAttribute('data-vehicle'),
        idMaintenanceType: button.getAttribute('data-type'),
        kmInterval: button.getAttribute('data-km'),
        monthInterval: button.getAttribute('data-months')
    };
    maintenanceConfigManager.openEditModal(data);
};