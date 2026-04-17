
'use strict';

/* ---- Product Detail: Dynamic Price Calculator ---- */
function initPriceCalculator() {
    const woodRadios   = document.querySelectorAll('input[name="woodTypeId"]');
    const priceDisplay = document.getElementById('currentPrice');
    const basePriceEl  = document.getElementById('basePriceData');
    const woodPriceMap = window.WOOD_PRICE_MAP || {};

    if (!priceDisplay || !basePriceEl) return;

    const basePrice = parseFloat(basePriceEl.dataset.price || '0');

    function updatePrice() {
        const selected = document.querySelector('input[name="woodTypeId"]:checked');
        let finalPrice = basePrice;

        if (selected) {
            const woodId = selected.value;
            const data = woodPriceMap[woodId];
            if (data) {
                if (data.override !== null && data.override !== undefined && data.override !== '') {
                    finalPrice = parseFloat(data.override);
                } else {
                    finalPrice = basePrice + parseFloat(data.adjustment || 0);
                }
            }
        }

        priceDisplay.textContent = '₹' + finalPrice.toLocaleString('en-IN', {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        });
    }

    woodRadios.forEach(radio => radio.addEventListener('change', updatePrice));
    updatePrice(); // run on load
}

/* ---- Product Detail: Image Gallery ---- */
function initGallery() {
    const mainImg  = document.getElementById('galleryMainImg');
    const thumbs   = document.querySelectorAll('.gallery-thumb');

    if (!mainImg || thumbs.length === 0) return;

    thumbs.forEach(thumb => {
        thumb.addEventListener('click', () => {
            mainImg.src = thumb.dataset.src;
            thumbs.forEach(t => t.classList.remove('active'));
            thumb.classList.add('active');
        });
    });
}

/* ---- Product Detail: Custom Size Fields Toggle ---- */
function initCustomSizeToggle() {
    const customSizeSection = document.getElementById('customSizeSection');
    if (!customSizeSection) return;

    // Section is shown/hidden based on product.customSizeAllowed (set server-side)
    // No toggle needed for user — the section is always visible if allowed.
    // However we can add a nice reveal animation:
    customSizeSection.style.display = 'block';
}

/* ---- Admin: Image Upload Preview ---- */
function initImagePreview() {
    const primaryInput  = document.getElementById('primaryImageInput');
    const additionalInput = document.getElementById('additionalImagesInput');

    if (primaryInput) {
        primaryInput.addEventListener('change', function () {
            previewImages(this.files, 'primaryPreview', true);
        });
    }
    if (additionalInput) {
        additionalInput.addEventListener('change', function () {
            previewImages(this.files, 'additionalPreview', false);
        });
    }
}

function previewImages(files, containerId, single) {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = '';

    const limit = single ? 1 : files.length;
    for (let i = 0; i < limit; i++) {
        const file = files[i];
        if (!file.type.startsWith('image/')) continue;

        const reader = new FileReader();
        reader.onload = (e) => {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('img-preview-thumb');
            container.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
}

/* ---- Admin: Wood Price Row Builder ---- */
function initWoodPriceRows() {
    const container = document.getElementById('woodPriceContainer');
    if (!container) return;
    // Rows are pre-rendered by Thymeleaf; JS manages "add row" if needed
}

/* ---- Flash Message Auto-dismiss ---- */
function initAlertDismiss() {
    const alerts = document.querySelectorAll('.alert-auto-dismiss');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });
}

/* ---- Confirm Delete ---- */
function confirmDelete(formId, itemName) {
    if (confirm(`Are you sure you want to delete "${itemName}"? This action cannot be undone.`)) {
        document.getElementById(formId).submit();
    }
}

/* ---- Quantity Stepper ---- */
function initQuantityStepper() {
    const minusBtn = document.getElementById('qtyMinus');
    const plusBtn  = document.getElementById('qtyPlus');
    const qtyInput = document.getElementById('qtyInput');

    if (!qtyInput) return;

    if (minusBtn) {
        minusBtn.addEventListener('click', () => {
            const val = parseInt(qtyInput.value) || 1;
            if (val > 1) qtyInput.value = val - 1;
        });
    }
    if (plusBtn) {
        plusBtn.addEventListener('click', () => {
            const val = parseInt(qtyInput.value) || 1;
            qtyInput.value = val + 1;
        });
    }
}

/* ---- Bootstrap Tooltips ---- */
function initTooltips() {
    const tooltipEls = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipEls.forEach(el => new bootstrap.Tooltip(el));
}

/* ---- Bootstrap Popovers ---- */
function initPopovers() {
    const popoverEls = document.querySelectorAll('[data-bs-toggle="popover"]');
    popoverEls.forEach(el => new bootstrap.Popover(el));
}

/* ---- Admin Sidebar Active Link ---- */
function initSidebarActive() {
    const links = document.querySelectorAll('.sidebar-link');
    const path  = window.location.pathname;
    links.forEach(link => {
        if (link.getAttribute('href') && path.startsWith(link.getAttribute('href'))) {
            link.classList.add('active');
        }
    });
}

/* ---- DOM Ready ---- */
document.addEventListener('DOMContentLoaded', () => {
    initPriceCalculator();
    initGallery();
    initCustomSizeToggle();
    initImagePreview();
    initAlertDismiss();
    initQuantityStepper();
    initTooltips();
    initPopovers();
    initSidebarActive();
});