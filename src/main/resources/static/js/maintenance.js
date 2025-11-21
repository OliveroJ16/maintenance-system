function switchTab(tabId) {
    // Ocultar todo
    document.getElementById("tab-mantenimientos").style.display = "none";
    document.getElementById("tab-tipos").style.display = "none";
    document.getElementById("tab-config").style.display = "none";

    // Mostrar tab seleccionada
    document.getElementById(tabId).style.display = "block";

    // Cambiar botones activos
    document.querySelectorAll(".tab-btn").forEach(btn => {
        btn.classList.remove("btn-primary");
        btn.classList.add("btn-secondary");
    });

    document.getElementById("tabBtn-" + tabId.split('-')[1])
        .classList.remove("btn-secondary");
    document.getElementById("tabBtn-" + tabId.split('-')[1])
        .classList.add("btn-primary");
}