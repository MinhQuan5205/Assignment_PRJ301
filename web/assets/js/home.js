/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
//để tạo hiệu ứng trượt ngang (horizontal scroll) cho slider sản phẩm 
document.addEventListener('DOMContentLoaded', function () {
    const productSlider = document.querySelector('.products-slider');
    const sliderPrevBtn = document.querySelector('.slider-prev');
    const sliderNextBtn = document.querySelector('.slider-next');
    if (sliderPrevBtn && sliderNextBtn && productSlider) {
        sliderNextBtn.addEventListener('click', () => {
            productSlider.scrollLeft += productSlider.offsetWidth;
        });
        sliderPrevBtn.addEventListener('click', () => {
            productSlider.scrollLeft -= productSlider.offsetWidth;
        });
    }
});

