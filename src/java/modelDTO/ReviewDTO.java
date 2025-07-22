/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDTO;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class ReviewDTO {
    private int reviewID ;
    private int bookID;
    private String accID;
    private String comment;
    private Timestamp date;
    private int rating ;

    public ReviewDTO() {
    }
    
    
    public ReviewDTO(int reviewID, int bookID, String accID, String comment, Timestamp date, int rating) {
        this.reviewID = reviewID;
        this.bookID = bookID;
        this.accID = accID;
        this.comment = comment;
        this.date = date;
        this.rating = rating;
    }

    public ReviewDTO(int bookID, String accID, String comment, int rating) {
        this.bookID = bookID;
        this.accID = accID;
        this.comment = comment;
        this.rating = rating;
    }
    
    
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getAccID() {
        return accID;
    }

    public void setAccID(String accID) {
        this.accID = accID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" + "reviewID=" + reviewID + ", bookID=" + bookID + ", accID=" + accID + ", comment=" + comment + ", date=" + date + ", rating=" + rating + '}';
    }
    
    
}
