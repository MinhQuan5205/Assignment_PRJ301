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
public class BookDTO {
    private int bookID; 
    private String title;
    private double price;
    private String description;
    private int genreID;
    private int authorID;
    private String imageURL;
    private double discount;
    private int stock;
    private double rating;

    public BookDTO() {
    }

    public BookDTO(int bookID, String title, double price, String description, int genreID, int authorID, String imageURL, double discount, int stock) {
        this.bookID = bookID;
        this.title = title;
        this.price = price;
        this.description = description;
        this.genreID = genreID;
        this.authorID = authorID;
        this.imageURL = imageURL;
        this.discount = discount;
        this.stock = stock;
    }

    public BookDTO(int bookID, String title, double price, String description, int genreID, int authorID, String imageURL, double discount, int stock, double rating) {
        this.bookID = bookID;
        this.title = title;
        this.price = price;
        this.description = description;
        this.genreID = genreID;
        this.authorID = authorID;
        this.imageURL = imageURL;
        this.discount = discount;
        this.stock = stock;
        this.rating = rating;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getRating() {
       BookDAO bdao = new BookDAO();
       return bdao.getRatingByBookID(bookID);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public double getFinalPrice(){
        double finalPrice = price;
        if(this.discount > 0){
            finalPrice -= this.price * (this.discount/100.0);
        }
        return finalPrice;
    }   
}
