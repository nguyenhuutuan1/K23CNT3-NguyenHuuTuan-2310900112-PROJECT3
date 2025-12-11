/**
 * validation.js - Form Validation JavaScript
 * Author: Nguyễn Hữu Tuấn - K23CNT3
 * Version: 1.0
 */

// ===== VALIDATION UTILITIES =====

/**
 * Check if email is valid
 * @param {string} email - Email to validate
 * @returns {boolean} True if valid
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Check if phone number is valid (Vietnam format)
 * @param {string} phone - Phone number to validate
 * @returns {boolean} True if valid
 */
function isValidPhone(phone) {
    // Remove spaces and +84 prefix
    const cleanPhone = phone.replace(/\s+/g, '').replace(/^\+84/, '0');
    const phoneRegex = /^(0[3|5|7|8|9])[0-9]{8}$/;
    return phoneRegex.test(cleanPhone);
}

/**
 * Check if password is strong enough
 * @param {string} password - Password to validate
 * @returns {Object} {valid: boolean, errors: string[]}
 */
function validatePassword(password) {
    const errors = [];

    if (password.length < 8) {
        errors.push('Mật khẩu phải có ít nhất 8 ký tự');
    }

    if (!/[a-z]/.test(password)) {
        errors.push('Mật khẩu phải có ít nhất một chữ thường');
    }

    if (!/[A-Z]/.test(password)) {
        errors.push('Mật khẩu phải có ít nhất một chữ hoa');
    }

    if (!/\d/.test(password)) {
        errors.push('Mật khẩu phải có ít nhất một số');
    }

    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        errors.push('Mật khẩu phải có ít nhất một ký tự đặc biệt');
    }

    return {
        valid: errors.length === 0,
        errors: errors
    };
}

/**
 * Check if passwords match
 * @param {string} password - Password
 * @param {string} confirmPassword - Confirm password
 * @returns {boolean} True if match
 */
function passwordsMatch(password, confirmPassword) {
    return password === confirmPassword;
}

/**
 * Format phone number to Vietnamese standard
 * @param {string} phone - Phone number to format
 * @returns {string} Formatted phone number
 */
function formatPhoneNumber(phone) {
    const cleanPhone = phone.replace(/\s+/g, '').replace(/^\+84/, '0');

    if (cleanPhone.length === 10) {
        return cleanPhone.replace(/(\d{4})(\d{3})(\d{3})/, '$1 $2 $3');
    }

    if (cleanPhone.length === 11) {
        return cleanPhone.replace(/(\d{4})(\d{3})(\d{4})/, '$1 $2 $3');
    }

    return phone;
}

/**
 * Validate date (must be at least 18 years old)
 * @param {string} dateString - Date string to validate
 * @param {number} minAge - Minimum age required
 * @returns {boolean} True if valid
 */
function isValidDate(dateString, minAge = 18) {
    const date = new Date(dateString);
    const today = new Date();

    // Check if valid date
    if (isNaN(date.getTime())) {
        return false;
    }

    // Check if date is in the past
    if (date > today) {
        return false;
    }

    // Check minimum age
    const age = today.getFullYear() - date.getFullYear();
    const monthDiff = today.getMonth() - date.getMonth();
    const dayDiff = today.getDate() - date.getDate();

    if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
        return age - 1 >= minAge;
    }

    return age >= minAge;
}

/**
 * Validate Vietnamese ID card
 * @param {string} idCard - ID card number to validate
 * @returns {boolean} True if valid
 */
function isValidIdCard(idCard) {
    // Remove all non-digit characters
    const cleanId = idCard.replace(/\D/g, '');

    // Check length (9 or 12 digits)
    if (cleanId.length !== 9 && cleanId.length !== 12) {
        return false;
    }

    // Check all digits
    return /^\d+$/.test(cleanId);
}

/**
 * Validate URL
 * @param {string} url - URL to validate
 * @returns {boolean} True if valid
 */
function isValidUrl(url) {
    try {
        new URL(url);
        return true;
    } catch (error) {
        return false;
    }
}

