// users.js - Ahora solo configuración
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
        email: button.getAttribute('data-email'),
        driver: button.getAttribute('data-driver')
    };
    usersManager.openEditModal(userData);
};
