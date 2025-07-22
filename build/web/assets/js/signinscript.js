/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

document.addEventListener("DOMContentLoaded", function () {
    //hiển thị hiệu ứng loaded 
    document.body.classList.add("loaded");
    
    //tự động tick nếu cookie 'user' đã tồn tại
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    if (getCookie("user")) {
        const checkbox = document.querySelector("input[name='checklogin']");
        if (checkbox) checkbox.checked = true;
    }    
    
    //Thêm hiệu ứng khi click chuyển trang 
    const links = document.querySelectorAll("a.signup, a.register-btn");
    links.forEach(link => {
        link.addEventListener("click", function(event){
            event.preventDefault();
            let destination = this.href;
            
            //thêm hiệu ứng trước khi rời trang
            document.body.style.transition = "opacity 0.5s ease, transform 0.5s ease";
            document.body.style.opacity = "0";
            document.body.style.transform = "translateY(-20px)";
            
            setTimeout(() => {
                window.location.href = destination;
            }, 500);
        });
    });
});
