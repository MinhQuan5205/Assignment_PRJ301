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
public class OrderDTO {
    private int orderID ;
    private String userID;
    private String create_at ;
    private String status;
    private double totalPrice;
    private String address;
    private String receiver_name;
    private String receiver_phone;

    public OrderDTO() {
    }

    public OrderDTO(int orderID, String userID, String create_at, String status, double totalPrice, String address, String receiver_name, String receiver_phone) {
        this.orderID = orderID;
        this.userID = userID;
        this.create_at = create_at;
        this.status = status;
        this.totalPrice = totalPrice;
        this.address = address;
        this.receiver_name = receiver_name;
        this.receiver_phone = receiver_phone;
    }

    
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }
    
}