/**
 * Validate file type
 * @param {File} file - File object
 * @param {string[]} allowedTypes - Array of allowed MIME types or extensions
 * @returns {boolean} True if valid
 */
function isValidFileType(file, allowedTypes) {
    if (!file) return false;

    const fileName = file.name.toLowerCase();
    const fileType = file.type.toLowerCase();

    return allowedTypes.some(type => {
        if (type.startsWith('.')) {
            // Extension check
            return fileName.endsWith(type.toLowerCase());
        } else if (type.includes('/*')) {
            // MIME type wildcard check
            const baseType = type.replace('/*', '');
            return fileType.startsWith(baseType);
        } else {
            // Exact MIME type check
            return fileType === type.toLowerCase();
        }
    });
}

/**
 * Validate file size
 * @param {File} file - File object
 * @param {number} maxSizeMB - Maximum size in MB
 * @returns {boolean} True if valid
 */
function isValidFileSize(file, maxSizeMB) {
    if (!file) return false;
    const maxSizeBytes = maxSizeMB * 1024 * 1024;
    return file.size <= maxSizeBytes;
}

// ===== FORM VALIDATION =====

document.addEventListener('DOMContentLoaded', function() {

    // ===== REGISTRATION FORM =====
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        initRegistrationForm(registerForm);
    }

    // ===== LOGIN FORM =====
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        initLoginForm(loginForm);
    }

    // ===== CHECKOUT FORM =====
    const checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        initCheckoutForm(checkoutForm);
    }

    // ===== PRODUCT FORM =====
    const productForm = document.getElementById('productForm');
    if (productForm) {
        initProductForm(productForm);
    }

    // ===== GENERAL FORM VALIDATION =====
    document.querySelectorAll('form[data-validate]').forEach(form => {
        initFormValidation(form);
    });

    // ===== REAL-TIME VALIDATION =====
    document.querySelectorAll('[data-validate-real-time]').forEach(input => {
        initRealTimeValidation(input);
    });

});

// ===== FORM INITIALIZATION FUNCTIONS =====

/**
 * Initialize registration form validation
 */
function initRegistrationForm(form) {
    const passwordInput = form.querySelector('input[type="password"]');
    const confirmPasswordInput = form.querySelector('input[name="confirmPassword"]');
    const emailInput = form.querySelector('input[type="email"]');
    const phoneInput = form.querySelector('input[type="tel"]');

    if (passwordInput && confirmPasswordInput) {
        // Real-time password validation
        passwordInput.addEventListener('input', function() {
            validatePasswordField(this);
        });

        // Real-time password confirmation
        confirmPasswordInput.addEventListener('input', function() {
            validatePasswordConfirmation(passwordInput, this);
        });
    }

    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            validateEmailField(this);
        });
    }

    if (phoneInput) {
        phoneInput.addEventListener('blur', function() {
            validatePhoneField(this);
        });

        // Format phone number on input
        phoneInput.addEventListener('input', function() {
            formatPhoneField(this);
        });
    }

    // Form submission
    form.addEventListener('submit', function(e) {
        if (!validateRegistrationForm(this)) {
            e.preventDefault();
        }
    });
}

/**
 * Initialize login form validation
 */
function initLoginForm(form) {
    const emailInput = form.querySelector('input[type="email"]');
    const passwordInput = form.querySelector('input[type="password"]');

    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            validateEmailField(this);
        });
    }

    if (passwordInput) {
        passwordInput.addEventListener('blur', function() {
            if (!this.value.trim()) {
                showFieldError(this, 'Vui lòng nhập mật khẩu');
            } else {
                clearFieldError(this);
            }
        });
    }

    form.addEventListener('submit', function(e) {
        if (!validateLoginForm(this)) {
            e.preventDefault();
        }
    });
}

/**
 * Initialize checkout form validation
 */
function initCheckoutForm(form) {
    const requiredFields = form.querySelectorAll('[required]');

    requiredFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateRequiredField(this);
        });

        field.addEventListener('input', function() {
            clearFieldError(this);
        });
    });

    // Phone number formatting
    const phoneInput = form.querySelector('input[type="tel"]');
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            formatPhoneField(this);
        });

        phoneInput.addEventListener('blur', function() {
            validatePhoneField(this);
        });
    }

    // Email validation
    const emailInput = form.querySelector('input[type="email"]');
    if (emailInput) {
        emailInput.addEventListener('blur', function() {
            validateEmailField(this);
        });
    }

    form.addEventListener('submit', function(e) {
        if (!validateCheckoutForm(this)) {
            e.preventDefault();
        }
    });
}

