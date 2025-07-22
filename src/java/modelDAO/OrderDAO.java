/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelDTO.BookDTO;
import modelDTO.CartDTO;
import modelDTO.OrderDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class OrderDAO {
    
    //câu truy vấn
    private static final String GET_CART_ITEM = "SELECT * FROM Cart WHERE cartID = ? AND accID = ?";
    private static final String GET_BOOK_FOR_CART = "SELECT * FROM Book WHERE bookID = ?";
    private static final String GET_ORDER_BY_ORDERID = "SELECT * FROM [Order] WHERE orderID = ?";
    private static final String GET_ORDER_BY_ACCID = "SELECT * FROM [Order] WHERE userID = ? ORDER BY create_at DESC";   
    private static final String CREATE_ORDER = "INSERT INTO [Order] (userID, create_at, totalPrice, address, receiver_name, receiver_phone) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ORDER_STATUS = "UPDATE [Order] SET status = ? WHERE orderID = ?";
    private static final String REMOVE_CART_ITEM = "DELETE FROM Cart WHERE cartID = ? AND accID = ?";
    
    
    public OrderDAO() {
    }
    
    public CartDTO getCartItem(int cartID, String accID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_CART_ITEM);
            ps.setInt(1, cartID);
            ps.setString(2, accID);
            rs = ps.executeQuery();
            
            while(rs.next()){
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getInt("cartID"));
                cart.setAccID(rs.getString("accID"));
                cart.setBookID(rs.getInt("booID"));
                cart.setQuantity(rs.getInt("quantity"));
                return cart;
            }
        } catch (Exception e) {
            System.err.println("Error in getCartItem(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public BookDTO getBookForCart(int bookID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_BOOK_FOR_CART);
            ps.setInt(1, bookID);
            rs = ps.executeQuery();
            while(rs.next()){
                BookDTO book = new BookDTO();
                book.setBookID(rs.getInt("bookID"));
                book.setTitle(rs.getString("title"));
                book.setPrice(rs.getDouble("price"));
                book.setImageURL(rs.getString("imageURL"));
                return book;
            }
        } catch (Exception e) {
            System.err.println("Error in getBookForCart(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public OrderDTO getOrderById(int orderID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ORDER_BY_ORDERID);
            ps.setInt(1, orderID);
            rs = ps.executeQuery();
            
            while(rs.next()){
                return new OrderDTO(
                        rs.getInt("orderID"),
                        rs.getString("userID"),
                        rs.getString("create_at"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice"),
                        rs.getString("address"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_phone")
                );
            }
        } catch (Exception e) {
            System.err.println("Error in getOrderById(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public List<OrderDTO> getOrdersByAccID(String accID){
        List<OrderDTO> ordersList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ORDER_BY_ACCID);
            ps.setString(1, accID);
            rs = ps.executeQuery();
            
            while(rs.next()){
                ordersList.add(new OrderDTO(
                        rs.getInt("orderID"),
                        rs.getString("userID"),
                        rs.getString("create_at"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice"),
                        rs.getString("address"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_phone")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in getOrdersByAccID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return ordersList;
    }
    
    public List<OrderDTO> getAllOrders(String sort){
        List<OrderDTO> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM [Order]";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        if(sort != null){
            switch (sort) {
                case "date_asc":
                    sql += " ORDER BY create_at ASC";
                    break;
                case "date_desc":
                    sql += " ORDER BY create_at DESC";
                    break;
                case "total_asc":
                    sql += " ORDER BY totalPrice ASC";
                    break;
                case "total_desc":
                    sql += " ORDER BY totalPrice DESC";
                    break;
            }
        }
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()){
                ordersList.add(new OrderDTO(
                        rs.getInt("orderID"),
                        rs.getString("userID"),
                        rs.getString("create_at"),
                        rs.getString("status"),
                        rs.getDouble("totalPrice"),
                        rs.getString("address"),
                        rs.getString("receiver_name"),
                        rs.getString("receiver_phone")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in getAllOrders(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return ordersList;
    }
    
    public int createOrder(OrderDTO order){//trả về id của đơn hàng vừa mới tạo
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, order.getUserID());
            ps.setString(2, order.getCreate_at());
            ps.setDouble(3, order.getTotalPrice());
            ps.setString(4, order.getAddress());
            ps.setString(5, order.getReceiver_name());
            ps.setString(6, order.getReceiver_phone());
            
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected > 0){
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);//trả về id của đơn hàng vừa mới thêm
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in createOrder(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return -1;
    }
    
    public boolean updateOrderStatus(int orderID, String status){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_ORDER_STATUS);
            ps.setString(1, status);
            ps.setInt(2, orderID);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.err.println("Error in updateOrderStatus(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return false;
    }
    
    public void removeCartItem(int cartID, String accID) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(REMOVE_CART_ITEM);
            ps.setInt(1, cartID);
            ps.setString(2, accID);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error in removeCartItem(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    public List<OrderDTO> getListByPage(List<OrderDTO> orderList, int start, int end) {
        ArrayList<OrderDTO> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            list.add(orderList.get(i));
        }
        return list;
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
