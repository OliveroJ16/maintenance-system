// ============================================
// SUPERVISOR.JS - Lógica para panel supervisor
// ============================================

// ==========================================
// NAVEGACIÓN Y CARGA DE SECCIONES
// ==========================================

const contentArea = document.getElementById("contentArea");
const menuItems = document.querySelectorAll(".menu-item");

async function loadSection(section) {
    try {
        const response = await fetch(`/supervisor/${section}`);
        const html = await response.text();
        contentArea.innerHTML = html;
    } catch (error) {
        contentArea.innerHTML = "<p>Error al cargar la sección.</p>";
        console.error("Error:", error);
    }
}

// Event listeners para los items del menú
menuItems.forEach(item => {
    item.addEventListener("click", () => {
        menuItems.forEach(i => i.classList.remove("active"));
        item.classList.add("active");
        const section = item.getAttribute("data-section");
        loadSection(section);
    });
});

// ==========================================
// REGISTRAR USO DE VEHÍCULOS
// ==========================================

window.openRegisterUsageModal = function(button) {
    const vehicleId = button.getAttribute('data-id');
    const plate = button.getAttribute('data-plate');
    const currentKm = button.getAttribute('data-current-km');
    
    document.getElementById('usageVehicleId').value = vehicleId;
    document.getElementById('usagePlate').value = plate;
    document.getElementById('currentKm').value = currentKm;
    document.getElementById('newMileage').min = parseInt(currentKm) + 1;
    document.getElementById('newMileage').value = '';
    
    document.getElementById('registerUsageModal').classList.remove('hidden');
};

window.closeRegisterUsageModal = function() {
    document.getElementById('registerUsageModal').classList.add('hidden');
};

window.saveVehicleUsage = async function(event) {
    event.preventDefault();
    
    const form = document.getElementById('registerUsageForm');
    const formData = new FormData(form);
    const currentKm = parseInt(document.getElementById('currentKm').value);
    const newKm = parseInt(formData.get('newMileage'));
    
    // Validación
    if (newKm <= currentKm) {
        Swal.fire({
            icon: 'error',
            title: 'Error de validación',
            text: `El nuevo kilometraje (${newKm} km) debe ser mayor al actual (${currentKm} km)`,
            confirmButtonColor: '#6366f1'
        });
        return;
    }
    
    try {
        const response = await fetch('/supervisor/vehicles/register-usage', {
            method: 'POST',
            body: formData
        });
        
        const result = await response.text();
        
        if (response.ok && result === 'success') {
            closeRegisterUsageModal();
            
            Swal.fire({
                title: '¡Registro exitoso!',
                text: 'El kilometraje del vehículo ha sido actualizado',
                icon: 'success',
                confirmButtonColor: '#6366f1',
                timer: 2000
            });
            
            // Recargar la sección
            setTimeout(() => {
                loadSection('vehicles');
            }, 500);
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo registrar el uso del vehículo',
                confirmButtonColor: '#6366f1'
            });
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error de conexión',
            text: 'No se pudo contactar con el servidor',
            confirmButtonColor: '#6366f1'
        });
    }
};

window.searchSupervisorVehicles = function(value) {
    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#supervisorVehiclesTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};

// ==========================================
// REPORTAR DAÑOS (MANTENIMIENTOS CORRECTIVOS)
// ==========================================

window.openReportDamageModal = function() {
    const modal = document.getElementById('reportDamageModal');
    modal.classList.remove('hidden');
    
    const form = document.getElementById('reportDamageForm');
    form.reset();
    
    // Establecer fecha actual
    const today = new Date().toISOString().split('T')[0];
    const dateInput = form.querySelector('input[name="scheduledDate"]');
    if (dateInput) {
        dateInput.value = today;
    }
};

window.closeReportDamageModal = function() {
    document.getElementById('reportDamageModal').classList.add('hidden');
};

window.saveReportDamage = async function(event) {
    event.preventDefault();
    
    const form = document.getElementById('reportDamageForm');
    const formData = new FormData(form);
    
    // Validación básica
    const description = formData.get('description');
    if (!description || description.trim().length < 10) {
        Swal.fire({
            icon: 'warning',
            title: 'Descripción insuficiente',
            text: 'Por favor, proporcione una descripción detallada del daño (mínimo 10 caracteres)',
            confirmButtonColor: '#6366f1'
        });
        return;
    }
    
    try {
        const response = await fetch('/supervisor/maintenance/report-damage', {
            method: 'POST',
            body: formData
        });
        
        const result = await response.text();
        
        if (response.ok && result === 'success') {
            closeReportDamageModal();
            
            Swal.fire({
                title: '¡Daño reportado!',
                text: 'El reporte de mantenimiento correctivo ha sido creado exitosamente',
                icon: 'success',
                confirmButtonColor: '#6366f1',
                timer: 2000
            });
            
            // Recargar la sección
            setTimeout(() => {
                loadSection('maintenance');
            }, 500);
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo crear el reporte de daño',
                confirmButtonColor: '#6366f1'
            });
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error de conexión',
            text: 'No se pudo contactar con el servidor',
            confirmButtonColor: '#6366f1'
        });
    }
};

window.searchSupervisorMaintenances = function(value) {
    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#supervisorMaintenancesTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};

// ==========================================
// CERRAR MODALES AL HACER CLIC FUERA
// ==========================================

window.addEventListener("click", function(event) {
    const modals = [
        'registerUsageModal',
        'reportDamageModal'
    ];
    
    modals.forEach(modalId => {
        const modal = document.getElementById(modalId);
        if (modal && event.target === modal) {
            modal.classList.add('hidden');
        }
    });
});

// ==========================================
// CARGAR SECCIÓN INICIAL (DASHBOARD)
// ==========================================

loadSection("dashboard-section");