// --- CART LOGIC (LocalStorage) ---
let cart = [];

function updateCartBadge() {
    const totalQty = cart.reduce((sum, item) => sum + item.quantity, 0);
    document.getElementById('cartBadgeCount').innerText = totalQty;
}

async function addToCart(event, id, name, price, image) {
    let existingItem = cart.find(item => item.id === id);
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({id, name, price, image, quantity: 1});
    }
    const url = CONTEXT_PATH + '/cart/add';
    const params = new URLSearchParams();
    params.append('productId', id);
    params.append('quantity', 1);
    const resp = await fetch(url, {
        method: 'post',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    });

    // Gọi hiệu ứng bay bay
    createFlyingEffect(event, image);
}

function createFlyingEffect(event, imagePath) {
    const startX = event.clientX;
    const startY = event.clientY;

    const cartBtn = document.querySelector('.floating-cart-btn');
    const targetRect = cartBtn.getBoundingClientRect();
    const targetX = targetRect.left + targetRect.width / 2;
    const targetY = targetRect.top + targetRect.height / 2;

    const flyingObj = document.createElement('img');
    flyingObj.src = imagePath.startsWith('http') ? imagePath : CONTEXT_PATH + '/' + imagePath;
    flyingObj.style.position = 'fixed';
    flyingObj.style.left = startX + 'px';
    flyingObj.style.top = startY + 'px';
    flyingObj.style.width = '60px';
    flyingObj.style.height = '60px';
    flyingObj.style.objectFit = 'cover';
    flyingObj.style.borderRadius = '50%';
    flyingObj.style.border = '3px solid #e67e22';
    flyingObj.style.boxShadow = '0 0 20px rgba(243, 156, 18, 0.8)';
    flyingObj.style.zIndex = '99999';
    flyingObj.style.transition = 'all 2.0s cubic-bezier(0.25, 0.8, 0.25, 1)';
    flyingObj.style.transform = 'translate(-50%, -50%) scale(1)';
    flyingObj.style.pointerEvents = 'none';

    document.body.appendChild(flyingObj);

    // Chờ 50ms để CSS áp dụng vị trí ban đầu rồi mới animate
    setTimeout(() => {
        flyingObj.style.left = targetX + 'px';
        flyingObj.style.top = targetY + 'px';
        flyingObj.style.transform = 'translate(-50%, -50%) scale(0.1)';
        flyingObj.style.opacity = '0.3';
    }, 50);

    // Sau 2000ms khi bay đến nơi thì xóa img và rung giỏ hàng từ tốn
    setTimeout(() => {
        flyingObj.remove();
        cartBtn.classList.add('cart-bounce-anim');
        setTimeout(() => cartBtn.classList.remove('cart-bounce-anim'), 1500);
        updateCartBadge();
    }, 2050);
}

function openCartModal() {
    renderCartItems();
    const cartModal = new bootstrap.Modal(document.getElementById('cartModal'));
    cartModal.show();
}

