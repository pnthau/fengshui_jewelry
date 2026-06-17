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

// Export for Node environments (if any) to keep it fully cross-compatible
if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {
    module.exports = {
        getElementByYear,
        getNewQuantity
    };
}
