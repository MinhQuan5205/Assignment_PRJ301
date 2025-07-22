/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function () {
    //khai báo các biến cần phải có 
    const quantityInputs = document.querySelectorAll('.quantity-input');
    const cartTotalAmount = document.querySelector('.cart-total strong');
    const cartCheckboxes = document.querySelectorAll('.cart-checkbox');
    const checkoutButton = document.getElementById('checkout-button');
    //duyệt từng sản phẩm trong giỏ hàng
    quantityInputs.forEach(input => {
        const cartItemRow = input.closest('tr');
        const priceCell = cartItemRow.querySelectorAll('td')[2];
        const itemTotalCell = cartItemRow.querySelector('.item-total');
        const cartCheckbox = cartItemRow.querySelector('.cart-checkbox');
        let basePriceText = priceCell.textContent.trim();
        //chuyển giá tiền từ chuỗi sang số 
        let basePriceValue = parseFloat(basePriceText.replace(/[^\d,-]/g, '').replace('.', '').replace(',', '.'));
        if (isNaN(basePriceValue)) {
            console.error("Invalid base price found in price cell:", priceCell);
            basePriceValue = 0;
        }
        let basePrice = basePriceValue;
        //cập nhật thành tiền cho từng sản phẩm 
        function updateItemTotalPrice() {
            const quantity = parseInt(input.value);
            if (isNaN(quantity) || quantity < 1) {
                input.value = 1;
                return;
            }
            const itemTotalPrice = basePrice * quantity;

            const localeVN = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'});
            itemTotalCell.textContent = localeVN.format(itemTotalPrice);

            updateCartTotal();
        }
        //gắn các sự kiện cho nhập số lượng và checkbox
        input.addEventListener('change', updateItemTotalPrice);
        input.addEventListener('keyup', updateItemTotalPrice);
        cartCheckbox.addEventListener('change', function () {
            updateCartTotal();
        });
        updateItemTotalPrice();
    });
    //tính tổng tiền cho giỏ hàng 
    function updateCartTotal() {
        let overallTotalPrice = 0;
        document.querySelectorAll('tbody tr').forEach(row => {
            const totalPriceCell = row.querySelector('.item-total');
            const cartCheckbox = row.querySelector('.cart-checkbox');

            if (cartCheckbox.checked) {
                let totalPriceText = totalPriceCell.textContent.trim();
                let totalPriceValue = parseFloat(totalPriceText.replace(/[^\d,-]/g, '').replace('.', '').replace(',', '.'));
                if (!isNaN(totalPriceValue)) {
                    overallTotalPrice += totalPriceValue;
                }
            }
        });
        const localeVN = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'});
        cartTotalAmount.textContent = localeVN.format(overallTotalPrice);
    }
    cartCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateCartTotal);
    });
    updateCartTotal();
    //xử lý khi nhấn nút thanh toán 
    checkoutButton.addEventListener('click', function (event) {
        event.preventDefault();
        const selectedItems = [];
        cartCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                selectedItems.push(checkbox.value);
            }
        });
        if (selectedItems.length > 0) {
            alert("Proceeding to checkout with Cart IDs: " + selectedItems.join(', '));
            const form = document.getElementById('checkout-form');
            form.submit();
        } else {
            alert("Vui lòng chọn sản phẩm để thanh toán.");
        }
    });
});