function renderCartItems() {
    const container = document.getElementById('cartItemsContainer');
    const priceEl = document.getElementById('cartTotalPrice');
    container.innerHTML = '';

    if (cart.length === 0) {
        container.innerHTML = '<p class="text-center text-secondary my-4">Giỏ hàng đang trống.</p>';
        priceEl.innerText = '0 VNĐ';
        return;
    }

    let totalPrice = 0;
    cart.forEach((item, index) => {
        totalPrice += item.price * item.quantity;
        let borderClass = 'border-bottom border-secondary';
        if (cart.length > 1 && index === cart.length - 1) {
            borderClass = '';
        }

        container.innerHTML += `
            <div class="d-flex align-items-start mb-3 p-2 ${borderClass}">
                <img src="${item.image.startsWith('http') ? item.image : CONTEXT_PATH + '/' + item.image}" alt="" style="width: 75px; height: 75px; object-fit: cover; border-radius: 8px;" class="me-3 mt-1 shadow">
                
                <div class="flex-grow-1 pe-2">
                    <div class="text-white fw-medium mb-1" style="font-size: 18px; line-height: 1.3;">${item.name}</div>
                    <div class="text-warning fw-bold mb-1" style="font-size: 16px;">${Number(item.price).toLocaleString('vi-VN')} VNĐ <span class="text-warning fw-bold"> x ${item.quantity}</span></div>
                    <div id="qty-input-container-${index}" class="d-none mt-1">
                        <div class="input-group input-group-sm" style="width: 90px;">
                            <input type="number" id="qty-input-${index}" class="form-control bg-dark text-white border-warning text-center px-1" value="${item.quantity}" onkeypress="if(event.key === 'Enter') saveCustomQuantity(${index})">
                            <button class="btn btn-warning border-warning px-2" onclick="saveCustomQuantity(${index})"><i class="bi bi-check-lg"></i></button>
                        </div>
                    </div>
                </div>
                
                <div class="d-flex flex-column gap-2 ms-auto">
                    <button class="btn btn-sm btn-outline-success border-success shadow-sm" style="padding: 2px 6px; font-weight: bold; font-size: 12px;" onclick="changeQuantity(${index}, 1)" title="Tăng số lượng">+1</button>
                    <button class="btn btn-sm btn-outline-secondary border-secondary text-white shadow-sm" style="padding: 2px 6px; font-weight: bold; font-size: 12px;" onclick="changeQuantity(${index}, -1)" title="Giảm số lượng">-1</button>
                    <button class="btn btn-sm btn-outline-warning border-warning shadow-sm" style="padding: 2px 6px;" onclick="enableCustomQuantity(${index})" title="Tùy Duyên (Nhập tay)"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-outline-danger border-danger shadow-sm" style="padding: 2px 6px;" onclick="removeFromCart(${index})" title="Xóa món"><i class="bi bi-trash"></i></button>
                </div>
            </div>
        `;
    });
    priceEl.innerText = Number(totalPrice).toLocaleString('vi-VN') + ' VNĐ';
}

async function removeFromCart(index) {
    let id = cart[index].id;

    cart.splice(index, 1);

    const url = CONTEXT_PATH + "/cart/remove";
    const params = new URLSearchParams();
    params.append('productId', id);

    const res = await fetch(url, {
        method: 'post',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    })

    updateCartBadge();
    renderCartItems();
}

async function changeQuantity(index, delta) {
    if (cart[index]) {
        const id = cart[index].id;

        cart[index].quantity += delta;
        if (cart[index].quantity <= 0) {
            await removeFromCart(index);
            return;
        }
        const url = CONTEXT_PATH + '/cart/update';
        const params = new URLSearchParams();
        params.append('productId', id);
        params.append('quantity', cart[index].quantity);
        const res = await fetch(url, {
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        });

        updateCartBadge();
        renderCartItems();
    }
}

function enableCustomQuantity(index) {
    const inputContainer = document.getElementById('qty-input-container-' + index);
    const inputDiv = document.getElementById('qty-input-' + index);
    if (inputContainer && inputDiv) {
        inputContainer.classList.remove('d-none');
        inputDiv.focus();
        inputDiv.select();
    }
}

async function saveCustomQuantity(index) {
    const inputDiv = document.getElementById('qty-input-' + index);
    if (inputDiv && cart[index]) {
        let newQty = parseInt(inputDiv.value);
        if (isNaN(newQty) || newQty <= 0) {
            // Nếu nhập bậy bạ (chữ, số âm), thì trả về số cũ
            newQty = cart[index].quantity;
        }
        const id = cart[index].id;
        cart[index].quantity = newQty;

        const url = CONTEXT_PATH + '/cart/update';
        const params = new URLSearchParams();
        params.append('productId', id);
        params.append('quantity', cart[index].quantity);
        const res = await fetch(url, {
            method: 'post',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: params.toString()
        });

        updateCartBadge();
        renderCartItems();
    }
}

async function checkoutCart() {
    if (cart.length === 0) {
        alert('⚠️ Giỏ hàng của bạn đang trống!');
        return;
    }

    const name = document.getElementById('cartCustomerName').value.trim();
    const phone = document.getElementById('cartCustomerPhone').value.trim();
    const addr = document.getElementById('cartCustomerAddress').value.trim();

    if (!name || !phone || !addr) {
        alert('Vui lòng điền đầy đủ thông tin (Họ tên, SĐT, Địa chỉ) để chúng tôi giao hàng!');
        return;
    }

    const url = CONTEXT_PATH + "/order";
    const params = new URLSearchParams();
    params.append('customerName', name);
    params.append('customerPhone', phone);
    params.append('customerAddress', addr);

    const res = await fetch(url, {
        method: 'post',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: params.toString()
    });

    if (res.ok) {
        console.log("Đơn hàng Giỏ hàng:", {name, phone, address: addr, items: cart});

        alert('🎉 Chúc mừng Ông/Bà ' + name + ' đã đặt hàng thành công! Nhân viên phong thủy sẽ gọi lại ngay để xác nhận.');
        // Reset cart
        cart = [];
        updateCartBadge();
        const cartModal = bootstrap.Modal.getInstance(document.getElementById('cartModal'));
        if (cartModal) cartModal.hide();
    }
}

