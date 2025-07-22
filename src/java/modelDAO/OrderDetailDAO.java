/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelDTO.OrderDetailDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class OrderDetailDAO {
    
    //câu truy vấn 
    private static final String GET_ORDERDETAILS_BY_ORDERID = "SELECT * FROM OrderDetail WHERE orderID = ?";
    private static final String CREATE_ORDERDETAIL = "INSERT INTO OrderDetail (orderID, bookID, quantity) VALUES (?, ?, ?)";
    private static final String CACULATE_TOTAL_PRICE = "SELECT od.quantity, b.price FROM OrderDetail od "
                                                        + "JOIN Book b ON od.bookID = b.bookID"
                                                        + "WHERE od.orderID = ?";
    
    public OrderDetailDAO() {
    }
    
    public List<OrderDetailDTO> getOrderDetailsByOrderId(int orderId){
        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ORDERDETAILS_BY_ORDERID);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            
            while(rs.next()){
                orderDetailList.add(new OrderDetailDTO(
                        rs.getInt("orderDetailID"),
                        rs.getInt("orderID"),
                        rs.getInt("bookID"),
                        rs.getInt("quantity")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in  getOrderDetailsByOrderId(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return orderDetailList;
    }
    
    public void createOrderDetail(OrderDetailDTO orderDetail){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CREATE_ORDERDETAIL);
            
            ps.setInt(1, orderDetail.getOrderID());
            ps.setInt(2, orderDetail.getBookID());
            ps.setInt(3, orderDetail.getQuantity());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error in  createOrderDetail(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    public double calculateTotalPrice(int orderId){
        double totalPrice = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CACULATE_TOTAL_PRICE);
            ps.setInt(1, orderId);
            
            rs = ps.executeQuery();
            while(rs.next()){
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                totalPrice += quantity * price;
            }
        } catch (Exception e) {
            System.err.println("Error in  calculateTotalPrice(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return totalPrice;
    }
    
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
