/**
 * cart.js - Shopping Cart JavaScript
 * Author: Nguyễn Hữu Tuấn - K23CNT3
 * Version: 1.0
 */

document.addEventListener('DOMContentLoaded', function() {
    
    // ===== CART ELEMENTS =====
    const cartTable = document.querySelector('.cart-table');
    const cartTotal = document.querySelector('.cart-total');
    const emptyCartMessage = document.querySelector('.cart-empty');
    const checkoutBtn = document.querySelector('.btn-checkout');
    
    // ===== INITIALIZE CART =====
    initCart();
    initQuantityUpdates();
    initRemoveButtons();
    initCheckout();
    
    // ===== CART FUNCTIONS =====
    
    /**
     * Initialize cart functionality
     */
    function initCart() {
        updateCartSummary();
        
        // Show/hide empty cart message
        const cartItems = document.querySelectorAll('.cart-item');
        if (cartItems.length === 0 && emptyCartMessage) {
            emptyCartMessage.style.display = 'block';
            if (cartTable) cartTable.style.display = 'none';
            if (checkoutBtn) checkoutBtn.style.display = 'none';
        } else if (emptyCartMessage) {
            emptyCartMessage.style.display = 'none';
            if (cartTable) cartTable.style.display = 'table';
            if (checkoutBtn) checkoutBtn.style.display = 'block';
        }
    }
    
    /**
     * Initialize quantity update buttons
     */
    function initQuantityUpdates() {
        document.querySelectorAll('.cart-quantity').forEach(input => {
            const productId = input.dataset.productId;
            const minusBtn = input.parentElement.querySelector('.quantity-minus');
            const plusBtn = input.parentElement.querySelector('.quantity-plus');
            
            if (minusBtn) {
                minusBtn.addEventListener('click', () => {
                    updateQuantity(productId, parseInt(input.value) - 1);
                });
            }
            
            if (plusBtn) {
                plusBtn.addEventListener('click', () => {
                    updateQuantity(productId, parseInt(input.value) + 1);
                });
            }
            
            input.addEventListener('change', () => {
                updateQuantity(productId, parseInt(input.value));
            });
            
            input.addEventListener('blur', () => {
                const value = parseInt(input.value);
                if (isNaN(value) || value < 1) {
                    input.value = 1;
                    updateQuantity(productId, 1);
                }
            });
        });
    }
    
    /**
     * Initialize remove item buttons
     */
    function initRemoveButtons() {
        document.querySelectorAll('.btn-remove-item').forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                const productId = this.dataset.productId;
                removeFromCart(productId);
            });
        });
    }
    
    /**
     * Initialize checkout button
     */
    function initCheckout() {
        if (checkoutBtn) {
            checkoutBtn.addEventListener('click', function(e) {
                // Check if cart is empty
                const cartItems = document.querySelectorAll('.cart-item');
                if (cartItems.length === 0) {
                    e.preventDefault();
                    showToast('Giỏ hàng trống. Vui lòng thêm sản phẩm trước khi thanh toán.', 'warning');
                    return;
                }
                
                // Check stock before checkout
                checkStockBeforeCheckout();
            });
        }
    }
    
    /**
     * Update quantity via AJAX
     */
    function updateQuantity(productId, quantity) {
        if (quantity < 1) {
            removeFromCart(productId);
            return;
        }
        
        fetch('/gio-hang/cap-nhat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: `maSP=${productId}&soLuong=${quantity}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                updateCartItem(productId, quantity, data.itemTotal);
                updateCartSummary(data.cartTotal, data.cartCount);
                showToast(data.message, 'success');
            } else {
                showToast(data.message, 'error');
                // Reload to sync with server
                location.reload();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra khi cập nhật giỏ hàng', 'error');
        });
    }
    
    /**
     * Remove item from cart via AJAX
     */
    function removeFromCart(productId) {
        if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?')) {
            return;
        }
        
        fetch('/gio-hang/xoa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: `maSP=${productId}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                removeCartItem(productId);
                updateCartSummary(data.cartTotal, data.cartCount);
                showToast(data.message, 'success');
                initCart(); // Reinitialize cart
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra khi xóa sản phẩm', 'error');
        });
    }
    
    /**
     * Update cart item in DOM
     */
    function updateCartItem(productId, quantity, itemTotal) {
        const itemRow = document.querySelector(`.cart-item[data-product-id="${productId}"]`);
        if (!itemRow) return;
        
        // Update quantity input
        const quantityInput = itemRow.querySelector('.cart-quantity');
        if (quantityInput) {
            quantityInput.value = quantity;
        }
        
        // Update item total
        const itemTotalEl = itemRow.querySelector('.item-total');
        if (itemTotalEl) {
            itemTotalEl.textContent = formatCurrency(itemTotal);
        }
        
        // Update subtotal
        const itemSubtotal = itemRow.querySelector('.item-subtotal');
        if (itemSubtotal) {
            itemSubtotal.textContent = formatCurrency(itemTotal);
        }
    }
    
    /**
     * Remove cart item from DOM
     */
    function removeCartItem(productId) {
        const itemRow = document.querySelector(`.cart-item[data-product-id="${productId}"]`);
        if (itemRow) {
            // Add removal animation
            itemRow.style.transition = 'all 0.3s ease';
            itemRow.style.opacity = '0';
            itemRow.style.transform = 'translateX(-100%)';
            
            setTimeout(() => {
                itemRow.remove();
            }, 300);
        }
    }
    
    /**
     * Update cart summary
     */
    function updateCartSummary(total, count) {
        // Update total display
        if (cartTotal) {
            cartTotal.textContent = formatCurrency(total);
        }
        
        // Update all subtotal elements
        document.querySelectorAll('.cart-subtotal').forEach(el => {
            el.textContent = formatCurrency(total);
        });
        
        // Update cart count in header
        const cartCountEl = document.querySelector('.cart-count');
        if (cartCountEl) {
            cartCountEl.textContent = count;
            cartCountEl.style.display = count > 0 ? 'flex' : 'none';
        }
    }
    
    /**
     * Check stock before checkout
     */
    function checkStockBeforeCheckout() {
        const productIds = [];
        const quantities = [];
        
        document.querySelectorAll('.cart-item').forEach(item => {
            const productId = item.dataset.productId;
            const quantity = item.querySelector('.cart-quantity').value;
            productIds.push(productId);
            quantities.push(quantity);
        });
        
        fetch('/gio-hang/kiem-tra-ton-kho', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({
                products: productIds,
                quantities: quantities
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // All products in stock, proceed to checkout
                window.location.href = '/thanh-toan';
            } else {
                showToast(data.message, 'error');
                // Highlight out of stock items
                data.outOfStockItems.forEach(itemId => {
                    const itemRow = document.querySelector(`.cart-item[data-product-id="${itemId}"]`);
                    if (itemRow) {
                        itemRow.classList.add('out-of-stock');
                    }
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra khi kiểm tra tồn kho', 'error');
        });
    }
    
    /**
     * Clear entire cart
     */
    function clearCart() {
        if (!confirm('Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?')) {
            return;
        }
        
        fetch('/gio-hang/xoa-tat-ca', {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Remove all items with animation
                document.querySelectorAll('.cart-item').forEach((item, index) => {
                    setTimeout(() => {
                        item.style.transition = 'all 0.3s ease';
                        item.style.opacity = '0';
                        item.style.transform = 'translateX(-100%)';
                        
                        setTimeout(() => {
                            item.remove();
                        }, 300);
                    }, index * 100);
                });
                
                // Update summary
                setTimeout(() => {
                    updateCartSummary(0, 0);
                    initCart();
                    showToast(data.message, 'success');
                }, document.querySelectorAll('.cart-item').length * 100 + 300);
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra khi xóa giỏ hàng', 'error');
        });
    }
    
    /**
     * Format currency VND
     */
    function formatCurrency(amount) {
        if (typeof amount !== 'number') {
            amount = parseFloat(amount) || 0;
        }
        return amount.toLocaleString('vi-VN', {
            style: 'currency',
            currency: 'VND'
        });
    }
    
    /**
     * Show toast notification
     */
    function showToast(message, type = 'info') {
        if (window.WebTapHoa && window.WebTapHoa.showToast) {
            window.WebTapHoa.showToast(message, type);
        } else {
            // Fallback to alert
            alert(message);
        }
    }
    
    // ===== EXPORT FUNCTIONS =====
    window.Cart = {
        updateQuantity,
        removeFromCart,
        clearCart,
        formatCurrency
    };
    
    // ===== CLEAR CART BUTTON =====
    const clearCartBtn = document.querySelector('.btn-clear-cart');
    if (clearCartBtn) {
        clearCartBtn.addEventListener('click', clearCart);
    }
    
    // ===== CONTINUE SHOPPING BUTTON =====
    const continueBtn = document.querySelector('.btn-continue-shopping');
    if (continueBtn) {
        continueBtn.addEventListener('click', function() {
            window.location.href = '/';
        });
    }
    
    // ===== UPDATE CART ON PAGE LOAD =====
    // Update cart count from session
    fetch('/gio-hang/so-luong')
        .then(response => response.json())
        .then(data => {
            if (data.count !== undefined) {
                updateCartSummary(0, data.count);
            }
        })
        .catch(error => console.error('Error fetching cart count:', error));
    
});

// ===== CART ANIMATIONS =====
// Add item to cart animation
function animateCartAdd(productId, productName) {
    const cartBtn = document.querySelector('.cart-btn');
    if (!cartBtn) return;
    
    // Create notification
    const notification = document.createElement('div');
    notification.className = 'cart-notification';
    notification.innerHTML = `
        <i class="fas fa-check-circle"></i>
        <span>Đã thêm "${productName}" vào giỏ hàng</span>
    `;
    
    document.body.appendChild(notification);
    
    // Show notification
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // Remove notification
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
    
    // Animate cart button
    cartBtn.classList.add('pulse');
    setTimeout(() => {
        cartBtn.classList.remove('pulse');
    }, 1000);
}

// ===== CART VALIDATION =====
function validateCartForm() {
    const form = document.querySelector('.cart-form');
    if (!form) return true;
    
    let isValid = true;
    const errors = [];
    
    // Check all quantities are valid numbers
    document.querySelectorAll('.cart-quantity').forEach(input => {
        const value = parseInt(input.value);
        const max = parseInt(input.getAttribute('max')) || 999;
        
        if (isNaN(value) || value < 1) {
            isValid = false;
            input.classList.add('is-invalid');
            errors.push('Số lượng sản phẩm không hợp lệ');
        } else if (value > max) {
            isValid = false;
            input.classList.add('is-invalid');
            errors.push(`Số lượng tối đa cho sản phẩm là ${max}`);
        } else {
            input.classList.remove('is-invalid');
        }
    });
    
    // Show errors
    if (!isValid) {
        const errorMessage = errors.join('<br>');
        showToast(errorMessage, 'error');
    }
    
    return isValid;
}