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
import modelDTO.AuthorDTO;
import modelDTO.GenreDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class AuthorDAO {

    //câu truy vấn 
    private static final String GET_ALL_AUTHOR = "SELECT * FROM Author";

    public AuthorDAO() {
    }

    public List<AuthorDTO> getAllAuthors() {
        List<AuthorDTO> authorList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_ALL_AUTHOR);
            rs = ps.executeQuery();

            while (rs.next()) {
                authorList.add(new AuthorDTO(
                        rs.getInt("authorID"),
                        rs.getString("authorName")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in getAllAuthors(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return authorList;
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
