/**
 * Feng Shui Jewelry - Premium Toast Notification Utility
 * Provides glassmorphic, senior-friendly notifications.
 */

const FengShuiToast = (function () {
    let container = null;

    // Helper to create the container if it doesn't exist
    function getContainer() {
        if (!container) {
            container = document.getElementById('fengshui-toast-container');
            if (!container) {
                container = document.createElement('div');
                container.id = 'fengshui-toast-container';
                document.body.appendChild(container);
            }
        }
        return container;
    }

    // Get icon class based on toast type
    function getIcon(type) {
        switch (type) {
            case 'success':
                return '<i class="bi bi-check-circle-fill"></i>';
            case 'error':
                return '<i class="bi bi-x-circle-fill"></i>';
            case 'warning':
                return '<i class="bi bi-exclamation-triangle-fill"></i>';
            case 'info':
            default:
                return '<i class="bi bi-info-circle-fill"></i>';
        }
    }

    /**
     * Show a toast notification
     * @param {Object} options - Toast options
     * @param {string} options.message - The text to display
     * @param {string} [options.type='success'] - 'success', 'error', 'warning', 'info'
     * @param {number} [options.duration=4000] - Duration in milliseconds
     */
    function show(options) {
        const message = options.message || '';
        const type = options.type || 'success';
        const duration = options.duration || 4000;

        const toastContainer = getContainer();
        const toast = document.createElement('div');
        
        // Setup classes and styles
        toast.className = `fs-toast ${type}`;
        
        // Define animation custom properties for CSS
        toast.style.setProperty('--toast-duration', `${duration}ms`);
        // The fadeout animation starts 400ms before the total duration ends
        toast.style.setProperty('--toast-delay', `${duration - 400}ms`);

        // Build inner HTML
        const iconHtml = getIcon(type);
        toast.innerHTML = `
            <div class="fs-toast-icon">${iconHtml}</div>
            <div class="fs-toast-content">${message}</div>
            <button class="fs-toast-close" title="Đóng thông báo">
                <i class="bi bi-x"></i>
            </button>
            <div class="fs-toast-progress"></div>
        `;

        // Event listener for manual close button
        const closeBtn = toast.querySelector('.fs-toast-close');
        closeBtn.addEventListener('click', () => {
            removeToast(toast);
        });

        // Add to container
        toastContainer.appendChild(toast);

        // Auto remove when the CSS fadeOut animation finishes
        toast.addEventListener('animationend', (e) => {
            if (e.animationName === 'toastFadeOut') {
                removeToast(toast);
            }
        });
        
        // Backup timeout in case animationend fails
        const autoCloseTimeout = setTimeout(() => {
            removeToast(toast);
        }, duration);

        // Store timeout ID to clear if removed manually
        toast.dataset.timeoutId = autoCloseTimeout;
    }

    function removeToast(toast) {
        if (toast.dataset.timeoutId) {
            clearTimeout(parseInt(toast.dataset.timeoutId));
        }
        
        // Apply immediate fade transition before removing from DOM
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(50px)';
        toast.style.pointerEvents = 'none';
        
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 300);
    }

    // Sugar methods for simplicity
    return {
        show: show,
        success: function (message, duration = 4000) {
            show({ message, type: 'success', duration });
        },
        error: function (message, duration = 4000) {
            show({ message, type: 'error', duration });
        },
        warning: function (message, duration = 4500) {
            show({ message, type: 'warning', duration });
        },
        info: function (message, duration = 4000) {
            show({ message, type: 'info', duration });
        }
    };
})();
