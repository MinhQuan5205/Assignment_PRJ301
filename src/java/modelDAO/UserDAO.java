/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelDTO.UserDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class UserDAO {
    
    //Câu truy vấn 
    private static final String GET_ALL_USER = "SELECT * FROM Account";
    private static final String GET_USER_BY_ACCID = "SELECT * FROM Account WHERE accID = ?";
    private static final String INSERT_USER = "INSERT INTO Account(accID,email,password,name,role,phone,isActive) VALUES (?,?,?,?,1,?,1)";
    private static final String CHECK_USER_EXIST = "SELECT COUNT(*) FROM Account WHERE accID != ? AND (email = ? OR phone = ?)";
    private static final String CHECK_USERID_EXIST = "SELECT COUNT(*) FROM Account WHERE accID = ?";
    private static final String CHECK_EMAIL_EXIST = "SELECT COUNT(*) FROM Account WHERE email = ?";
    private static final String CHECK_PHONE_EXIST = "SELECT COUNT(*) FROM Account WHERE phone = ?";
    private static final String CHANGE_PASSWORD = "UPDATE Account SET password = ? WHERE accID = ?";
    private static final String UPDATE_USER = "UPDATE Account SET name = ?, email = ?, phone = ? WHERE accID = ?";
    private static final String CHANGE_ACTIVE_USER = "UPDATE Account SET isActive = 0 WHERE accID = ?";
    
    public UserDAO() {
    }
    
    public List<UserDTO> getAllUser(){
        List<UserDTO> userList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ALL_USER);
            rs = ps.executeQuery();
            while(rs.next()){
                UserDTO user = new UserDTO(
                        rs.getString("accID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getInt("role"),
                        rs.getString("phone"),
                        rs.getBoolean("isActive"));
                userList.add(user);
            }
        } catch (Exception e) {
            System.err.println("Error in getAllUser(): " + e.getMessage());
            e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return userList;
    }
    
    public UserDTO getUserById(String accId){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_USER_BY_ACCID);
            ps.setString(1, accId);
            rs = ps.executeQuery();
            while(rs.next()){
                return new UserDTO(
                        rs.getString("accID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getInt("role"),
                        rs.getString("phone"),
                        rs.getBoolean("isActive"));
            }
        } catch (Exception e) {
            System.err.println("Error in getUser(): " + e.getMessage());
            e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public boolean addUser(UserDTO user){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(INSERT_USER);
            ps.setString(1, user.getAccID());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getName());
            ps.setString(5, user.getPhone());
            
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in addUser(): " + e.getMessage());
            e.printStackTrace();
        }finally{
            closeResources(conn, ps, null);
        }
        return success;
    }
    
    public boolean isUserExist(String accID, String email, String phone){//kiểm tra xem ngoại trừ tài khoản vừa mới cập nhật nó có số điện thoại hay email mới có trừng với tài khoản khác hay không
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_USER_EXIST);
            ps.setString(1, accID);
            ps.setString(2, email);
            ps.setString(3, phone);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
           System.err.println("Error in isUserExist(): " + e.getMessage());
           e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    public boolean isUsernameExist(String userID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_USERID_EXIST);
            ps.setString(1, userID);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
           System.err.println("Error in isUsernameExist(): " + e.getMessage());
           e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    public boolean isEmailExist(String email){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_EMAIL_EXIST);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
           System.err.println("Error in isEmailExist: " + e.getMessage());
           e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    public boolean isPhoneExist(String phone){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHECK_PHONE_EXIST);
            ps.setString(1, phone);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
           System.err.println("Error in isPhoneExist: " + e.getMessage());
           e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    public boolean changePassword(String accID, String newPassword){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHANGE_PASSWORD);
            ps.setString(1, newPassword);
            ps.setString(2, accID);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
           System.err.println("Error in changePassword(): " + e.getMessage());
           e.printStackTrace();
        } finally{
            closeResources(conn, ps, null);
        }
        return false;
    }
    
    public boolean updateUser(UserDTO updateUser){
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_USER);
            ps.setString(1, updateUser.getName());
            ps.setString(2, updateUser.getEmail());
            ps.setString(3, updateUser.getPhone());
            ps.setString(4, updateUser.getAccID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
           System.err.println("Error in updateUser(): " + e.getMessage());
           e.printStackTrace();
        } finally{
            closeResources(conn, ps, null);
        }
        return false;
    }
    
    public List<UserDTO> getListByPage(List<UserDTO> userList, int begin, int end){
        ArrayList<UserDTO> list = new ArrayList<>();
        for(int i = begin; i < end ; i++){
            list.add(userList.get(i));
        }
        return list;
    }
    
    public List<UserDTO> getUsersByFilter(String searchName, Integer role){
        List<UserDTO> userList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Account WHERE 1=1";
        
        if(searchName != null && !searchName.isEmpty()){
            sql += " AND (accID LIKE ? OR name LIKE ?)";
        }
        
        if(role != null){
            sql += " AND role = ?";
        }
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(sql);
            
            int index = 1;
            if(searchName != null && !searchName.isEmpty()){
                ps.setString(index++, "%" + searchName + "%");
                ps.setString(index++, "%" + searchName + "%");
            }
            if(role != null){
                ps.setInt(index++, role);
            }
            
            rs = ps.executeQuery();
            while(rs.next()){
                UserDTO user = new UserDTO(
                                    rs.getString("accID"),
                                    rs.getString("email"),
                                    rs.getString("password"),
                                    rs.getString("name"),
                                    rs.getInt("role"),
                                    rs.getString("phone"),
                                    rs.getBoolean("isActive"));
                userList.add(user);
            }
        } catch (Exception e) {
            System.err.println("Error in getUsersByFilter(): " + e.getMessage());
            e.printStackTrace();
        }finally{
            closeResources(conn, ps, rs);
        }
        return userList;
    }
    
    public boolean changeAvtiveUser(String accID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CHANGE_ACTIVE_USER);
            ps.setString(1, accID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("Error in changeAvtiveUser: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
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
