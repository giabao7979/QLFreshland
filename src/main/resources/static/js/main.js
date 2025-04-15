// Main JavaScript file for Freshland Store

// Wait for DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        var alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
    
    // Product quantity controls
    var quantityControls = document.querySelectorAll('.quantity-control');
    quantityControls.forEach(function(control) {
        var decreaseBtn = control.querySelector('.decrease');
        var increaseBtn = control.querySelector('.increase');
        var input = control.querySelector('input');
        
        if (decreaseBtn && increaseBtn && input) {
            decreaseBtn.addEventListener('click', function() {
                var currentValue = parseInt(input.value);
                if (currentValue > 1) {
                    input.value = currentValue - 1;
                }
            });
            
            increaseBtn.addEventListener('click', function() {
                var currentValue = parseInt(input.value);
                var max = parseInt(input.getAttribute('max') || 100);
                if (currentValue < max) {
                    input.value = currentValue + 1;
                }
            });
        }
    });
    
    // Add to cart functionality
    var addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            var productId = this.getAttribute('data-product-id');
            var quantity = 1;
            
            // Get quantity from input if available
            var quantityInput = document.querySelector('#quantity-' + productId);
            if (quantityInput) {
                quantity = parseInt(quantityInput.value);
            }
            
            // Add to cart logic would go here (AJAX call to server)
            console.log('Adding to cart: Product ID = ' + productId + ', Quantity = ' + quantity);
            
            // Show success message
            var toast = document.createElement('div');
            toast.className = 'toast align-items-center text-white bg-success border-0 position-fixed bottom-0 end-0 m-3';
            toast.setAttribute('role', 'alert');
            toast.setAttribute('aria-live', 'assertive');
            toast.setAttribute('aria-atomic', 'true');
            toast.innerHTML = `
                <div class="d-flex">
                    <div class="toast-body">
                        Product added to cart successfully!
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            `;
            document.body.appendChild(toast);
            
            var bsToast = new bootstrap.Toast(toast);
            bsToast.show();
            
            // Remove toast after it's hidden
            toast.addEventListener('hidden.bs.toast', function() {
                document.body.removeChild(toast);
            });
        });
    });
    
    // Image preview for product form
    var productImageInput = document.getElementById('productImage');
    if (productImageInput) {
        productImageInput.addEventListener('change', function() {
            if (this.files && this.files[0]) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var imagePreview = document.querySelector('.card-img-top');
                    if (imagePreview) {
                        imagePreview.src = e.target.result;
                    } else {
                        // Create preview if doesn't exist
                        var previewContainer = document.createElement('div');
                        previewContainer.className = 'mb-3';
                        previewContainer.innerHTML = `
                            <label class="form-label">Image Preview</label>
                            <div class="card">
                                <img src="${e.target.result}" class="card-img-top" alt="Product Image Preview">
                            </div>
                        `;
                        productImageInput.parentNode.insertAdjacentElement('afterend', previewContainer);
                    }
                };
                reader.readAsDataURL(this.files[0]);
            }
        });
    }
    
    // Admin dashboard charts (if Chart.js is loaded)
    if (typeof Chart !== 'undefined') {
        // Sales Chart
        var salesChartCanvas = document.getElementById('salesChart');
        if (salesChartCanvas) {
            var salesChart = new Chart(salesChartCanvas, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                    datasets: [{
                        label: 'Sales',
                        data: [12, 19, 3, 5, 2, 3, 10, 15, 8, 12, 13, 17],
                        backgroundColor: 'rgba(78, 115, 223, 0.05)',
                        borderColor: '#4e73df',
                        borderWidth: 2,
                        pointBackgroundColor: '#4e73df',
                        pointBorderColor: '#fff',
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: '#4e73df',
                        pointHoverBorderColor: '#fff',
                        tension: 0.3
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }
        
        // Products by Category Chart
        var categoryChartCanvas = document.getElementById('categoryChart');
        if (categoryChartCanvas) {
            var categoryChart = new Chart(categoryChartCanvas, {
                type: 'pie',
                data: {
                    labels: ['Fruits', 'Vegetables', 'Bakery', 'Dairy', 'Beverages'],
                    datasets: [{
                        data: [12, 19, 8, 15, 10],
                        backgroundColor: [
                            '#4e73df',
                            '#1cc88a',
                            '#36b9cc',
                            '#f6c23e',
                            '#e74a3b'
                        ],
                        hoverOffset: 4
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                }
            });
        }
    }
});