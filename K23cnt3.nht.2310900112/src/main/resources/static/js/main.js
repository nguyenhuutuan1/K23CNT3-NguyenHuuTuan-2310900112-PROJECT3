/**
 * main.js - Main JavaScript for WebTạpHóa
 * Author: Nguyễn Hữu Tuấn - K23CNT3
 * Version: 1.0
 */

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {

    // ===== INITIALIZE COMPONENTS =====
    initHeroSlider();
    initProductGrid();
    initCategoryMenu();
    initSearch();
    initScrollToTop();
    initNotifications();
    initQuantityControls();

    // ===== UTILITY FUNCTIONS =====

    /**
     * Format currency VND
     * @param {number} amount - Amount to format
     * @returns {string} Formatted currency string
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
     * @param {string} message - Message to display
     * @param {string} type - success/error/warning/info
     */
    function showToast(message, type = 'info') {
        const toastContainer = document.getElementById('toast-container') || createToastContainer();

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <div class="toast-content">
                <i class="toast-icon ${getToastIcon(type)}"></i>
                <span class="toast-message">${message}</span>
            </div>
            <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
        `;

        toastContainer.appendChild(toast);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (toast.parentElement) {
                toast.remove();
            }
        }, 5000);
    }

    /**
     * Get icon for toast type
     */
    function getToastIcon(type) {
        const icons = {
            success: 'fas fa-check-circle',
            error: 'fas fa-exclamation-circle',
            warning: 'fas fa-exclamation-triangle',
            info: 'fas fa-info-circle'
        };
        return icons[type] || icons.info;
    }

    /**
     * Create toast container if not exists
     */
    function createToastContainer() {
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
        return container;
    }

    // ===== HERO SLIDER =====
    function initHeroSlider() {
        const slider = document.querySelector('.hero-slider');
        if (!slider) return;

        const slides = slider.querySelectorAll('.hero-slide');
        if (slides.length <= 1) return;

        let currentSlide = 0;
        const slideCount = slides.length;

        // Show first slide
        slides[0].classList.add('active');

        // Auto slide every 5 seconds
        setInterval(() => {
            nextSlide();
        }, 5000);

        function nextSlide() {
            slides[currentSlide].classList.remove('active');
            currentSlide = (currentSlide + 1) % slideCount;
            slides[currentSlide].classList.add('active');
        }

        // Add navigation dots if not present
        if (!slider.querySelector('.slider-dots')) {
            const dotsContainer = document.createElement('div');
            dotsContainer.className = 'slider-dots';

            for (let i = 0; i < slideCount; i++) {
                const dot = document.createElement('button');
                dot.className = `slider-dot ${i === 0 ? 'active' : ''}`;
                dot.addEventListener('click', () => {
                    slides[currentSlide].classList.remove('active');
                    currentSlide = i;
                    slides[currentSlide].classList.add('active');
                    updateDots();
                });
                dotsContainer.appendChild(dot);
            }

            slider.appendChild(dotsContainer);

            function updateDots() {
                const dots = dotsContainer.querySelectorAll('.slider-dot');
                dots.forEach((dot, index) => {
                    dot.classList.toggle('active', index === currentSlide);
                });
            }
        }
    }

    // ===== PRODUCT GRID =====
    function initProductGrid() {
        const productCards = document.querySelectorAll('.product-card');

        productCards.forEach(card => {
            // Add to cart button
            const addToCartBtn = card.querySelector('.btn-add-to-cart');
            if (addToCartBtn) {
                addToCartBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const productId = this.dataset.productId;
                    const quantity = 1;

                    addToCart(productId, quantity);
                });
            }

            // Quick view (if implemented)
            const quickViewBtn = card.querySelector('.btn-quick-view');
            if (quickViewBtn) {
                quickViewBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const productId = this.dataset.productId;
                    openQuickView(productId);
                });
            }

            // Wishlist (if implemented)
            const wishlistBtn = card.querySelector('.btn-wishlist');
            if (wishlistBtn) {
                wishlistBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const productId = this.dataset.productId;
                    toggleWishlist(productId);
                });
            }
        });
    }

    /**
     * Add product to cart via AJAX
     */
    function addToCart(productId, quantity) {
        fetch('/gio-hang/them', {
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
                showToast(data.message, 'success');
                updateCartCount(data.cartCount);
            } else {
                showToast(data.message, 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Có lỗi xảy ra khi thêm vào giỏ hàng', 'error');
        });
    }

    /**
     * Update cart count in header
     */
    function updateCartCount(count) {
        const cartCountEl = document.querySelector('.cart-count');
        if (cartCountEl) {
            cartCountEl.textContent = count;
            cartCountEl.style.display = count > 0 ? 'flex' : 'none';
        }
    }

    /**
     * Open quick view modal
     */
    function openQuickView(productId) {
        // Implement quick view modal
        console.log('Quick view for product:', productId);
    }

    /**
     * Toggle product in wishlist
     */
    function toggleWishlist(productId) {
        // Implement wishlist functionality
        console.log('Toggle wishlist for product:', productId);
    }

    // ===== CATEGORY MENU =====
    function initCategoryMenu() {
        const categoryMenu = document.querySelector('.category-list');
        if (!categoryMenu) return;

        // Make category menu scrollable on mobile
        if (window.innerWidth < 768) {
            let isDown = false;
            let startX;
            let scrollLeft;

            categoryMenu.addEventListener('mousedown', (e) => {
                isDown = true;
                categoryMenu.classList.add('active');
                startX = e.pageX - categoryMenu.offsetLeft;
                scrollLeft = categoryMenu.scrollLeft;
            });

            categoryMenu.addEventListener('mouseleave', () => {
                isDown = false;
                categoryMenu.classList.remove('active');
            });

            categoryMenu.addEventListener('mouseup', () => {
                isDown = false;
                categoryMenu.classList.remove('active');
            });

            categoryMenu.addEventListener('mousemove', (e) => {
                if (!isDown) return;
                e.preventDefault();
                const x = e.pageX - categoryMenu.offsetLeft;
                const walk = (x - startX) * 2;
                categoryMenu.scrollLeft = scrollLeft - walk;
            });

            // Touch events for mobile
            categoryMenu.addEventListener('touchstart', (e) => {
                isDown = true;
                startX = e.touches[0].pageX - categoryMenu.offsetLeft;
                scrollLeft = categoryMenu.scrollLeft;
            });

            categoryMenu.addEventListener('touchend', () => {
                isDown = false;
            });

            categoryMenu.addEventListener('touchmove', (e) => {
                if (!isDown) return;
                const x = e.touches[0].pageX - categoryMenu.offsetLeft;
                const walk = (x - startX) * 2;
                categoryMenu.scrollLeft = scrollLeft - walk;
            });
        }
    }

    // ===== SEARCH FUNCTIONALITY =====
    function initSearch() {
        const searchForm = document.querySelector('.search-box form');
        const searchInput = document.querySelector('.search-input');

        if (!searchForm || !searchInput) return;

        // Auto-suggestions
        searchInput.addEventListener('input', debounce(function() {
            const query = this.value.trim();
            if (query.length >= 2) {
                fetchSearchSuggestions(query);
            }
        }, 300));

        // Search on Enter
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchForm.submit();
            }
        });
    }

    /**
     * Fetch search suggestions
     */
    function fetchSearchSuggestions(query) {
        fetch(`/api/tim-kiem/goi-y?q=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(suggestions => {
                showSearchSuggestions(suggestions);
            })
            .catch(error => console.error('Error fetching suggestions:', error));
    }

    /**
     * Show search suggestions dropdown
     */
    function showSearchSuggestions(suggestions) {
        // Implement suggestions dropdown
        console.log('Search suggestions:', suggestions);
    }

    /**
     * Debounce function for performance
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

    // ===== SCROLL TO TOP =====
    function initScrollToTop() {
        const scrollBtn = document.createElement('button');
        scrollBtn.className = 'scroll-to-top';
        scrollBtn.innerHTML = '<i class="fas fa-chevron-up"></i>';
        scrollBtn.setAttribute('aria-label', 'Scroll to top');
        document.body.appendChild(scrollBtn);

        window.addEventListener('scroll', () => {
            if (window.pageYOffset > 300) {
                scrollBtn.classList.add('visible');
            } else {
                scrollBtn.classList.remove('visible');
            }
        });

        scrollBtn.addEventListener('click', () => {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    }

    // ===== NOTIFICATIONS =====
    function initNotifications() {
        // Check for flash messages from server
        const flashMessages = document.querySelectorAll('[data-flash-message]');
        flashMessages.forEach(element => {
            const message = element.dataset.flashMessage;
            const type = element.dataset.flashType || 'info';
            if (message) {
                showToast(message, type);
                element.remove();
            }
        });
    }

    // ===== QUANTITY CONTROLS =====
    function initQuantityControls() {
        document.querySelectorAll('.quantity-control').forEach(control => {
            const input = control.querySelector('.quantity-input');
            const minusBtn = control.querySelector('.quantity-minus');
            const plusBtn = control.querySelector('.quantity-plus');

            if (!input || !minusBtn || !plusBtn) return;

            const min = parseInt(input.getAttribute('min')) || 1;
            const max = parseInt(input.getAttribute('max')) || 999;

            minusBtn.addEventListener('click', () => {
                let value = parseInt(input.value) || min;
                if (value > min) {
                    input.value = value - 1;
                    input.dispatchEvent(new Event('change'));
                }
            });

            plusBtn.addEventListener('click', () => {
                let value = parseInt(input.value) || min;
                if (value < max) {
                    input.value = value + 1;
                    input.dispatchEvent(new Event('change'));
                }
            });

            input.addEventListener('change', () => {
                let value = parseInt(input.value) || min;
                if (value < min) input.value = min;
                if (value > max) input.value = max;
                if (isNaN(value)) input.value = min;
            });

            input.addEventListener('blur', () => {
                let value = parseInt(input.value);
                if (isNaN(value) || value < min) {
                    input.value = min;
                }
            });
        });
    }

    // ===== LAZY LOADING IMAGES =====
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    const src = img.getAttribute('data-src');
                    if (src) {
                        img.src = src;
                        img.classList.add('loaded');
                    }
                    observer.unobserve(img);
                }
            });
        });

        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }

    // ===== ADD TO CART ANIMATION =====
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-add-to-cart')) {
            const btn = e.target.closest('.btn-add-to-cart');
            const productCard = btn.closest('.product-card');

            // Add animation class
            btn.classList.add('adding');
            setTimeout(() => {
                btn.classList.remove('adding');
            }, 1000);

            // Create flying item animation
            if (productCard) {
                const productImage = productCard.querySelector('.product-img');
                if (productImage) {
                    createFlyingItem(productImage, document.querySelector('.cart-btn'));
                }
            }
        }
    });

    /**
     * Create flying item animation
     */
    function createFlyingItem(startElement, endElement) {
        const startRect = startElement.getBoundingClientRect();
        const endRect = endElement.getBoundingClientRect();

        const flyingItem = document.createElement('div');
        flyingItem.className = 'flying-item';
        flyingItem.style.cssText = `
            position: fixed;
            width: 40px;
            height: 40px;
            background: url('${startElement.src}') center/contain no-repeat;
            border-radius: 50%;
            border: 2px solid white;
            box-shadow: 0 0 10px rgba(0,0,0,0.3);
            z-index: 10000;
            left: ${startRect.left + startRect.width/2 - 20}px;
            top: ${startRect.top + startRect.height/2 - 20}px;
            pointer-events: none;
            transition: all 0.8s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        `;

        document.body.appendChild(flyingItem);

        // Force reflow
        flyingItem.offsetHeight;

        // Animate to cart
        flyingItem.style.left = `${endRect.left + endRect.width/2 - 20}px`;
        flyingItem.style.top = `${endRect.top + endRect.height/2 - 20}px`;
        flyingItem.style.transform = 'scale(0.5)';
        flyingItem.style.opacity = '0.5';

        // Remove after animation
        setTimeout(() => {
            flyingItem.remove();
        }, 800);
    }

    // ===== EXPORT FUNCTIONS FOR GLOBAL USE =====
    window.WebTapHoa = {
        formatCurrency,
        showToast,
        addToCart,
        updateCartCount
    };

});