/**
 * Initialize product form validation
 */
function initProductForm(form) {
    // Price validation
    const priceInput = form.querySelector('input[name="donGia"]');
    if (priceInput) {
        priceInput.addEventListener('blur', function() {
            validatePriceField(this);
        });
    }

    // Quantity validation
    const quantityInput = form.querySelector('input[name="soLuong"]');
    if (quantityInput) {
        quantityInput.addEventListener('blur', function() {
            validateQuantityField(this);
        });
    }

    // Image validation
    const imageInput = form.querySelector('input[type="file"]');
    if (imageInput) {
        imageInput.addEventListener('change', function() {
            validateImageField(this);
        });
    }

    // Required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateRequiredField(this);
        });
    });

    form.addEventListener('submit', function(e) {
        if (!validateProductForm(this)) {
            e.preventDefault();
        }
    });
}

/**
 * Initialize general form validation
 */
function initFormValidation(form) {
    const requiredFields = form.querySelectorAll('[required]');

    requiredFields.forEach(field => {
        field.addEventListener('blur', function() {
            validateRequiredField(this);
        });

        field.addEventListener('input', function() {
            clearFieldError(this);
        });
    });

    // Special field types
    form.querySelectorAll('input[type="email"]').forEach(field => {
        field.addEventListener('blur', function() {
            validateEmailField(this);
        });
    });

    form.querySelectorAll('input[type="tel"]').forEach(field => {
        field.addEventListener('blur', function() {
            validatePhoneField(this);
        });

        field.addEventListener('input', function() {
            formatPhoneField(this);
        });
    });

    form.addEventListener('submit', function(e) {
        if (!validateGeneralForm(this)) {
            e.preventDefault();
        }
    });
}

/**
 * Initialize real-time validation
 */
function initRealTimeValidation(input) {
    const validationType = input.dataset.validateRealTime;

    input.addEventListener('input', function() {
        clearFieldError(this);

        switch (validationType) {
            case 'email':
                if (this.value && !isValidEmail(this.value)) {
                    showFieldError(this, 'Email không hợp lệ');
                }
                break;

            case 'phone':
                if (this.value && !isValidPhone(this.value)) {
                    showFieldError(this, 'Số điện thoại không hợp lệ');
                }
                break;

            case 'password':
                if (this.value) {
                    const result = validatePassword(this.value);
                    if (!result.valid) {
                        showFieldError(this, result.errors[0]);
                    }
                }
                break;
        }
    });
}

// ===== FORM VALIDATION FUNCTIONS =====

/**
 * Validate registration form
 */
function validateRegistrationForm(form) {
    let isValid = true;

    // Required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(field => {
        if (!validateRequiredField(field)) {
            isValid = false;
        }
    });

    // Email
    const emailInput = form.querySelector('input[type="email"]');
    if (emailInput && !validateEmailField(emailInput)) {
        isValid = false;
    }

    // Phone
    const phoneInput = form.querySelector('input[type="tel"]');
    if (phoneInput && !validatePhoneField(phoneInput)) {
        isValid = false;
    }

    // Password
    const passwordInput = form.querySelector('input[type="password"]');
    const confirmPasswordInput = form.querySelector('input[name="confirmPassword"]');

    if (passwordInput && !validatePasswordField(passwordInput)) {
        isValid = false;
    }

    if (passwordInput && confirmPasswordInput) {
        if (!validatePasswordConfirmation(passwordInput, confirmPasswordInput)) {
            isValid = false;
        }
    }

    // Terms agreement
    const termsCheckbox = form.querySelector('input[name="terms"]');
    if (termsCheckbox && !termsCheckbox.checked) {
        showFieldError(termsCheckbox, 'Vui lòng đồng ý với điều khoản');
        isValid = false;
    }

    return isValid;
}

