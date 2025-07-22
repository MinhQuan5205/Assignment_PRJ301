/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDTO;

import modelDAO.BookDAO;

/**
 *
 * @author ACER
 */
public class CartDTO {
    private int cartID;
    private String accID;
    private int bookID;
    private int quantity;
    private BookDTO book;
    
    public CartDTO() {
    }

    public CartDTO(int cartID, String accID, int bookID, int quantity) {
        this.cartID = cartID;
        this.accID = accID;
        this.bookID = bookID;
        this.quantity = quantity;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public String getAccID() {
        return accID;
    }

    public void setAccID(String accID) {
        this.accID = accID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }
    
    BookDAO bdao = new BookDAO();
    public double getTotalPrice(){
        BookDTO book = bdao.getBookById(bookID);
        double price = book.getPrice();
        if(book.getDiscount() > 0){
            price -= price * (book.getDiscount()/100.0);
        }
        return price * quantity; 
    }
}
