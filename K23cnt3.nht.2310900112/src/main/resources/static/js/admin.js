/**
 * admin.js - Admin Panel JavaScript
 * Author: Nguyễn Hữu Tuấn - K23CNT3
 * Version: 1.0
 */

document.addEventListener('DOMContentLoaded', function() {

    // ===== ADMIN SIDEBAR =====
    initSidebar();

    // ===== DATA TABLES =====
    initDataTables();

    // ===== CHARTS =====
    initCharts();

    // ===== FORM VALIDATION =====
    initAdminForms();

    // ===== BULK ACTIONS =====
    initBulkActions();

    // ===== SEARCH & FILTER =====
    initSearchFilter();

    // ===== MODAL DIALOGS =====
    initModals();

    // ===== UTILITY FUNCTIONS =====

    /**
     * Initialize sidebar functionality
     */
    function initSidebar() {
        const sidebarToggle = document.querySelector('.sidebar-toggle');
        const sidebar = document.querySelector('.admin-sidebar');
        const mainContent = document.querySelector('.admin-main');

        if (sidebarToggle && sidebar) {
            sidebarToggle.addEventListener('click', function() {
                sidebar.classList.toggle('active');
                mainContent.classList.toggle('sidebar-collapsed');
            });
        }

        // Auto collapse sidebar on mobile
        if (window.innerWidth < 768) {
            sidebar.classList.remove('active');
            if (mainContent) {
                mainContent.classList.add('sidebar-collapsed');
            }
        }

        // Active menu item
        const currentPath = window.location.pathname;
        document.querySelectorAll('.sidebar-menu a').forEach(link => {
            if (link.getAttribute('href') === currentPath) {
                link.classList.add('active');
            }
        });
    }

    /**
     * Initialize data tables with sorting and filtering
     */
    function initDataTables() {
        const tables = document.querySelectorAll('.data-table');

        tables.forEach(table => {
            // Add sorting functionality
            const headers = table.querySelectorAll('th[data-sort]');
            headers.forEach(header => {
                header.style.cursor = 'pointer';
                header.addEventListener('click', function() {
                    sortTable(table, this.dataset.sort);
                });
            });

            // Add row selection
            if (table.querySelector('th.select-all')) {
                initRowSelection(table);
            }
        });
    }

    /**
     * Sort table by column
     */
    function sortTable(table, columnIndex) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const header = table.querySelector(`th[data-sort="${columnIndex}"]`);
        const isAscending = !header.classList.contains('sort-asc');

        // Clear previous sort indicators
        table.querySelectorAll('th').forEach(th => {
            th.classList.remove('sort-asc', 'sort-desc');
        });

        // Set new sort indicator
        header.classList.add(isAscending ? 'sort-asc' : 'sort-desc');

        // Sort rows
        rows.sort((a, b) => {
            const aValue = a.querySelector(`td:nth-child(${parseInt(columnIndex) + 1})`).textContent;
            const bValue = b.querySelector(`td:nth-child(${parseInt(columnIndex) + 1})`).textContent;

            // Try to compare as numbers
            const aNum = parseFloat(aValue.replace(/[^\d.-]/g, ''));
            const bNum = parseFloat(bValue.replace(/[^\d.-]/g, ''));

            if (!isNaN(aNum) && !isNaN(bNum)) {
                return isAscending ? aNum - bNum : bNum - aNum;
            }

            // Compare as strings
            return isAscending ?
                aValue.localeCompare(bValue) :
                bValue.localeCompare(aValue);
        });

        // Reorder rows
        rows.forEach(row => tbody.appendChild(row));
    }

    /**
     * Initialize row selection for bulk actions
     */
    function initRowSelection(table) {
        const selectAll = table.querySelector('th.select-all input[type="checkbox"]');
        const rowCheckboxes = table.querySelectorAll('td.select-row input[type="checkbox"]');

        if (selectAll) {
            selectAll.addEventListener('change', function() {
                rowCheckboxes.forEach(checkbox => {
                    checkbox.checked = this.checked;
                });
                updateBulkActions();
            });
        }

        rowCheckboxes.forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                updateSelectAllCheckbox();
                updateBulkActions();
            });
        });
    }

    /**
     * Update select all checkbox state
     */
    function updateSelectAllCheckbox() {
        const tables = document.querySelectorAll('.data-table');

        tables.forEach(table => {
            const selectAll = table.querySelector('th.select-all input[type="checkbox"]');
            const rowCheckboxes = table.querySelectorAll('td.select-row input[type="checkbox"]');
            const checkedCount = Array.from(rowCheckboxes).filter(cb => cb.checked).length;

            if (selectAll) {
                selectAll.checked = checkedCount === rowCheckboxes.length;
                selectAll.indeterminate = checkedCount > 0 && checkedCount < rowCheckboxes.length;
            }
        });
    }

    /**
     * Initialize charts using Chart.js
     */
    function initCharts() {
        // Revenue chart
        const revenueCtx = document.getElementById('revenueChart');
        if (revenueCtx) {
            new Chart(revenueCtx.getContext('2d'), {
                type: 'line',
                data: {
                    labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
                    datasets: [{
                        label: 'Doanh thu (triệu VND)',
                        data: [12, 19, 15, 25, 22, 30, 28, 35, 32, 40, 38, 45],
                        borderColor: '#28a745',
                        backgroundColor: 'rgba(40, 167, 69, 0.1)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return `${context.dataset.label}: ${context.parsed.y.toLocaleString('vi-VN')} triệu VND`;
                                }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return value.toLocaleString('vi-VN') + ' triệu';
                                }
                            }
                        }
                    }
                }
            });
        }

        // Sales by category chart
        const categoryCtx = document.getElementById('categoryChart');
        if (categoryCtx) {
            new Chart(categoryCtx.getContext('2d'), {
                type: 'doughnut',
                data: {
                    labels: ['Thực phẩm khô', 'Đồ hộp', 'Gia vị', 'Bánh kẹo', 'Đồ uống', 'Sữa', 'Khác'],
                    datasets: [{
                        data: [25, 15, 10, 20, 15, 10, 5],
                        backgroundColor: [
                            '#28a745',
                            '#17a2b8',
                            '#ffc107',
                            '#dc3545',
                            '#007bff',
                            '#6f42c1',
                            '#fd7e14'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'right',
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = Math.round((context.parsed / total) * 100);
                                    return `${context.label}: ${context.parsed}%`;
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Initialize admin form validation
     */
    function initAdminForms() {
        const forms = document.querySelectorAll('.admin-form');

        forms.forEach(form => {
            form.addEventListener('submit', function(e) {
                if (!validateForm(this)) {
                    e.preventDefault();
                }
            });

            // Real-time validation
            const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
            inputs.forEach(input => {
                input.addEventListener('blur', function() {
                    validateField(this);
                });

                input.addEventListener('input', function() {
                    clearFieldError(this);
                });
            });
        });
    }

    /**
     * Validate form fields
     */
    function validateForm(form) {
        let isValid = true;
        const requiredFields = form.querySelectorAll('[required]');

        requiredFields.forEach(field => {
            if (!validateField(field)) {
                isValid = false;
            }
        });

        if (!isValid) {
            showAdminAlert('Vui lòng điền đầy đủ thông tin bắt buộc', 'error');
            // Scroll to first error
            const firstError = form.querySelector('.is-invalid');
            if (firstError) {
                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }

        return isValid;
    }

    /**
     * Validate individual field
     */
    function validateField(field) {
        let isValid = true;
        let errorMessage = '';

        // Clear previous error
        clearFieldError(field);

        // Check required
        if (field.hasAttribute('required') && !field.value.trim()) {
            isValid = false;
            errorMessage = 'Trường này là bắt buộc';
        }

        // Check email format
        if (field.type === 'email' && field.value.trim()) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(field.value)) {
                isValid = false;
                errorMessage = 'Email không hợp lệ';
            }
        }

        // Check number range
        if (field.type === 'number') {
            const min = field.getAttribute('min');
            const max = field.getAttribute('max');
            const value = parseFloat(field.value);

            if (min && value < parseFloat(min)) {
                isValid = false;
                errorMessage = `Giá trị tối thiểu là ${min}`;
            }

            if (max && value > parseFloat(max)) {
                isValid = false;
                errorMessage = `Giá trị tối đa là ${max}`;
            }
        }

        // Check file type
        if (field.type === 'file') {
            const allowedTypes = field.getAttribute('accept');
            if (allowedTypes && field.files.length > 0) {
                const file = field.files[0];
                const fileType = file.type;
                const allowedArray = allowedTypes.split(',').map(t => t.trim());

                if (!allowedArray.some(type => {
                    if (type.startsWith('.')) {
                        return file.name.toLowerCase().endsWith(type.toLowerCase());
                    }
                    return fileType.startsWith(type.replace('/*', '/'));
                })) {
                    isValid = false;
                    errorMessage = 'Định dạng file không được hỗ trợ';
                }
            }
        }

        // Show error if invalid
        if (!isValid) {
            showFieldError(field, errorMessage);
        }

        return isValid;
    }

    /**
     * Show field error message
     */
    function showFieldError(field, message) {
        field.classList.add('is-invalid');

        const errorDiv = document.createElement('div');
        errorDiv.className = 'invalid-feedback';
        errorDiv.textContent = message;

        field.parentNode.appendChild(errorDiv);
    }

    /**
     * Clear field error
     */
    function clearFieldError(field) {
        field.classList.remove('is-invalid');

        const errorDiv = field.parentNode.querySelector('.invalid-feedback');
        if (errorDiv) {
            errorDiv.remove();
        }
    }

    /**
     * Initialize bulk actions
     */
    function initBulkActions() {
        const bulkActionSelect = document.querySelector('.bulk-actions select');
        const bulkActionBtn = document.querySelector('.bulk-actions .btn');

        if (bulkActionSelect && bulkActionBtn) {
            bulkActionBtn.addEventListener('click', function() {
                const action = bulkActionSelect.value;
                const selectedIds = getSelectedIds();

                if (selectedIds.length === 0) {
                    showAdminAlert('Vui lòng chọn ít nhất một mục', 'warning');
                    return;
                }

                if (confirm(`Bạn có chắc chắn muốn ${getActionName(action)} ${selectedIds.length} mục đã chọn?`)) {
                    executeBulkAction(action, selectedIds);
                }
            });
        }
    }

    /**
     * Get selected row IDs
     */
    function getSelectedIds() {
        const ids = [];
        document.querySelectorAll('.select-row input[type="checkbox"]:checked').forEach(checkbox => {
            ids.push(checkbox.value);
        });
        return ids;
    }

    /**
     * Update bulk actions button state
     */
    function updateBulkActions() {
        const selectedCount = getSelectedIds().length;
        const bulkActionBtn = document.querySelector('.bulk-actions .btn');

        if (bulkActionBtn) {
            bulkActionBtn.disabled = selectedCount === 0;
            bulkActionBtn.textContent = selectedCount > 0 ?
                `Áp dụng cho ${selectedCount} mục` :
                'Áp dụng hàng loạt';
        }
    }

    /**
     * Get action display name
     */
    function getActionName(action) {
        const actions = {
            'delete': 'xóa',
            'activate': 'kích hoạt',
            'deactivate': 'vô hiệu hóa',
            'export': 'xuất',
            'print': 'in'
        };
        return actions[action] || action;
    }

    /**
     * Execute bulk action
     */
    function executeBulkAction(action, ids) {
        fetch('/admin/bulk-action', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({
                action: action,
                ids: ids
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAdminAlert(data.message, 'success');
                // Reload or update table
                if (data.reload) {
                    setTimeout(() => location.reload(), 1500);
                } else {
                    // Remove selected rows
                    ids.forEach(id => {
                        const row = document.querySelector(`tr[data-id="${id}"]`);
                        if (row) row.remove();
                    });
                    updateBulkActions();
                }
            } else {
                showAdminAlert(data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showAdminAlert('Có lỗi xảy ra khi thực hiện hàng loạt', 'error');
        });
    }

    /**
     * Initialize search and filter
     */
    function initSearchFilter() {
        const searchInput = document.querySelector('.table-search input');
        const filterSelect = document.querySelector('.table-filter select');

        if (searchInput) {
            searchInput.addEventListener('input', debounce(function() {
                filterTable(this.value);
            }, 300));
        }

        if (filterSelect) {
            filterSelect.addEventListener('change', function() {
                filterTableByColumn(this.dataset.column, this.value);
            });
        }
    }

    /**
     * Filter table by search term
     */
    function filterTable(searchTerm) {
        const tables = document.querySelectorAll('.data-table');
        const searchLower = searchTerm.toLowerCase();

        tables.forEach(table => {
            const rows = table.querySelectorAll('tbody tr');
            let visibleCount = 0;

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                const isVisible = text.includes(searchLower);
                row.style.display = isVisible ? '' : 'none';
                if (isVisible) visibleCount++;
            });

            // Show/hide no results message
            const noResults = table.parentNode.querySelector('.no-results');
            if (noResults) {
                noResults.style.display = visibleCount === 0 ? 'block' : 'none';
            }
        });
    }

    /**
     * Filter table by column value
     */
    function filterTableByColumn(columnIndex, filterValue) {
        if (!filterValue) return;

        const tables = document.querySelectorAll('.data-table');

        tables.forEach(table => {
            const rows = table.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const cell = row.querySelector(`td:nth-child(${parseInt(columnIndex) + 1})`);
                if (cell) {
                    const cellValue = cell.textContent.toLowerCase();
                    const filterLower = filterValue.toLowerCase();
                    row.style.display = cellValue.includes(filterLower) ? '' : 'none';
                }
            });
        });
    }

    /**
     * Initialize modal dialogs
     */
    function initModals() {
        // Delete confirmation modals
        document.querySelectorAll('[data-toggle="modal"]').forEach(btn => {
            btn.addEventListener('click', function() {
                const target = this.dataset.target;
                const modal = document.querySelector(target);
                if (modal) {
                    // Set modal content based on button data
                    if (this.dataset.id) {
                        modal.querySelector('.modal-id').textContent = this.dataset.id;
                    }
                    if (this.dataset.name) {
                        modal.querySelector('.modal-name').textContent = this.dataset.name;
                    }

                    // Show modal
                    modal.classList.add('show');
                    modal.style.display = 'block';
                }
            });
        });

        // Close modals
        document.querySelectorAll('.modal .close, .modal .btn-secondary').forEach(btn => {
            btn.addEventListener('click', function() {
                const modal = this.closest('.modal');
                modal.classList.remove('show');
                modal.style.display = 'none';
            });
        });

        // Close modal when clicking outside
        document.querySelectorAll('.modal').forEach(modal => {
            modal.addEventListener('click', function(e) {
                if (e.target === this) {
                    this.classList.remove('show');
                    this.style.display = 'none';
                }
            });
        });
    }

    /**
     * Show admin alert
     */
    function showAdminAlert(message, type = 'info') {
        // Remove existing alerts
        const existingAlert = document.querySelector('.admin-alert');
        if (existingAlert) {
            existingAlert.remove();
        }

        // Create alert
        const alert = document.createElement('div');
        alert.className = `admin-alert alert-${type}`;
        alert.innerHTML = `
            <div class="alert-content">
                <i class="alert-icon ${getAlertIcon(type)}"></i>
                <span class="alert-message">${message}</span>
            </div>
            <button class="alert-close" onclick="this.parentElement.remove()">&times;</button>
        `;

        document.querySelector('.admin-header').after(alert);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (alert.parentElement) {
                alert.remove();
            }
        }, 5000);
    }

    /**
     * Get alert icon
     */
    function getAlertIcon(type) {
        const icons = {
            success: 'fas fa-check-circle',
            error: 'fas fa-exclamation-circle',
            warning: 'fas fa-exclamation-triangle',
            info: 'fas fa-info-circle'
        };
        return icons[type] || icons.info;
    }

    /**
     * Debounce function
     */
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // ===== EXPORT FUNCTIONS =====
    window.Admin = {
        showAlert: showAdminAlert,
        validateForm: validateForm,
        executeBulkAction: executeBulkAction
    };

});

