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
import modelDTO.BookDTO;
import modelDTO.CartDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class CartDAO {

    //Câu truy vấn
    private static final String GET_BASIC_CART_BY_ACCID = "SELECT * FROM Cart WHERE accID = ?";
    private static final String GET_CART_BY_CARTID = "SELECT * FROM Cart WHERE cartID = ?";
    private static final String GET_CART_BY_ACCID = "SELECT c.cartID, c.bookID, c.quantity, b.title, b.price, b.imageURL "
                                                    + "FROM Cart c "
                                                    + "JOIN Book b ON c.bookID = b.bookID "
                                                    + "WHERE c.accID = ?";
    private static final String REMOVE_CART_ITEM_BY_ACCID = "DELETE FROM Cart WHERE cartID = ? AND accID = ?";
    private static final String UPDATE_CART_QUANTITY = "UPDATE Cart SET quantity = ? WHERE cartID = ?";
    private static final String REDUCE_STOCK = "UPDATE Book SET stock = stock - ? WHERE bookID = ? AND stock >= ?";

    public CartDAO() {
    }

    public List<CartDTO> getBasicCartByAccID(String accID) {
        List<CartDTO> cartList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_BASIC_CART_BY_ACCID);
            ps.setString(1, accID);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getInt("cartID"));
                cart.setAccID(rs.getString("accID"));
                cart.setBookID(rs.getInt("bookID"));
                cart.setQuantity(rs.getInt("quantity"));
                cartList.add(cart);
            }
        } catch (Exception e) {
            System.err.println("Error in getBasicCartByAccID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return cartList;
    }

    public CartDTO getCartByID(int cartID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_CART_BY_CARTID);
            ps.setInt(1, cartID);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getInt("cartID"));
                cart.setAccID(rs.getString("accID"));
                cart.setBookID(rs.getInt("bookID"));
                cart.setQuantity(rs.getInt("quantity"));
                return cart;
            }
        } catch (Exception e) {
            System.err.println("Error in getCartByID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public List<CartDTO> getCartByAccID(String accID) {
        List<CartDTO> cartList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_CART_BY_ACCID);
            ps.setString(1, accID);
            rs = ps.executeQuery();
            
            while(rs.next()){
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getInt("cartID"));
                cart.setBookID(rs.getInt("bookID"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setAccID(accID);
                
                BookDTO book = new BookDTO();
                book.setBookID(rs.getInt("bookID"));
                book.setTitle(rs.getString("title"));
                book.setPrice(rs.getDouble("price"));
                book.setImageURL(rs.getString("imageURL"));
                
                cart.setBook(book);
                cartList.add(cart);
            }
        } catch (Exception e) {
            System.err.println("Error in getCartByAccID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return cartList;
    }

    public boolean removeCartItem(int cartID, String accID){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
           conn = DbUtils.getConnection();
           ps = conn.prepareStatement(REMOVE_CART_ITEM_BY_ACCID);
           ps.setInt(1, cartID);
           ps.setString(2, accID);
           int rowsAffected = ps.executeUpdate();
           success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in removeCartItem(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    public void addItemToCart(String accID, int bookID, int quantity){
        Connection conn = null;
        ResultSet rs = null;
        
        String sqlCheckIfExists = "SELECT quantity FROM Cart WHERE accID = ? AND bookID = ?";
        String sqlUpdateQuantity = "UPDATE Cart SET quantity = quantity + ? WHERE accID = ? AND bookID = ?";
        String sqlInsertNewItem = "INSERT INTO Cart (accID, bookID, quantity) VALUES (?, ?, ?)";
        
        
        try {
            conn = DbUtils.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(sqlCheckIfExists);
            checkStmt.setString(1, accID);
            checkStmt.setInt(2, bookID);
            rs = checkStmt.executeQuery();
            
            if(rs.next()){
                int existingQuantity = rs.getInt("quantity");
                PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateQuantity);
                updateStmt.setInt(1, quantity);
                updateStmt.setString(2, accID);
                updateStmt.setInt(3, bookID);
                updateStmt.executeUpdate();
            }else{
                PreparedStatement insertStmt = conn.prepareStatement(sqlInsertNewItem);
                insertStmt.setString(1, accID);
                insertStmt.setInt(2, bookID);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("Error in  addItemToCart(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean updateCartQuantity(int cartID, int quantity){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_CART_QUANTITY);
            ps.setInt(1, quantity);
            ps.setInt(2, cartID);
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in  updateCartQuantity(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    public boolean reduceStock(int bookID, int quantitySold){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(REDUCE_STOCK);
            ps.setInt(1, quantitySold);
            ps.setInt(2, bookID);
            ps.setInt(3, quantitySold);
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in  reduceStock(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
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
