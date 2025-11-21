window.confirmDelete = function (id) {
    Swal.fire({
        title: "¿Eliminar usuario?",
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
                
                const response = await fetch(`/users/delete/${id}`);

                if (response.ok) {
                    Swal.fire({
                        title: "¡Eliminado!",
                        text: "El usuario fue eliminado correctamente",
                        icon: "success",
                        confirmButtonColor: "#6366f1",
                    });

                    setTimeout(() => {
                        loadSection('users');
                    }, 500);

                } else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "No se pudo eliminar el usuario.",
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
}


//Modal para registro
window.openUserModal = function () {
    const modal = document.getElementById('userModal');
    if (modal) {
        modal.classList.remove('hidden');
        const title = document.getElementById('modalTitle');
        const form = document.getElementById('userForm');
        if (title) title.textContent = 'Nuevo Usuario';
        if (form) form.reset();
    } else {
        console.error('Modal de usuarios no encontrado');
    }
}

//Cerrar modal de registro
window.closeUserModal = function () {
    const modal = document.getElementById('userModal');
    if (modal) modal.classList.add('hidden');
}

// Función para editar usuario
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

    openEditUserModal(userData);
}

//Modal para edición
window.openEditUserModal = function(userData) {
    const modal = document.getElementById('editUserModal');
    if (!modal) {
        console.error('Modal de edición no encontrado');
        return;
    }

    modal.classList.remove('hidden');

    // Rellenar el formulario con los datos del usuario
    const form = document.getElementById('editUserForm');
    if (form) {
        form.querySelector('input[name="idUser"]').value = userData.idUser;
        form.querySelector('input[name="username"]').value = userData.username;
        form.querySelector('input[name="firstName"]').value = userData.firstName;
        form.querySelector('input[name="lastName"]').value = userData.lastName;
        form.querySelector('select[name="role"]').value = userData.role;
        form.querySelector('input[name="email"]').value = userData.email;
        form.querySelector('input[name="driver"]').value = userData.driver || '';
    }
}

//Cerrar modal de edición
window.closeEditUserModal = function() {
    const modal = document.getElementById('editUserModal');
    if (modal) modal.classList.add('hidden');
}

//Actualizar usuario
window.updateUser = async function (event) {
    event.preventDefault();

    const form = document.getElementById('editUserForm');
    const formData = new FormData(form);
    const userId = formData.get('idUser');

    try {
        const response = await fetch(`/users/update/${userId}`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeEditUserModal();

            Swal.fire({
                title: "Actualizado",
                text: "El usuario fue actualizado correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1"
            });

            setTimeout(() => loadSection('users'), 500);

        } else {
            Swal.fire({
                title: "Error",
                text: "No se pudo actualizar el usuario",
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


//Guardar usuario
window.saveUser = async function (event) {
    event.preventDefault();

    const form = document.getElementById('userForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/users', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            closeUserModal();

            Swal.fire({
                title: "¡Usuario guardado!",
                text: "El registro se completó correctamente",
                icon: "success",
                confirmButtonColor: "#6366f1",
                draggable: true
            });

            setTimeout(() => {
                loadSection('users');
            }, 500);

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

//Buscar usuario
window.searchUsers = function (value) {
    const normalize = (str) =>
        str
            .normalize("NFD") 
            .replace(/[\u0300-\u036f]/g, "")
            .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#usersTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
}

