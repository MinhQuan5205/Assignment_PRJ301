/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
//này giúp mình có tạo chức năng thu gọn hay thu nhỏ bộ lọc tìm kiếm
document.addEventListener('DOMContentLoaded', function () {
    const filterHeaders = document.querySelectorAll('.filter-header');
    filterHeaders.forEach(header => {
        header.addEventListener('click', function () {
            const filterGroup = this.closest('.filter-group');
            const filterOptions = filterGroup.querySelector('.filter-options');
            const icon = this.querySelector('i');
            filterOptions.classList.toggle('active');
            if (filterOptions.classList.contains('active')) {
                filterOptions.style.display = 'block';
                icon.classList.replace('fa-chevron-down', 'fa-chevron-up');
            } else {
                filterOptions.style.display = 'none';
                icon.classList.replace('fa-chevron-up', 'fa-chevron-down');
            }
        });
    });

    const allFilterOptions = document.querySelectorAll('.filter-options');
    allFilterOptions.forEach(options => {
        options.style.display = 'none';
    });
});

//này giúp mình tạo hiệu ứng cuộn mượt hơn khi đến một phần tử chứ không phải đến 1 cách đột ngột
document.querySelectorAll('.scroll-link').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const targetId = this.getAttribute('href');
        const targetElement = document.querySelector(targetId);
        if (targetElement) {
            window.scrollTo({
                top: targetElement.offsetTop - 50,
                behavior: 'smooth'
            });
        }
    });
});

//Hàm reset bộ lọc về lại trạng thái ban đầu
function resetFilter() {
    window.location.href = "MainController?action=listBook"; 
}
