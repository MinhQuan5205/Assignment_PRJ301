/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function() {
    const qtyMinusBtn = document.querySelector('.qty-btn.minus');//nút giảm số lượng 
    const qtyPlusBtn = document.querySelector('.qty-btn.plus');//nút tăng số lượng sản phẩm 
    const qtyInput = document.querySelector('.qty-input');//ô nhập số lượng sản phẩm 
    const totalPriceAmount = document.querySelector('.total-amount');//nơi hiển thị tổng tiền 
    const productCard = document.querySelector('.product-card');//thẻ để chứa thông tin sản phẩm 
    
    let basePrice = 0;
    let discount = 0;

    if (productCard && productCard.dataset) { //lấy giá giảm và giảm giá tư html
        basePrice = parseFloat(productCard.dataset.basePrice || "0");
        discount = parseFloat(productCard.dataset.discount || "0");
    }
    
    function updateTotalPrice() {//hàm này giúp tính tổng tiền và cập nhật hiển thị 
        const quantity = parseInt(qtyInput.value);
        let discountedPrice = basePrice * (1 - discount / 100);
        const totalPrice = discountedPrice * quantity;

        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0, 
            maximumFractionDigits: 0
        });
        totalPriceAmount.textContent = formatter.format(totalPrice);
    }
    
    //hàm này bắt sự kiện khi bấm nút tăng và nút giảm 
    qtyMinusBtn.addEventListener('click', (event) => {
        event.preventDefault();
        let currentQty = parseInt(qtyInput.value);
        if (currentQty > 1) {
            qtyInput.value = currentQty - 1;
            updateTotalPrice();
        }
    });

    qtyPlusBtn.addEventListener('click', (event) => {
        event.preventDefault();
        let currentQty = parseInt(qtyInput.value);
        qtyInput.value = currentQty + 1;
        updateTotalPrice();
    });
    
    //để bắt được sự kiện người dùng thay đổi số lượng thủ công
    qtyInput.addEventListener('change', () => {
        let currentQty = parseInt(qtyInput.value);
        if (isNaN(currentQty) || currentQty < 1) {
            qtyInput.value = 1;
        }
        updateTotalPrice();
    });
     updateTotalPrice();
});
