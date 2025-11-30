// alerts.js - Gestión completa de alertas

// ============================================
// MARCAR ALERTA COMO VISTA
// ============================================
window.markAlertAsViewed = async function(id) {
    try {
        const response = await fetch(`/alerts/mark-viewed/${id}`, {
            method: 'POST'
        });

        if (response.ok) {
            Swal.fire({
                title: "Marcada como vista",
                text: "La alerta ha sido marcada como vista",
                icon: "success",
                confirmButtonColor: "#6366f1",
                timer: 1500,
                showConfirmButton: false
            });

            setTimeout(() => {
                if (typeof loadSection === 'function') {
                    loadSection('alerts');
                } else {
                    window.location.reload();
                }
            }, 500);
        } else {
            throw new Error('Error al marcar como vista');
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: "error",
            title: "Error",
            text: "No se pudo marcar la alerta como vista",
            confirmButtonColor: "#6366f1"
        });
    }
};

// ============================================
// MARCAR TODAS LAS ALERTAS COMO VISTAS
// ============================================
window.markAllAsViewed = async function() {
    Swal.fire({
        title: '¿Marcar todas como vistas?',
        text: "Todas las alertas se marcarán como revisadas",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#6366f1",
        cancelButtonColor: "#6c757d",
        confirmButtonText: "Sí, marcar todas",
        cancelButtonText: "Cancelar"
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch('/alerts/mark-all-viewed', {
                    method: 'POST'
                });

                if (response.ok) {
                    Swal.fire({
                        title: "¡Actualizado!",
                        text: "Todas las alertas han sido marcadas como vistas",
                        icon: "success",
                        confirmButtonColor: "#6366f1"
                    });

                    setTimeout(() => {
                        if (typeof loadSection === 'function') {
                            loadSection('alerts');
                        } else {
                            window.location.reload();
                        }
                    }, 1000);
                } else {
                    throw new Error('Error al actualizar');
                }
            } catch (error) {
                console.error('Error:', error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: "No se pudieron actualizar las alertas",
                    confirmButtonColor: "#6366f1"
                });
            }
        }
    });
};

// ============================================
// ABRIR MODAL PARA ATENDER ALERTA
// ============================================
window.openAttendModal = function(button) {
    const alertId = button.getAttribute('data-alert-id');
    const vehiclePlate = button.getAttribute('data-vehicle-plate');
    const vehicleKm = button.getAttribute('data-vehicle-km');

    document.getElementById('attendAlertId').value = alertId;
    document.getElementById('attendVehiclePlate').value = vehiclePlate;
    document.getElementById('attendExecutionKm').value = vehicleKm;
    
    // Mostrar hint con el km actual
    document.getElementById('kmHint').textContent = `Kilometraje actual del vehículo: ${vehicleKm} km`;

    // Fecha actual por defecto
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('attendExecutionDate').value = today;

    document.getElementById('attendAlertModal').classList.remove('hidden');
};

// Función alternativa que recibe parámetros directamente (para Thymeleaf)
window.openAttendModalById = function(alertId, vehiclePlate, vehicleKm) {
    document.getElementById('attendAlertId').value = alertId;
    document.getElementById('attendVehiclePlate').value = vehiclePlate;
    document.getElementById('attendExecutionKm').value = vehicleKm;
    
    // Mostrar hint con el km actual
    document.getElementById('kmHint').textContent = `Kilometraje actual del vehículo: ${vehicleKm} km`;

    // Fecha actual por defecto
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('attendExecutionDate').value = today;

    document.getElementById('attendAlertModal').classList.remove('hidden');
};

window.closeAttendModal = function() {
    document.getElementById('attendAlertModal').classList.add('hidden');
    document.getElementById('attendAlertForm').reset();
};

// Cerrar modal al hacer clic fuera
window.onclick = function(event) {
    const modal = document.getElementById('attendAlertModal');
    if (event.target === modal) {
        closeAttendModal();
    }
};

// ============================================
// ATENDER ALERTA (REGISTRAR MANTENIMIENTO)
// ============================================
window.attendAlert = async function(event) {
    event.preventDefault();

    const form = document.getElementById('attendAlertForm');
    const formData = new FormData(form);

    try {
        const response = await fetch('/alerts/attend', {
            method: 'POST',
            body: formData
        });

        const text = await response.text();

        if (response.ok && text === 'success') {
            closeAttendModal();
            
            Swal.fire({
                title: '¡Mantenimiento Registrado!',
                text: 'La alerta ha sido marcada como atendida',
                icon: 'success',
                confirmButtonColor: '#6366f1'
            });

            setTimeout(() => {
                if (typeof loadSection === 'function') {
                    loadSection('alerts');
                } else {
                    window.location.reload();
                }
            }, 1000);

        } else {
            throw new Error(text);
        }

    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo registrar el mantenimiento: ' + error.message,
            confirmButtonColor: '#6366f1'
        });
    }
};

// ============================================
// ACTUALIZAR ESTADO DE ALERTA
// ============================================
window.updateAlertStatus = async function(id, newStatus) {
    try {
        const response = await fetch(`/alerts/update-status/${id}?status=${newStatus}`, {
            method: 'POST'
        });

        if (response.ok) {
            Swal.fire({
                title: "Estado actualizado",
                text: `Alerta marcada como ${newStatus.toLowerCase()}`,
                icon: "success",
                confirmButtonColor: "#6366f1",
                timer: 1500,
                showConfirmButton: false
            });

            setTimeout(() => {
                if (typeof loadSection === 'function') {
                    loadSection('alerts');
                } else {
                    window.location.reload();
                }
            }, 500);
        } else {
            throw new Error('Error al actualizar estado');
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: "error",
            title: "Error",
            text: "No se pudo actualizar el estado de la alerta",
            confirmButtonColor: "#6366f1"
        });
    }
};

// ============================================
// BUSCAR/FILTRAR ALERTAS
// ============================================
window.searchAlerts = function(value) {
    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const rows = document.querySelectorAll('#alertsTable tbody tr');

    rows.forEach(row => {
        const rowText = normalize(row.textContent);
        row.style.display = rowText.includes(filter) ? '' : 'none';
    });
};

// ============================================
// FUNCIONES DE UTILIDAD (ADMIN/TESTING)
// ============================================
window.generatePreventiveAlerts = async function() {
    Swal.fire({
        title: 'Generar alertas preventivas',
        text: "¿Desea generar alertas preventivas manualmente?",
        icon: "question",
        showCancelButton: true,
        confirmButtonColor: "#6366f1",
        cancelButtonColor: "#6c757d",
        confirmButtonText: "Sí, generar",
        cancelButtonText: "Cancelar"
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch('/alerts/generate-preventive', {
                    method: 'POST'
                });

                if (response.ok) {
                    Swal.fire({
                        title: "Alertas generadas",
                        text: "Las alertas preventivas han sido generadas",
                        icon: "success",
                        confirmButtonColor: "#6366f1"
                    });

                    setTimeout(() => {
                        if (typeof loadSection === 'function') {
                            loadSection('alerts');
                        } else {
                            window.location.reload();
                        }
                    }, 1000);
                } else {
                    throw new Error('Error al generar alertas');
                }
            } catch (error) {
                console.error('Error:', error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: "No se pudieron generar las alertas",
                    confirmButtonColor: "#6366f1"
                });
            }
        }
    });
};

// Auto-actualizar alertas cada 5 minutos
setInterval(() => {
    if (window.location.pathname === '/alerts' || document.querySelector('#alertsTable')) {
        if (typeof loadSection === 'function') {
            loadSection('alerts');
        }
    }
}, 300000); // 300000 ms = 5 minutos