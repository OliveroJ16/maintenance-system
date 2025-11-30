// users.js - Ahora con manejo de driver ID
const usersManager = new CRUDManager({
    entity: 'users',
    entityName: 'usuario',
    endpoint: '/users',
    tableId: 'usersTable',
    modalPrefix: 'user'
});

// Exponer funciones al scope global
window.confirmDelete = (id) => usersManager.confirmDelete(id);
window.openUserModal = () => usersManager.openModal();
window.closeUserModal = () => usersManager.closeModal();
window.saveUser = (e) => usersManager.save(e);
window.updateUser = (e) => usersManager.update(e);
window.closeEditUserModal = () => usersManager.closeEditModal();
window.searchUsers = (value) => usersManager.search(value);

window.editUser = function(button) {
    const userData = {
        idUser: button.getAttribute('data-id'),
        username: button.getAttribute('data-username'),
        firstName: button.getAttribute('data-firstname'),
        lastName: button.getAttribute('data-lastname'),
        role: button.getAttribute('data-role'),
        email: button.getAttribute('data-email')
    };
    
    usersManager.openEditModal(userData);
    
    // Seleccionar el driver en el select después de abrir el modal
    const driverId = button.getAttribute('data-driver-id');
    const driverSelect = document.querySelector('#editUserForm select[name="driver.idDriver"]');
    
    if (driverSelect && driverId) {
        driverSelect.value = driverId;
    } else if (driverSelect) {
        driverSelect.value = '';
    }
};