// Thiết lập tọa độ xuất phát/hút vào cho Modal từ Nút Giỏ Hàng
const cartModalEl = document.getElementById('cartModal');
if (cartModalEl) {
    cartModalEl.addEventListener('show.bs.modal', function () {
        const cartBtn = document.querySelector('.floating-cart-btn');
        if (cartBtn) {
            const btnRect = cartBtn.getBoundingClientRect();
            const btnCenterX = btnRect.left + btnRect.width / 2;
            const btnCenterY = btnRect.top + btnRect.height / 2;

            const windowCenterX = window.innerWidth / 2;
            const windowCenterY = window.innerHeight / 2;

            const dx = btnCenterX - windowCenterX;
            const dy = btnCenterY - windowCenterY;

            cartModalEl.style.setProperty('--fly-x', dx + 'px');
            cartModalEl.style.setProperty('--fly-y', dy + 'px');
        }
    });
}

// --- Subliminal Animation (Rung 1 nút ngẫu nhiên trong màn hình) ---
function initRandomTwitch() {
    const btns = document.querySelectorAll('.btn-add-cart-icon');
    let visibleBtns = [];

    // Theo dõi xem nút nào đang nằm trong tầm nhìn của khách hàng
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                if (!visibleBtns.includes(entry.target)) {
                    visibleBtns.push(entry.target);
                }
            } else {
                visibleBtns = visibleBtns.filter(btn => btn !== entry.target);
            }
        });
    }, {threshold: 0.1});

    btns.forEach(btn => observer.observe(btn));

    function scheduleNextGlobalTwitch() {
        // Chờ ngẫu nhiên từ 6 đến 12 giây
        const randomDelay = Math.random() * 6000 + 6000;
        setTimeout(() => {
            if (visibleBtns.length > 0) {
                // Chỉ chọn ngẫu nhiên 1 nút duy nhất trong số các nút đang hiện trên màn hình
                const randomIndex = Math.floor(Math.random() * visibleBtns.length);
                const selectedBtn = visibleBtns[randomIndex];

                if (!selectedBtn.matches(':hover')) {
                    selectedBtn.classList.add('btn-twitch-anim');
                    setTimeout(() => selectedBtn.classList.remove('btn-twitch-anim'), 600);
                }
            }
            scheduleNextGlobalTwitch();
        }, randomDelay);
    }

    // Kích hoạt người điều phối rung
    scheduleNextGlobalTwitch();
}

// --- MAGIC CURSOR (Con chuột ảo thu nhỏ) ---
function initMagicCursor() {
    const magicCursor = document.createElement('div');
    magicCursor.classList.add('magic-cursor');
    document.body.appendChild(magicCursor);

    // Lắng nghe mousemove trên body để di chuyển cursor
    document.addEventListener('mousemove', (e) => {
        magicCursor.style.left = e.clientX + 'px';
        magicCursor.style.top = e.clientY + 'px';
    });

    // Gắn event cho các vùng ảnh sản phẩm
    const imgWrappers = document.querySelectorAll('.product-img-wrapper');
    imgWrappers.forEach(wrapper => {
        wrapper.addEventListener('mouseenter', () => {
            magicCursor.classList.add('active-on-card');
        });
        wrapper.addEventListener('mouseleave', () => {
            magicCursor.classList.remove('active-on-card');
            magicCursor.classList.remove('active-on-btn');
        });

        // Gắn event riêng cho nút bấm trong hình
        const addBtn = wrapper.querySelector('.btn-add-cart-icon');
        if (addBtn) {
            addBtn.addEventListener('mouseenter', () => {
                magicCursor.classList.add('active-on-btn');
            });
            addBtn.addEventListener('mouseleave', () => {
                magicCursor.classList.remove('active-on-btn');
            });
        }
    });
}

// Khởi tạo
document.addEventListener('DOMContentLoaded', () => {
    updateCartBadge();
    initRandomTwitch();
    initMagicCursor();
});
