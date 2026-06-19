/**
 * Feng Shui Jewelry - Pure Utility Functions for Unit Testing
 */

/**
 * Calculates the Feng Shui element based on the Lunar/Solar Stem-Branch calculation method.
 * @param {number} year - Birth year (AD)
 * @returns {string|null} - "KIM", "MOC", "THUY", "HOA", "THO" or null if invalid
 */
function getElementByYear(year) {
    if (isNaN(year) || year < 1900 || year > new Date().getFullYear()) {
        return null;
    }

    const elementsMapping = {
        1: "MOC",
        2: "KIM",
        3: "THUY",
        4: "HOA",
        5: "THO"
    };

    // Index calculation based on reference year
    const heavenlyStemsIndex = (year - 4) % 10;
    const earthlyBranchesIndex = (year - 4) % 12;

    const heavenlyStemsVal = Math.floor(heavenlyStemsIndex / 2) + 1;
    const earthlyBranchesVal = Math.floor(earthlyBranchesIndex % 6 / 2) + 1;

    const totalVal = (heavenlyStemsVal + earthlyBranchesVal);
    const elementCode = totalVal > 5 ? totalVal - 5 : totalVal;
    
    return elementsMapping[elementCode];
}

/**
 * Calculates the bounded quantity for an order item (+/-).
 * @param {number} currentQty - The current quantity in the input field
 * @param {number} val - The delta change (+1 or -1)
 * @returns {number} - Bounded quantity between 1 and 99
 */
function getNewQuantity(currentQty, val) {
    let nextQty = (parseInt(currentQty) || 1) + val;
    if (nextQty < 1) nextQty = 1;
    if (nextQty > 99) nextQty = 99;
    return nextQty;
}

/**
 * Validates a Vietnamese phone number (9 to 12 digits, ignoring spaces, dots, hyphens).
 * @param {string} phone - The input phone number
 * @returns {boolean} - true if valid, false otherwise
 */
function validatePhoneNumber(phone) {
    if (!phone) return false;
    const cleanPhone = phone.replace(/[\s.-]/g, '');
    return /^\d{9,12}$/.test(cleanPhone);
}

/**
 * Validates the order input fields.
 * @param {string} name - Customer name
 * @param {string} phone - Customer phone
 * @param {string} address - Customer address
 * @returns {string|null} - Null if valid, or an error message string if invalid
 */
function validateOrderInput(name, phone, address) {
    if (!name || !name.trim()) {
        return "Vui lòng nhập Họ và Tên của Ông/Bà.";
    }
    if (!phone || !phone.trim()) {
        return "Vui lòng nhập Số điện thoại.";
    }
    if (!address || !address.trim()) {
        return "Vui lòng nhập Địa chỉ nhận hàng.";
    }
    if (!validatePhoneNumber(phone)) {
        return "Số điện thoại không hợp lệ (từ 9 đến 12 chữ số).";
    }
    return null;
}

// Export for Node environments (if any) to keep it fully cross-compatible
if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {
    module.exports = {
        getElementByYear,
        getNewQuantity,
        validatePhoneNumber,
        validateOrderInput
    };
}
