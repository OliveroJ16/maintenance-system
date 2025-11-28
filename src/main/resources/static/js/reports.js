// reports.js - Gestión de descarga de reportes PDF

// ========== REPORTE DE MANTENIMIENTOS ==========
window.downloadMaintenancesReport = async function(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Construir parámetros de URL
    const params = new URLSearchParams();
    
    const startDate = formData.get('startDate');
    const endDate = formData.get('endDate');
    const status = formData.get('status');
    const vehicleId = formData.get('vehicleId');
    
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    if (status) params.append('status', status);
    if (vehicleId) params.append('vehicleId', vehicleId);
    
    try {
        // Mostrar loading
        Swal.fire({
            title: 'Generando reporte...',
            html: 'Por favor espere',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
        
        const response = await fetch(`/reports/maintenances/download?${params.toString()}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `reporte_mantenimientos_${new Date().getTime()}.pdf`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
            
            Swal.fire({
                title: '¡Descarga exitosa!',
                text: 'El reporte PDF ha sido descargado',
                icon: 'success',
                confirmButtonColor: '#6366f1',
                timer: 2000
            });
        } else {
            throw new Error('Error al generar el reporte');
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo generar el reporte PDF',
            confirmButtonColor: '#6366f1'
        });
    }
};

// ========== REPORTE DE COSTOS ==========
window.downloadCostsReport = async function(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    // Construir parámetros de URL
    const params = new URLSearchParams();
    
    const startDate = formData.get('startDate');
    const endDate = formData.get('endDate');
    const workshopId = formData.get('workshopId');
    
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    if (workshopId) params.append('workshopId', workshopId);
    
    try {
        // Mostrar loading
        Swal.fire({
            title: 'Generando reporte...',
            html: 'Por favor espere',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
        
        const response = await fetch(`/reports/costs/download?${params.toString()}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `reporte_costos_${new Date().getTime()}.pdf`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
            
            Swal.fire({
                title: '¡Descarga exitosa!',
                text: 'El reporte PDF ha sido descargado',
                icon: 'success',
                confirmButtonColor: '#6366f1',
                timer: 2000
            });
        } else {
            throw new Error('Error al generar el reporte');
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo generar el reporte PDF',
            confirmButtonColor: '#6366f1'
        });
    }
};

// ========== REPORTE DE ESTADÍSTICAS COMPLETAS ==========
window.downloadStatisticsReport = async function() {
    try {
        // Mostrar loading
        Swal.fire({
            title: 'Generando reporte...',
            html: 'Por favor espere',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });
        
        const response = await fetch('/reports/statistics/download', {
            method: 'POST'
        });
        
        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `reporte_estadisticas_${new Date().getTime()}.pdf`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
            
            Swal.fire({
                title: '¡Descarga exitosa!',
                text: 'El reporte PDF ha sido descargado',
                icon: 'success',
                confirmButtonColor: '#6366f1',
                timer: 2000
            });
        } else {
            throw new Error('Error al generar el reporte');
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo generar el reporte PDF',
            confirmButtonColor: '#6366f1'
        });
    }
};

// ========== FUNCIÓN DE BÚSQUEDA (si necesitas filtrar reportes en el futuro) ==========
window.searchReports = function(value) {
    const normalize = (str) =>
        str.normalize("NFD")
           .replace(/[\u0300-\u036f]/g, "")
           .toLowerCase();

    const filter = normalize(value);
    const cards = document.querySelectorAll('.section-card');

    cards.forEach(card => {
        const cardText = normalize(card.textContent);
        card.style.display = cardText.includes(filter) ? '' : 'none';
    });
};