// ===== GLOBAL ADMIN FUNCTIONS =====

/**
 * Confirm and delete item
 */
function confirmDelete(itemId, itemName, deleteUrl) {
    if (confirm(`Bạn có chắc chắn muốn xóa "${itemName}"?`)) {
        fetch(deleteUrl, {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Remove row from table
                const row = document.querySelector(`tr[data-id="${itemId}"]`);
                if (row) {
                    row.style.transition = 'all 0.3s ease';
                    row.style.opacity = '0';
                    row.style.height = '0';
                    row.style.overflow = 'hidden';

                    setTimeout(() => {
                        row.remove();
                    }, 300);
                }

                // Show success message
                if (window.Admin && window.Admin.showAlert) {
                    window.Admin.showAlert(data.message, 'success');
                }
            } else {
                if (window.Admin && window.Admin.showAlert) {
                    window.Admin.showAlert(data.message, 'error');
                }
            }
        })
        .catch(error => {
            console.error('Error:', error);
            if (window.Admin && window.Admin.showAlert) {
                window.Admin.showAlert('Có lỗi xảy ra khi xóa', 'error');
            }
        });
    }
}

/**
 * Update status via AJAX
 */
function updateStatus(itemId, newStatus, updateUrl) {
    fetch(updateUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        },
        body: JSON.stringify({
            id: itemId,
            status: newStatus
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update status badge
            const statusBadge = document.querySelector(`tr[data-id="${itemId}"] .status-badge`);
            if (statusBadge) {
                statusBadge.className = `status-badge status-${newStatus.toLowerCase()}`;
                statusBadge.textContent = data.statusText;
            }

            if (window.Admin && window.Admin.showAlert) {
                window.Admin.showAlert(data.message, 'success');
            }
        } else {
            if (window.Admin && window.Admin.showAlert) {
                window.Admin.showAlert(data.message, 'error');
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        if (window.Admin && window.Admin.showAlert) {
            window.Admin.showAlert('Có lỗi xảy ra khi cập nhật', 'error');
        }
    });
}

/**
 * Preview image before upload
 */
function previewImage(input, previewId) {
    const preview = document.getElementById(previewId);
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(input.files[0]);
    }
}

