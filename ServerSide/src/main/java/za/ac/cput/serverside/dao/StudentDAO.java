package za.ac.cput.serverside.dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import za.ac.cput.domain.Student;
import za.ac.cput.serverside.connection.DBConnection;

public class StudentDAO {

    //Declare JDBC components
    private Connection con;
    private PreparedStatement pstmt;

    public StudentDAO() {
        try {
            this.con = DBConnection.derbyConnection();
        } catch (Exception exception) {

        }
    }

    //Add students
    //takes an argument that is a student object
    public void addStudent(Student student) {
        int ok;
        String insertSql = "INSERT INTO Student(studentNum, firstName, lastName) VALUES(?, ?, ?)";

        try {
            pstmt = this.con.prepareStatement(insertSql);
            pstmt.setString(1, student.getStudentNum());     //sets the placeholders
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());

            ok = pstmt.executeUpdate();
            System.out.println("Student has been successfully added.");

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
        }//end finally
    }//end addStudent method  

    //Update student data
    public int updateStudent(Student student) {
        //Number of rows to be updated
        int ok = 0;
        String updateSql = "UPDATE student SET firstName = ?, lastName = ? WHERE studentNum = ?";

        try {
            pstmt = this.con.prepareStatement(updateSql);
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getStudentNum());

            ok = pstmt.executeUpdate();
            if (ok > 0) {
                System.out.println("Student " + student.getStudentNum() + " has been updated successfully.");
            } else {
                System.out.println("Student " + student.getStudentNum() + " is not found.");
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

    //Delete student
    public int delStudent(String studentNum) {
        //Number of rows to be deleted
        int ok = 0;
        String deleteSql = "DELETE FROM student WHERE studentNum = ?";
        System.out.println("sql string: " + deleteSql);

        try {

            pstmt = this.con.prepareStatement(deleteSql);
            pstmt.setString(1, studentNum);
            ok = pstmt.executeUpdate();
            if (ok > 0) {
                System.out.println("Student: " + studentNum + "is deleted successfully.");
            } else {
                System.out.println("Student: " + studentNum + "was not found.");
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
        }//end finally
        return ok;
    }//end delStudent method   

    //Retrieve all the students
    public ArrayList<Student> displayStudents() {
        //create a list to store the data
        ArrayList<Student> studentList = new ArrayList<>();
        try {
            String viewAllSql = "SELECT * FROM Student";
            pstmt = this.con.prepareStatement(viewAllSql);
            ResultSet rs = pstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    System.out.println("Student table Records:"
                            + "\nStudent Number: " + rs.getString(1)
                            + "\nFirst Name: " + rs.getString(2)
                            + "\nLast Name: " + rs.getString(3));
                    //create student object with the data and adds it to the list
                    studentList.add(new Student(rs.getString(1), rs.getString(2), rs.getString(3)));

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
        return studentList;
    }
}