/**
 * Validate login form
 */
function validateLoginForm(form) {
    let isValid = true;

    const emailInput = form.querySelector('input[type="email"]');
    const passwordInput = form.querySelector('input[type="password"]');

    if (emailInput && !validateEmailField(emailInput)) {
        isValid = false;
    }

    if (passwordInput && !passwordInput.value.trim()) {
        showFieldError(passwordInput, 'Vui lòng nhập mật khẩu');
        isValid = false;
    }

    return isValid;
}

/**
 * Validate checkout form
 */
function validateCheckoutForm(form) {
    let isValid = true;

    // Required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(field => {
        if (!validateRequiredField(field)) {
            isValid = false;
        }
    });

    // Email
    const emailInput = form.querySelector('input[type="email"]');
    if (emailInput && !validateEmailField(emailInput)) {
        isValid = false;
    }

    // Phone
    const phoneInput = form.querySelector('input[type="tel"]');
    if (phoneInput && !validatePhoneField(phoneInput)) {
        isValid = false;
    }

    return isValid;
}

/**
 * Validate product form
 */
function validateProductForm(form) {
    let isValid = true;

    // Required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(field => {
        if (!validateRequiredField(field)) {
            isValid = false;
        }
    });

    // Price
    const priceInput = form.querySelector('input[name="donGia"]');
    if (priceInput && !validatePriceField(priceInput)) {
        isValid = false;
    }

    // Quantity
    const quantityInput = form.querySelector('input[name="soLuong"]');
    if (quantityInput && !validateQuantityField(quantityInput)) {
        isValid = false;
    }

    // Image
    const imageInput = form.querySelector('input[type="file"]');
    if (imageInput && imageInput.files.length > 0 && !validateImageField(imageInput)) {
        isValid = false;
    }

    return isValid;
}

/**
 * Validate general form
 */
function validateGeneralForm(form) {
    let isValid = true;

    // Required fields
    const requiredFields = form.querySelectorAll('[required]');
    requiredFields.forEach(field => {
        if (!validateRequiredField(field)) {
            isValid = false;
        }
    });

    // Email fields
    form.querySelectorAll('input[type="email"]').forEach(field => {
        if (field.value && !validateEmailField(field)) {
            isValid = false;
        }
    });

    // Phone fields
    form.querySelectorAll('input[type="tel"]').forEach(field => {
        if (field.value && !validatePhoneField(field)) {
            isValid = false;
        }
    });

    return isValid;
}

// ===== FIELD VALIDATION FUNCTIONS =====

/**
 * Validate required field
 */
