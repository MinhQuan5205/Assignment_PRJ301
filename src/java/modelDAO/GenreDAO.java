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
import modelDTO.GenreDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class GenreDAO {
    //tạo câu truy vấn 
    private static final String GET_ALL_GENRE = "SELECT * FROM Genre";
    private static final String CREATE_GENRE = "INSERT INTO Genre (genreName) VALUES (?)";
    private static final String GET_GENRE_BY_GENREID ="SELECT * FROM Genre WHERE genreID = ?";
    
    public GenreDAO() {
    }
    
    public GenreDTO getGenreById(int genreID){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_GENRE_BY_GENREID);
            ps.setInt(1, genreID);
            rs = ps.executeQuery();
            
            while(rs.next()){
                return new GenreDTO(
                        rs.getInt("genreID"),
                        rs.getString("genreName")
                );
            }
        } catch (Exception e) {
            System.err.println("Error in getGenreById(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public List<GenreDTO> getAllGenre(){
        List<GenreDTO> genreList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ALL_GENRE);
            rs = ps.executeQuery();
            
            while(rs.next()){
                genreList.add(new GenreDTO(
                            rs.getInt("genreID"),
                            rs.getString("genreName")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in getAllGenre(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return genreList;
    }
    
    public boolean createGenre(GenreDTO genre){
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CREATE_GENRE);
            ps.setString(1, genre.getGenreName());
            
            int rowsAffect = ps.executeUpdate();
            success = (rowsAffect > 0);
        } catch (Exception e) {
            System.err.println("Error in createGenre(): " + e.getMessage());
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
