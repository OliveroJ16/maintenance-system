// workshops.js - Configuración usando CRUDManager
const workshopsManager = new CRUDManager({
    entity: 'workshops',
    entityName: 'taller',
    endpoint: '/workshops',
    tableId: 'workshopsTable',
    modalPrefix: 'workshop'
});

// Exponer funciones al scope global
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