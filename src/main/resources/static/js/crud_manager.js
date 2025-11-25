class CRUDManager {
    constructor(config) {
        this.entity = config.entity; // 'users', 'choferes', etc.
        this.entityName = config.entityName; // 'usuario', 'chofer'
        this.endpoint = config.endpoint; // '/users', '/choferes'
        this.tableId = config.tableId; // 'usersTable', 'driversTable'
        this.modalPrefix = config.modalPrefix; // 'user', 'driver'
    }

    // Confirmar eliminación
    confirmDelete(id) {
        Swal.fire({
            title: `¿Eliminar ${this.entityName}?`,
            text: "Esta acción no se puede revertir",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#e3342f",
            cancelButtonColor: "#6c757d",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar"
        }).then(async (result) => {
            if (result.isConfirmed) {
                await this.delete(id);
            }
        });
    }

    // Eliminar registro
    async delete(id) {
        try {
            const response = await fetch(`${this.endpoint}/delete/${id}`);

            if (response.ok) {
                Swal.fire({
                    title: "¡Eliminado!",
                    text: `El ${this.entityName} fue eliminado correctamente`,
                    icon: "success",
                    confirmButtonColor: "#6366f1"
                });
                
                // VERIFICADO: Usa loadSection global
                setTimeout(() => {
                    if (typeof loadSection === 'function') {
                        loadSection(this.entity);
                    } else {
                        console.error('loadSection no está definida globalmente');
                    }
                }, 500);
            } else {
                this.showError(`No se pudo eliminar el ${this.entityName}.`);
            }
        } catch (error) {
            console.error(error);
            this.showConnectionError();
        }
    }

    // Abrir modal de registro
    openModal() {
        const modal = document.getElementById(`${this.modalPrefix}Modal`);
        if (!modal) return console.error('Modal no encontrado');

        modal.classList.remove('hidden');
        
        const title = document.getElementById(`${this.modalPrefix}ModalTitle`) || 
                     document.getElementById('modalTitle');
        const form = document.getElementById(`${this.modalPrefix}Form`);

        if (title) title.textContent = `Nuevo ${this.entityName.charAt(0).toUpperCase() + this.entityName.slice(1)}`;
        if (form) form.reset();
    }

    // Cerrar modal de registro
    closeModal() {
        const modal = document.getElementById(`${this.modalPrefix}Modal`);
        if (modal) modal.classList.add('hidden');
    }

    // Abrir modal de edición
    openEditModal(data) {
        const modal = document.getElementById(`edit${this.modalPrefix.charAt(0).toUpperCase() + this.modalPrefix.slice(1)}Modal`);
        if (!modal) return console.error('Modal de edición no encontrado');

        modal.classList.remove('hidden');
        this.fillForm(`edit${this.modalPrefix.charAt(0).toUpperCase() + this.modalPrefix.slice(1)}Form`, data);
    }

    // Cerrar modal de edición
    closeEditModal() {
        const modal = document.getElementById(`edit${this.modalPrefix.charAt(0).toUpperCase() + this.modalPrefix.slice(1)}Modal`);
        if (modal) modal.classList.add('hidden');
    }

    // Rellenar formulario
    fillForm(formId, data) {
        const form = document.getElementById(formId);
        if (!form) return;

        Object.keys(data).forEach(key => {
            const input = form.querySelector(`[name="${key}"]`);
            if (input) input.value = data[key] || '';
        });
    }

    // Guardar nuevo registro
    async save(event) {
        event.preventDefault();

        const form = document.getElementById(`${this.modalPrefix}Form`);
        const formData = new FormData(form);

        try {
            const response = await fetch(this.endpoint, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                this.closeModal();
                Swal.fire({
                    title: `¡${this.entityName.charAt(0).toUpperCase() + this.entityName.slice(1)} guardado!`,
                    text: "El registro se completó correctamente",
                    icon: "success",
                    confirmButtonColor: "#6366f1"
                });
                
                // VERIFICADO: Usa loadSection global
                setTimeout(() => {
                    if (typeof loadSection === 'function') {
                        loadSection(this.entity);
                    } else {
                        console.error('loadSection no está definida globalmente');
                    }
                }, 500);
            } else {
                this.showError("No se pudo completar la operación.");
            }
        } catch (error) {
            console.error('Error:', error);
            this.showConnectionError();
        }
    }

    // Actualizar registro
    async update(event) {
        event.preventDefault();

        const form = document.getElementById(`edit${this.modalPrefix.charAt(0).toUpperCase() + this.modalPrefix.slice(1)}Form`);
        const formData = new FormData(form);
        const id = formData.get(`id${this.modalPrefix.charAt(0).toUpperCase() + this.modalPrefix.slice(1)}`);

        try {
            const response = await fetch(`${this.endpoint}/update/${id}`, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                this.closeEditModal();
                Swal.fire({
                    title: "Actualizado",
                    text: `El ${this.entityName} fue actualizado correctamente`,
                    icon: "success",
                    confirmButtonColor: "#6366f1"
                });
                
                // VERIFICADO: Usa loadSection global
                setTimeout(() => {
                    if (typeof loadSection === 'function') {
                        loadSection(this.entity);
                    } else {
                        console.error('loadSection no está definida globalmente');
                    }
                }, 500);
            } else {
                this.showError(`No se pudo actualizar el ${this.entityName}`);
            }
        } catch (error) {
            console.error("Error:", error);
            this.showConnectionError();
        }
    }

    // Buscar/filtrar
    search(value) {
        const normalize = (str) =>
            str.normalize("NFD")
               .replace(/[\u0300-\u036f]/g, "")
               .toLowerCase();

        const filter = normalize(value);
        const rows = document.querySelectorAll(`#${this.tableId} tbody tr`);

        rows.forEach(row => {
            const rowText = normalize(row.textContent);
            row.style.display = rowText.includes(filter) ? '' : 'none';
        });
    }

    // Helpers para mensajes de error
    showError(message) {
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: message,
            confirmButtonColor: "#6366f1"
        });
    }

    showConnectionError() {
        Swal.fire({
            icon: "error",
            title: "Error de conexión",
            text: "No se pudo conectar con el servidor",
            confirmButtonColor: "#6366f1"
        });
    }
}