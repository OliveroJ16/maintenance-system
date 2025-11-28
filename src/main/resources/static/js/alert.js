// alerts.js - Gestión de alertas (SOLO VISUALIZACIÓN - No CRUD completo)

// Función para marcar alerta como vista
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

// Función para marcar todas las alertas como vistas
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

// Función para actualizar el estado de una alerta
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

// Función para buscar/filtrar alertas
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

// Función para obtener clase de badge según el estado
window.getStatusBadgeClass = function(status) {
    const statusClasses = {
        'NOTIFICADA': 'pendiente',
        'ATENDIDA': 'completado',
        'VENCIDA': 'vencida'
    };
    return statusClasses[status] || 'pendiente';
};

// Función para obtener clase de badge según el tipo
window.getTypeBadgeClass = function(type) {
    const typeClasses = {
        'PREVENTIVA': 'badge-info',
        'CORRECTIVA': 'badge-danger'
    };
    return typeClasses[type] || 'badge-secondary';
};

// Función de utilidad para generar alertas manualmente (solo para testing/admin)
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