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
import modelDTO.ReviewDTO;
import utils.DbUtils;

/**
 *
 * @author ACER
 */
public class ReviewDAO {

    //câu truy vấn
    private static final String GET_REVIEWS_BY_BOOKID = "SELECT * FROM Review WHERE bookID = ?";
    private static final String GET_REVIEW_BY_REVIEWID = "SELECT * FROM Review WHERE reviewID = ?";
    private static final String CREATE_REVIEW = "INSERT INTO Review (bookID, accID, comment, rating) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_REVIEW = "UPDATE Review SET bookID=?, accID=?, comment=?, rating=? WHERE reviewID=?";
    private static final String DELETE_REVIEW = "DELETE FROM Review WHERE reviewID = ?";

    public ReviewDAO() {
    }

    public ReviewDTO getReviewByID(int reviewID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_REVIEW_BY_REVIEWID);
            ps.setInt(1, reviewID);
            rs = ps.executeQuery();

            while (rs.next()) {
                return new ReviewDTO(
                        rs.getInt("reviewID"),
                        rs.getInt("bookID"),
                        rs.getString("accID"),
                        rs.getString("comment"),
                        rs.getTimestamp("date"),
                        rs.getInt("rating")
                );
            }
        } catch (Exception e) {
            System.err.println("Error in  getReviewByID(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public List<ReviewDTO> getReviewsByBookID(int bookID) {
        List<ReviewDTO> reviewsList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(GET_REVIEWS_BY_BOOKID);
            ps.setInt(1, bookID);
            rs = ps.executeQuery();

            while (rs.next()) {
                reviewsList.add(new ReviewDTO(
                        rs.getInt("reviewID"),
                        rs.getInt("bookID"),
                        rs.getString("accID"),
                        rs.getString("comment"),
                        rs.getTimestamp("date"),
                        rs.getInt("rating")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error in getReviewsByBookID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return reviewsList;
    }

    public boolean addReview(ReviewDTO review) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(CREATE_REVIEW);
            ps.setInt(1, review.getBookID());
            ps.setString(2, review.getAccID());
            ps.setString(3, review.getComment());
            ps.setInt(4, review.getRating());
            int rowsAffected = ps.executeUpdate();
            success = (rowsAffected > 0);
        } catch (Exception e) {
            System.err.println("Error in addReview(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return success;
    }

    public boolean updateReview(ReviewDTO review) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(UPDATE_REVIEW);
            ps.setInt(1, review.getBookID());
            ps.setString(2, review.getAccID());
            ps.setString(3, review.getComment());
            ps.setInt(4, review.getRating());
            ps.setInt(5, review.getReviewID());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in addReview(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }
        return false;
    }

    public boolean deleteReview(ReviewDTO review) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DbUtils.getConnection();
            ps = conn.prepareStatement(DELETE_REVIEW);
            ps.setInt(1, review.getBookID());
            ps.setString(2, review.getAccID());
            ps.setString(3, review.getComment());
            ps.setInt(4, review.getRating());
            ps.setInt(5, review.getReviewID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error in deleteReview(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
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
