package za.ac.cput.serverside.dao;

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import za.ac.cput.domain.Student;
import za.ac.cput.domain.Course;
import za.ac.cput.serverside.connection.DBConnection;

/**
 *
 * @author 220094489
 */
public class EnrolmentDAO {

    //Declare JDBC components
    private Connection con;
    private PreparedStatement pstmt;

    public EnrolmentDAO() {
        try {
            this.con = DBConnection.derbyConnection();
        } catch (Exception exception) {
            System.out.println("Connection error: " + exception.getMessage());
        }
    }

    // Enrol students
    public void enrolStudent(String studentNum, String courseCode) {
        String sql = "INSERT INTO Enrolment (studentNum, courseCode) VALUES (?, ?)";
        try {
            //sets the placeholders
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, studentNum);
            pstmt.setString(2, courseCode);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\nStudent " + studentNum + " successfully enrolled in " + courseCode + ".");
            } else {
                System.out.println("\nEnrolment failed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e.getMessage());
            }
        } //end finally
    }

    //  all students enrolled in a particular course
    public ArrayList<Student> getStudentsInCourse(String courseCode) {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT s.studentNum, s.firstName, s.lastName "
                + "FROM Student s "
                + "JOIN Enrolment e ON s.studentNum = e.studentNum "
                + "WHERE e.courseCode = ?";
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // [UPDATED] Created Student objects using constructor
                Student student = new Student(
                        rs.getString("studentNum"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                students.add(student);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving students for course: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e.getMessage());
            }
        }
        return students;
    }

    // [UPDATED] Retrieve all courses a particular student is enrolled in
    public ArrayList<Course> getCoursesForStudent(String studentNum) {
        ArrayList<Course> courses = new ArrayList<>();

        try {
            String sql = """
            SELECT c.courseCode, c.courseDescription
            FROM Enrolment e
            JOIN Course c ON e.courseCode = c.courseCode
            WHERE e.studentNum = ?
                                """;

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, studentNum);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Created Course objects using constructor
                Course course = new Course(
                        rs.getString("courseCode"),
                        rs.getString("courseDescription")
                );
                courses.add(course);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving courses for student: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e.getMessage());
            }
        }
        return courses;
    }
}
