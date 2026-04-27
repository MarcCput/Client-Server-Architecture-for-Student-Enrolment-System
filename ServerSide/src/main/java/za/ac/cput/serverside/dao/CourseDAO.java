package za.ac.cput.serverside.dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import za.ac.cput.domain.Course;
import za.ac.cput.serverside.connection.DBConnection;


public class CourseDAO {

    //Declare JDBC components
    private Connection con;
    private PreparedStatement pstmt;

    public CourseDAO() {
        try {
            //opens database connection
            this.con = DBConnection.derbyConnection();
        } catch (Exception exception) {

        }
    }
        //Add a course to the database
    public void addCourse(Course course) {
        int ok; 
        //insert query with the use of placeholders
        String insertSql = "INSERT INTO course VALUES(?, ?)";

        try {
            //sets the placeholders
            pstmt = this.con.prepareStatement(insertSql);
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseDescription());
                  
            //Execute the pstmt
            ok = pstmt.executeUpdate();
            System.out.println("Course has been successfully added.");

        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }
    }

    //Update course data
    public int updateCourse(Course course) {
        //Number of rows to be updated
        int ok = 0;
        String updateSql = "UPDATE course SET courseDescription = ? WHERE courseCode = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = this.con.prepareStatement(updateSql);
            pstmt.setString(1, course.getCourseDescription());
            pstmt.setString(2, course.getCourseCode());

            ok = pstmt.executeUpdate();
            if (ok > 0) {
                System.out.println("Course " + course.getCourseCode() + " has been updated successfully.");
            } else {
                System.out.println("Course " + course.getCourseCode() + " is not found.");
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close(); 
                }
            } catch (SQLException e) {
                System.out.println("Error:  " + e.getMessage());
            }
        }
        return ok;
    }
    
    //Method to delete course
    public int delCourse(String courseCode) {
        //Number of rows to be updated
        int ok = 0;
        String deleteSql = "DELETE FROM course WHERE courseCode = ?";
        System.out.println("sql string: " + deleteSql);

        try {

            pstmt = this.con.prepareStatement(deleteSql);
            pstmt.setString(1, courseCode);
            ok = pstmt.executeUpdate();
            if (ok > 0) {
                System.out.println("Course: " + courseCode + " is deleted successfully.");
            } else {
                System.out.println("Course: " + courseCode + " was not found.");
            }
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Error: " + sqlException.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return ok;
    }  

    //Retrieve all the courses
    public ArrayList<Course> displayCourse() {
        //create a list to store the data
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            String displayAllSql = "SELECT * FROM course";
            pstmt = this.con.prepareStatement(displayAllSql);
            ResultSet rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    //Each row prints data
                    System.out.println("Courses Available:"
                            + "\nCourse Code: " + rs.getString(1) + "\nCourse Description: " + rs.getString(2));
                   //create course object with the data and adds it to the list
                    courseList.add(new Course(rs.getString(1), rs.getString(2)));

                }// end while
                rs.close();
            } //end if
        }//end try
        catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    //close prepared statement
                    pstmt.close();
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage());
            }
        }//end finally
        return courseList;
    }

}
