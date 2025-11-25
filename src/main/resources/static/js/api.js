// API Base URL
const API_BASE_URL = '/api';

// Utility functions
const api = {
    async get(url) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`);
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('GET Error:', error);
            throw error;
        }
    },

    async post(url, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.mensaje || `Error ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('POST Error:', error);
            throw error;
        }
    },

    async put(url, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.mensaje || `Error ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('PUT Error:', error);
            throw error;
        }
    },

    async delete(url) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'DELETE',
            });
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
            return response.status === 204 ? null : await response.json();
        } catch (error) {
            console.error('DELETE Error:', error);
            throw error;
        }
    },
};

// UI Utilities
const ui = {
    showAlert(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        
        const container = document.querySelector('.container') || document.body;
        container.insertBefore(alertDiv, container.firstChild);
        
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    },

    showLoading(element) {
        element.innerHTML = '<div class="loading"><div class="spinner"></div><p>Cargando...</p></div>';
    },

    formatDate(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    showModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'block';
        }
    },

    hideModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
        }
    },

    closeModalOnClick(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            window.onclick = function(event) {
                if (event.target === modal) {
                    modal.style.display = 'none';
                }
            };
        }
    },

    clearForm(formId) {
        const form = document.getElementById(formId);
        if (form) {
            form.reset();
        }
    },
};

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { api, ui };
}

