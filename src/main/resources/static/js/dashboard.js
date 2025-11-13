// ========================================
// CARGA DE SECCIONES
// ========================================
const contentArea = document.getElementById("contentArea");
const menuItems = document.querySelectorAll(".menu-item");

async function loadSection(section) {
    try {
        const response = await fetch(`${section}`);
        const html = await response.text();
        contentArea.innerHTML = html;

        setTimeout(() => {
            const pageTitle = document.getElementById("pageTitle");
            if (pageTitle) {
                pageTitle.textContent = section.charAt(0).toUpperCase() + section.slice(1);
            }
        }, 0);
    } catch (error) {
        contentArea.innerHTML = "<p>Error al cargar la sección.</p>";
        console.error("Error:", error);
    }
}

menuItems.forEach(item => {
    item.addEventListener("click", () => {
        menuItems.forEach(i => i.classList.remove("active"));
        item.classList.add("active");
        const section = item.getAttribute("data-section");
        loadSection(section);
    });
});

// ========================================
// FUNCIÓN MEJORADA PARA MOSTRAR MENSAJES
// ========================================
function mostrarMensaje(texto, esError = false, subtexto = null) {
    // Crear overlay de fondo
    const overlay = document.createElement("div");
    overlay.className = "toast-overlay";

    // Crear el elemento del mensaje
    const toast = document.createElement("div");
    toast.className = `toast-message ${esError ? "error" : "success"}`;

    // Agregar icono según el tipo
    const icon = esError
        ? `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
             <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                   d="M6 18L18 6M6 6l12 12"/>
           </svg>`
        : `<svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
             <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                   d="M5 13l4 4L19 7"/>
           </svg>`;

    // Construir el HTML del toast
    const subtextoHTML = subtexto ? `<div class="toast-subtext">${subtexto}</div>` : '';

    toast.innerHTML = `
        <div class="toast-icon">${icon}</div>
        <div class="toast-text">${texto}</div>
        ${subtextoHTML}
        <div class="toast-progress"></div>
    `;

    // Agregar toast al overlay
    overlay.appendChild(toast);

    // Agregar al body
    document.body.appendChild(overlay);

    // Hacer que desaparezca después de 3 segundos
    setTimeout(() => {
        overlay.classList.add("fade-out");
        // Eliminar del DOM después de la animación
        setTimeout(() => overlay.remove(), 300);
    }, 3000);

    // Cerrar al hacer clic en el overlay (opcional)
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
            overlay.classList.add("fade-out");
            setTimeout(() => overlay.remove(), 300);
        }
    });
}

// ========================================
// USUARIOS
// ========================================
window.openUserModal = function() {
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

window.closeUserModal = function() {
    const modal = document.getElementById('userModal');
    if (modal) modal.classList.add('hidden');
}

window.editUser = function(id) {
    openUserModal();
    setTimeout(() => {
        const title = document.getElementById('modalTitle');
        if (title) title.textContent = 'Editar Usuario';
    }, 10);
}

window.deleteUser = function(id) {
    if (confirm('¿Está seguro de eliminar este usuario?')) {
        alert('Usuario eliminado (funcionalidad pendiente)');
    }
}

window.saveUser = async function(event) {
    event.preventDefault();

    const form = document.getElementById('userForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/usuarios', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            // Cerrar modal
            closeUserModal();

            // Mostrar mensaje de éxito espectacular
            mostrarMensaje("¡Usuario guardado!", false, "Los cambios se han aplicado correctamente");

            // Recargar la sección de usuarios sin recargar toda la página
            setTimeout(() => {
                loadSection('usuarios');
            }, 500);
        } else {
            mostrarMensaje("Error al guardar", true, "No se pudo completar la operación");
        }
    } catch (error) {
        console.error('Error:', error);
        mostrarMensaje("Error de conexión", true, "No se pudo conectar con el servidor");
    }
};