/**
 * Export table to Excel
 */
function exportToExcel(tableId, filename) {
    const table = document.getElementById(tableId);
    if (!table) return;

    // Remove action columns
    const tempTable = table.cloneNode(true);
    tempTable.querySelectorAll('th.select-all, td.select-row, td.actions').forEach(cell => {
        cell.remove();
    });

    // Create workbook
    const wb = XLSX.utils.table_to_book(tempTable, {sheet: "Sheet1"});

    // Generate and download file
    XLSX.writeFile(wb, `${filename}_${new Date().toISOString().split('T')[0]}.xlsx`);
}

/**
 * Print table
 */
function printTable(tableId) {
    const table = document.getElementById(tableId);
    if (!table) return;

    // Create print window
    const printWindow = window.open('', '_blank');
    printWindow.document.write(`
        <html>
        <head>
            <title>In bảng dữ liệu</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                table { width: 100%; border-collapse: collapse; margin: 20px 0; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                th { background-color: #f2f2f2; }
                @media print {
                    body { margin: 0; }
                    .no-print { display: none; }
                }
            </style>
        </head>
        <body>
            <h2>Bảng dữ liệu</h2>
            <p>Ngày in: ${new Date().toLocaleDateString('vi-VN')}</p>
            ${table.outerHTML}
            <div class="no-print" style="margin-top: 20px;">
                <button onclick="window.print()">In</button>
                <button onclick="window.close()">Đóng</button>
            </div>
        </body>
        </html>
    `);
    printWindow.document.close();
}