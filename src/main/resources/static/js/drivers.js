const driversManager = new CRUDManager({
    entity: 'drivers',
    entityName: 'chofer',
    endpoint: '/drivers',
    tableId: 'driversTable',
    modalPrefix: 'driver'
});

window.confirmDeleteDriver = (id) => driversManager.confirmDelete(id);
window.openDriverModal = () => driversManager.openModal();
window.closeDriverModal = () => driversManager.closeModal();
window.saveDriver = (e) => driversManager.save(e);
window.updateDriver = (e) => driversManager.update(e);
window.closeEditDriverModal = () => driversManager.closeEditModal();
window.searchDrivers = (value) => driversManager.search(value);

window.editDriver = function(button) {
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
    driversManager.openEditModal(driverData);
};