function validateRequiredField(field) {
    const value = field.type === 'checkbox' ? field.checked : field.value.trim();

    if (!value) {
        const fieldName = field.getAttribute('data-field-name') ||
                         field.previousElementSibling?.textContent?.replace(':', '') ||
                         'Trường này';
        showFieldError(field, `${fieldName} là bắt buộc`);
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate email field
 */
function validateEmailField(field) {
    const value = field.value.trim();

    if (!value) {
        if (field.hasAttribute('required')) {
            showFieldError(field, 'Email là bắt buộc');
            return false;
        }
        return true;
    }

    if (!isValidEmail(value)) {
        showFieldError(field, 'Email không hợp lệ');
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate phone field
 */
function validatePhoneField(field) {
    const value = field.value.trim();

    if (!value) {
        if (field.hasAttribute('required')) {
            showFieldError(field, 'Số điện thoại là bắt buộc');
            return false;
        }
        return true;
    }

    if (!isValidPhone(value)) {
        showFieldError(field, 'Số điện thoại không hợp lệ');
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate password field
 */
function validatePasswordField(field) {
    const value = field.value;

    if (!value) {
        if (field.hasAttribute('required')) {
            showFieldError(field, 'Mật khẩu là bắt buộc');
            return false;
        }
        return true;
    }

    const result = validatePassword(value);
    if (!result.valid) {
        showFieldError(field, result.errors[0]);
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate password confirmation
 */
function validatePasswordConfirmation(passwordField, confirmField) {
    const password = passwordField.value;
    const confirmPassword = confirmField.value;

    if (!confirmPassword) {
        if (confirmField.hasAttribute('required')) {
            showFieldError(confirmField, 'Vui lòng xác nhận mật khẩu');
            return false;
        }
        return true;
    }

    if (!passwordsMatch(password, confirmPassword)) {
        showFieldError(confirmField, 'Mật khẩu không khớp');
        return false;
    }

    clearFieldError(confirmField);
    return true;
}

/**
 * Validate price field
 */
function validatePriceField(field) {
    const value = parseFloat(field.value);

    if (isNaN(value)) {
        showFieldError(field, 'Giá không hợp lệ');
        return false;
    }

    if (value < 0) {
        showFieldError(field, 'Giá không được âm');
        return false;
    }

    if (value > 1000000000) { // 1 tỷ
        showFieldError(field, 'Giá quá lớn');
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate quantity field
 */
function validateQuantityField(field) {
    const value = parseInt(field.value);

    if (isNaN(value)) {
        showFieldError(field, 'Số lượng không hợp lệ');
        return false;
    }

    if (value < 0) {
        showFieldError(field, 'Số lượng không được âm');
        return false;
    }

    if (value > 10000) {
        showFieldError(field, 'Số lượng quá lớn');
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Validate image field
 */
function validateImageField(field) {
    if (field.files.length === 0) {
        if (field.hasAttribute('required')) {
            showFieldError(field, 'Vui lòng chọn ảnh');
            return false;
        }
        return true;
    }

    const file = field.files[0];
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
    const maxSizeMB = 5;

    if (!isValidFileType(file, allowedTypes)) {
        showFieldError(field, 'Chỉ chấp nhận file ảnh (JPEG, PNG, GIF, WebP)');
        return false;
    }

    if (!isValidFileSize(file, maxSizeMB)) {
        showFieldError(field, 'Kích thước file không được vượt quá 5MB');
        return false;
    }

    clearFieldError(field);
    return true;
}

/**
 * Format phone field
 */
function formatPhoneField(field) {
    const value = field.value;
    const formatted = formatPhoneNumber(value);

    if (formatted !== value) {
        field.value = formatted;
    }
}

// ===== ERROR HANDLING FUNCTIONS =====

/**
 * Show field error
 */
function showFieldError(field, message) {
    // Remove existing error
    clearFieldError(field);

    // Add error class
    field.classList.add('is-invalid');

    // Create error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;

    // Insert after field
    field.parentNode.appendChild(errorDiv);

    // Focus field if it's the first error
    const firstError = field.form?.querySelector('.is-invalid');
    if (firstError === field) {
        field.focus();
    }
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
 * Show form error summary
 */
function showFormErrors(form, errors) {
    // Remove existing error summary
    const existingSummary = form.querySelector('.error-summary');
    if (existingSummary) {
        existingSummary.remove();
    }

    if (errors.length === 0) return;

    // Create error summary
    const summary = document.createElement('div');
    summary.className = 'error-summary alert alert-danger';
    summary.innerHTML = `
        <h5>Vui lòng sửa các lỗi sau:</h5>
        <ul>
            ${errors.map(error => `<li>${error}</li>`).join('')}
        </ul>
    `;

    // Insert at top of form
    form.insertBefore(summary, form.firstChild);

    // Scroll to error summary
    summary.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

// ===== EXPORT VALIDATION FUNCTIONS =====
window.Validation = {
    // Utility functions
    isValidEmail,
    isValidPhone,
    validatePassword,
    passwordsMatch,
    formatPhoneNumber,
    isValidDate,
    isValidIdCard,
    isValidUrl,
    isValidFileType,
    isValidFileSize,

    // Field validation
    validateRequiredField,
    validateEmailField,
    validatePhoneField,
    validatePasswordField,
    validatePasswordConfirmation,
    validatePriceField,
    validateQuantityField,
    validateImageField,

    // Form validation
    validateRegistrationForm,
    validateLoginForm,
    validateCheckoutForm,
    validateProductForm,
    validateGeneralForm,

    // Error handling
    showFieldError,
    clearFieldError,
    showFormErrors
};