window.searchUsers = function(value) {
    const filter = value.toLowerCase();
    const rows = document.querySelectorAll('#usersTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// ========================================
// VEHÍCULOS
// ========================================
window.openVehicleModal = function() {
    console.log('=== INICIANDO APERTURA DE MODAL DE VEHÍCULOS ===');

    const modal = document.getElementById('vehicleModal');
    console.log('Modal vehicleModal:', modal);

    if (!modal) {
        console.error('❌ Modal vehicleModal NO ENCONTRADO en el DOM');
        console.log('Elementos con clase modal:', document.querySelectorAll('.modal'));
        console.log('Todos los IDs en el documento:',
            Array.from(document.querySelectorAll('[id]')).map(el => el.id)
        );
        return;
    }

    console.log('✅ Modal encontrado, removiendo clase hidden');
    modal.classList.remove('hidden');

    const title = document.getElementById('vehicleModalTitle');
    const form = document.getElementById('vehicleForm');

    console.log('Título del modal:', title);
    console.log('Formulario:', form);

    if (title) {
        title.textContent = 'Nuevo Vehículo';
        console.log('✅ Título actualizado');
    }

    if (form) {
        form.reset();
        console.log('✅ Formulario reseteado');
    }

    console.log('=== FIN APERTURA MODAL ===');
}

window.closeVehicleModal = function() {
    const modal = document.getElementById('vehicleModal');
    if (modal) modal.classList.add('hidden');
}

window.editVehicle = function(id) {
    openVehicleModal();
    setTimeout(() => {
        const title = document.getElementById('vehicleModalTitle');
        if (title) title.textContent = 'Editar Vehículo';
    }, 10);
}

window.deleteVehicle = function(id) {
    if (confirm('¿Está seguro de eliminar este vehículo?')) {
        alert('Vehículo eliminado (funcionalidad pendiente)');
    }
}

window.saveVehicle = function(event) {
    event.preventDefault();
    alert('Vehículo guardado (funcionalidad pendiente)');
    closeVehicleModal();
}

window.searchVehicles = function(value) {
    const filter = value.toLowerCase();
    const rows = document.querySelectorAll('#vehiclesTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// ========================================
// MANTENIMIENTOS
// ========================================
window.openMaintenanceModal = function() {
    console.log('=== INICIANDO APERTURA DE MODAL DE MANTENIMIENTOS ===');

    const modal = document.getElementById('maintenanceModal');
    console.log('Modal maintenanceModal:', modal);

    if (!modal) {
        console.error('❌ Modal maintenanceModal NO ENCONTRADO en el DOM');
        console.log('Elementos con clase modal:', document.querySelectorAll('.modal'));
        console.log('Todos los IDs en el documento:',
            Array.from(document.querySelectorAll('[id]')).map(el => el.id)
        );
        return;
    }

    console.log('✅ Modal encontrado, removiendo clase hidden');
    modal.classList.remove('hidden');

    const title = document.getElementById('maintenanceModalTitle');
    const form = document.getElementById('maintenanceForm');

    console.log('Título del modal:', title);
    console.log('Formulario:', form);

    if (title) {
        title.textContent = 'Nuevo Mantenimiento';
        console.log('✅ Título actualizado');
    }

    if (form) {
        form.reset();
        console.log('✅ Formulario reseteado');
    }

    console.log('=== FIN APERTURA MODAL ===');
}

window.closeMaintenanceModal = function() {
    const modal = document.getElementById('maintenanceModal');
    if (modal) modal.classList.add('hidden');
}

window.editMaintenance = function(id) {
    openMaintenanceModal();
    setTimeout(() => {
        const title = document.getElementById('maintenanceModalTitle');
        if (title) title.textContent = 'Editar Mantenimiento';
    }, 10);
}

window.deleteMaintenance = function(id) {
    if (confirm('¿Está seguro de eliminar este mantenimiento?')) {
        alert('Mantenimiento eliminado (funcionalidad pendiente)');
    }
}

window.saveMaintenance = function(event) {
    event.preventDefault();
    alert('Mantenimiento guardado (funcionalidad pendiente)');
    closeMaintenanceModal();
}

window.searchMaintenance = function(value) {
    const filter = value.toLowerCase();
    const rows = document.querySelectorAll('#maintenanceTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// ========================================
// TALLERES
// ========================================
window.openWorkshopModal = function() {
    const modal = document.getElementById('workshopModal');
    if (modal) {
        modal.classList.remove('hidden');
        const title = document.getElementById('modalTitle');
        const form = document.getElementById('workshopForm');
        if (title) title.textContent = 'Nuevo Taller';
        if (form) form.reset();
    } else {
        console.error('Modal de talleres no encontrado');
    }
}

window.closeWorkshopModal = function() {
    const modal = document.getElementById('workshopModal');
    if (modal) modal.classList.add('hidden');
}

window.editWorkshop = function(id) {
    openWorkshopModal();
    setTimeout(() => {
        const title = document.getElementById('modalTitle');
        if (title) title.textContent = 'Editar Taller';
    }, 10);
}

window.deleteWorkshop = function(id) {
    if (confirm('¿Está seguro de eliminar este taller?')) {
        alert('Taller eliminado (funcionalidad pendiente)');
    }
}

window.saveWorkshop = function(event) {
    event.preventDefault();
    alert('Taller guardado (funcionalidad pendiente)');
    closeWorkshopModal();
}

window.searchWorkshops = function(value) {
    const filter = value.toLowerCase();
    const rows = document.querySelectorAll('#workshopsTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// ========================================
// ALERTAS
// ========================================
window.markAlertRead = function(id) {
    if (confirm('¿Marcar esta alerta como atendida?')) {
        alert('Alerta marcada como atendida (funcionalidad pendiente)');
    }
}

// ========================================
// CHOFERES
// ========================================
window.openDriverModal = function() {
    const modal = document.getElementById('driverModal');
    if (modal) {
        modal.classList.remove('hidden');
        const title = document.getElementById('modalTitle');
        const form = document.getElementById('driverForm');
        if (title) title.textContent = 'Nuevo Chofer';
        if (form) form.reset();
    } else {
        console.error('Modal de choferes no encontrado');
    }
}

window.closeDriverModal = function() {
    const modal = document.getElementById('driverModal');
    if (modal) modal.classList.add('hidden');
}

window.editDriver = function(id) {
    openDriverModal();
    setTimeout(() => {
        const title = document.getElementById('modalTitle');
        if (title) title.textContent = 'Editar Chofer';
    }, 10);
}

window.deleteDriver = function(id) {
    if (confirm('¿Está seguro de eliminar este chofer?')) {
        alert('Chofer eliminado (funcionalidad pendiente)');
    }
}

window.saveDriver = function(event) {
    event.preventDefault();
    alert('Chofer guardado (funcionalidad pendiente)');
    closeDriverModal();
}

window.searchDrivers = function(value) {
    const filter = value.toLowerCase();
    const rows = document.querySelectorAll('#driversTable tbody tr');

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// ========================================
// CERRAR MODAL AL HACER CLIC FUERA
// ========================================
window.addEventListener("click", function(event) {
    const modals = [
        'userModal',
        'vehicleModal',
        'maintenanceModal',
        'workshopModal',
        'driverModal'
    ];

    modals.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (modal && event.target === modal) {
            modal.classList.add('hidden');
        }
    });
});

// ========================================
// CARGA INICIAL
// ========================================
loadSection("inicio");