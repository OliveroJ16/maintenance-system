// Cargar secciones
const contentArea = document.getElementById("contentArea");
const menuItems = document.querySelectorAll(".menu-item");

async function loadSection(section) {
    try {
        const response = await fetch(`${section}`);
        const html = await response.text();
        contentArea.innerHTML = html;

        setTimeout(() => {
            const pageTitle = document.getElementById("pageTitle");
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

window.addEventListener("click", function (event) {
    const modals = [
        'userModal',
        'editUserModal',
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


loadSection("inicio");