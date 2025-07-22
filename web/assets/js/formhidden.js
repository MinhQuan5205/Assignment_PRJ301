/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//dùng để hiện hoặc ẩn đi thanh tìm kiếm khi người dùng bấm vào biểu tượng tìm kiếm
document.addEventListener('DOMContentLoaded', function() {
    const searchIconTrigger = document.getElementById('search-icon-trigger');
    const searchBox = document.getElementById('search-box');
    if (searchIconTrigger && searchBox) {
        searchIconTrigger.addEventListener('click', function(event) {
            event.preventDefault(); 
            searchBox.classList.toggle('hidden'); 
        });
    }
});
