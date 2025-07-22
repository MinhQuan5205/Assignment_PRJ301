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
import utils.DbUtils;


/**
 *
 * @author ACER
 */
public class BookDAO {
    //câu truy vấn
    private static final String GET_ALL_BOOKS = "SELECT * FROM Book WHERE status NOT IN (0)";
    private static final String GET_BOOK_BY_ID = "SELECT * FROM Book WHERE bookID = ? AND status NOT IN (0)";
    private static final String CREATE_BOOK = "INSERT INTO Book(title,price,description,genreID,authorID,imageURL,discount,stock,status) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_BOOK = "UPDATE Book SET title=?,price=?,description=?,genreID=?,authorID=?,imageURL=?,discount=?,stock=? WHERE bookID = ? AND status NOT IN (0)";
    private static final String CHECK_BOOK_ISEXIST_BY_TITLE = "SELECT COUNT(*) FROM Book WHERE title = ? AND status NOT IN (0)";
    private static final String GET_RATING_BY_BOOKID = "SELECT rating FROM BookRating WHERE bookID = ?";
    private static final String GET_STOCK_BY_BOOKID = "SELECT stock FROM Book WHERE bookID = ? AND status NOT IN (0)";
    private static final String UPDATE_STATUS_BOOK_BYID = "UPDATE Book SET status = 0 WHERE bookID = ?";
    public BookDAO() {
    }
    
    public List<BookDTO> getAllBooks(){
        List<BookDTO> bookList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ALL_BOOKS);
            rs = ps.executeQuery();
            
            while(rs.next()){
                BookDTO book = new BookDTO(
                        rs.getInt("bookID"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("genreID"),
                        rs.getInt("authorID"),
                        rs.getString("imageURL"),
                        rs.getDouble("discount"),
                        rs.getInt("stock")
                        );
                bookList.add(book);
            }
        } catch (Exception e) {
            System.err.println("Error in getAllBooks(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return bookList;
    }
    
    public BookDTO getBookById(int bookId){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_BOOK_BY_ID);
            ps.setInt(1, bookId);
            rs = ps.executeQuery();
            
            while(rs.next()){
                return new BookDTO(
                        rs.getInt("bookID"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("genreID"),
                        rs.getInt("authorID"),
                        rs.getString("imageURL"),
                        rs.getDouble("discount"),
                        rs.getInt("stock")
                        );
            }
        } catch (Exception e) {
            System.err.println("Error in getBookById(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public int getStockByBookID(int bookID) {
        int stock = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_STOCK_BY_BOOKID);
            ps.setInt(1, bookID);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (Exception e) {
            System.err.println("Error in getStockByBookID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return stock;
    }
    
    public List<BookDTO> getListByPage(List<BookDTO> bookList, int start, int end) {
        ArrayList<BookDTO> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            list.add(bookList.get(i));
        }
        return list;
    }
    
    public boolean addBook(BookDTO book){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CREATE_BOOK);
            
            ps.setString(1, book.getTitle());
            ps.setDouble(2, book.getPrice());
            ps.setString(3, book.getDescription());
            ps.setInt(4, book.getGenreID());
            ps.setInt(5, book.getAuthorID());
            ps.setString(6, book.getImageURL());
            ps.setDouble(7, book.getDiscount());
            ps.setInt(8, book.getStock());
            ps.setInt(9, 1);
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in addBook(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    public boolean updateBook(BookDTO book){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_BOOK);
            
            ps.setString(1, book.getTitle());
            ps.setDouble(2, book.getPrice());
            ps.setString(3, book.getDescription());
            ps.setInt(4, book.getGenreID());
            ps.setInt(5, book.getAuthorID());
            ps.setString(6, book.getImageURL());
            ps.setDouble(7, book.getDiscount());
            ps.setInt(8, book.getStock());
            ps.setInt(9, book.getBookID());
            
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in updateBook(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    public boolean updateStatusBookById(int bookID){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_STATUS_BOOK_BYID);
            ps.setInt(1, bookID);
            
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in updateStatusBookById(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    
    public boolean isBookExist(String title){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_BOOK_ISEXIST_BY_TITLE);
            ps.setString(1, title);
            try(ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            } 
        } catch (Exception e) {
            System.err.println("Error in isBookExist(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return false;
    }
    
    public List<BookDTO> getSimilarBooks(int genreID, int authorID, int currentBookID, int limit){
        List<BookDTO> booksList = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Book WHERE genreID = ? AND authorID = ? AND bookID != ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            ps.setInt(2, genreID);
            ps.setInt(3, authorID);
            ps.setInt(4, currentBookID);
            
            rs = ps.executeQuery();
            while(rs.next()){
                booksList.add(new BookDTO(
                        rs.getInt("bookID"),
                        rs.getString("title"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("genreID"),
                        rs.getInt("authorID"),
                        rs.getString("imageURL"),
                        rs.getDouble("discount"),
                        rs.getInt("stock")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in isBookExist(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return booksList;
    }
    
    public double getRatingByBookID(int bookID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_RATING_BY_BOOKID);
            ps.setInt(1, bookID);
            
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getDouble("rating");
            }
        } catch (Exception e) {
            System.err.println("Error in getRatingByBookID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return 0.0;
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
    
    
    public static void main(String[] args) {
        BookDAO bdao = new BookDAO();
        List<BookDTO> bookList = bdao.getAllBooks();
        
        for (BookDTO bookDTO : bookList) {
            System.out.println(bookDTO.getTitle());
        }
        
        if(!bookList.isEmpty()){
            System.out.println(1);
        }else{
            System.out.println(0);
        }
    }
}