// ===== GLOBAL ERROR HANDLER =====
window.addEventListener('error', function(e) {
    console.error('Global error:', e.error);

    // Don't show error toast for network errors in production
    if (e.message && !e.message.includes('Failed to fetch')) {
        const toast = document.createElement('div');
        toast.className = 'toast toast-error';
        toast.innerHTML = `
            <div class="toast-content">
                <i class="fas fa-exclamation-circle"></i>
                <span class="toast-message">Đã xảy ra lỗi. Vui lòng thử lại.</span>
            </div>
            <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
        `;

        const container = document.getElementById('toast-container') || (() => {
            const c = document.createElement('div');
            c.id = 'toast-container';
            c.className = 'toast-container';
            document.body.appendChild(c);
            return c;
        })();

        container.appendChild(toast);

        setTimeout(() => {
            if (toast.parentElement) {
                toast.remove();
            }
        }, 5000);
    }
});

// ===== OFFLINE DETECTION =====
window.addEventListener('online', function() {
    if (window.WebTapHoa && window.WebTapHoa.showToast) {
        window.WebTapHoa.showToast('Đã kết nối lại Internet', 'success');
    }
});

window.addEventListener('offline', function() {
    if (window.WebTapHoa && window.WebTapHoa.showToast) {
        window.WebTapHoa.showToast('Mất kết nối Internet', 'error');
    }
});