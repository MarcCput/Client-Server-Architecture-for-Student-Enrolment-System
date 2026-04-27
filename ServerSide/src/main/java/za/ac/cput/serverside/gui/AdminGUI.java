package za.ac.cput.serverside.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.domain.Course;
import za.ac.cput.domain.Student;
import za.ac.cput.serverside.dao.CourseDAO;
import za.ac.cput.serverside.dao.StudentDAO;
import za.ac.cput.serverside.dao.EnrolmentDAO;

public class AdminGUI extends JFrame implements ActionListener {

    private JLabel courseCode, courseDesc, studentNum, studentName, studentSurname;
    private JTextField courseCodetxt, courseDesctxt, studentNumtxt, studentNametxt, studentSurnametxt;
    private JButton addCourseBtn, viewCoursesBtn, delCourseBtn, updateCourseBtn, addStudentBtn, viewStudentsBtn, updateStudentBtn,
            viewStudentsInCourseBtn, viewCoursesForStudentBtn,
            logoutBtn, delStudentBtn;
    private JTable table1, table2;
    private JScrollPane studentPane, coursePane;
    private DefaultTableModel courseTable, studentTable;
    private StudentDAO studMethod; //declare dao objects
    private CourseDAO courseMethod;
    private EnrolmentDAO enrollMethod;  


    public AdminGUI() {
        super("Admin Portal");
        //initialize DAO objects to communicate with database
        studMethod = new StudentDAO();
        courseMethod = new CourseDAO();
        enrollMethod = new EnrolmentDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

         

        // These are the Course panel
        JPanel coursePanel = new JPanel(new GridLayout(3, 3, 10, 10));
        courseCode = new JLabel("Course Code: ");
        courseCodetxt = new JTextField(10);

        courseDesc = new JLabel("Course Description: ");
        courseDesctxt = new JTextField(20);

        addCourseBtn = new JButton("Add Course");
        updateCourseBtn = new JButton("Update Course");
        viewCoursesBtn = new JButton("Display Courses");
        delCourseBtn = new JButton("Delete Course");
        viewStudentsInCourseBtn = new JButton("Students in Course"); 
        viewCoursesForStudentBtn = new JButton("Courses for Student");

        coursePanel.add(courseCode);
        coursePanel.add(courseCodetxt);
        coursePanel.add(courseDesc);
        coursePanel.add(courseDesctxt);
        coursePanel.add(addCourseBtn);
        coursePanel.add(updateCourseBtn);
        coursePanel.add(delCourseBtn);
        coursePanel.add(viewCoursesBtn);
        coursePanel.add(viewStudentsInCourseBtn); 

        // Student panel
        JPanel studentPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        studentNum = new JLabel("Student Number:");
        studentNumtxt = new JTextField(10);
        studentName = new JLabel("First Name:");
        studentNametxt = new JTextField(15);
        studentSurname = new JLabel("Last Name:");
        studentSurnametxt = new JTextField(15);

        addStudentBtn = new JButton("Add Student");
        updateStudentBtn = new JButton("Update Student");
        delStudentBtn = new JButton("Delete Student");
        viewStudentsBtn = new JButton("Display Student");

        studentPanel.add(studentNum);
        studentPanel.add(studentNumtxt);
        studentPanel.add(studentName);
        studentPanel.add(studentNametxt);
        studentPanel.add(studentSurname);
        studentPanel.add(studentSurnametxt);
        studentPanel.add(addStudentBtn);
        studentPanel.add(updateStudentBtn);
        studentPanel.add(delStudentBtn);
        studentPanel.add(viewStudentsBtn);
        studentPanel.add(viewCoursesForStudentBtn);

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 40, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(coursePanel);
        topPanel.add(studentPanel);
        add(topPanel, BorderLayout.NORTH);

        //Table to display course details
        String[] course = {"Course Code", "Course Description"};
        courseTable = new DefaultTableModel(course, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1 = new JTable(courseTable);
        coursePane = new JScrollPane(table1);

        //Table to display course details
        String[] student = {"Student Number", "First Name", "Last Name"};
        studentTable = new DefaultTableModel(student, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table2 = new JTable(studentTable);
        studentPane = new JScrollPane(table2);

        // split the tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, coursePane, studentPane);
        splitPane.setDividerLocation(450); //split the table into 2
        add(splitPane, BorderLayout.CENTER);

        // Logout button 
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        logoutBtn = new JButton("Logout");
        pnlSouth.add(logoutBtn);
        add(pnlSouth, BorderLayout.SOUTH);

        // register buttons to the actionPerformed() method
        addCourseBtn.addActionListener(this);
        updateCourseBtn.addActionListener(this);
        delCourseBtn.addActionListener(this);
        viewCoursesBtn.addActionListener(this);
        viewStudentsInCourseBtn.addActionListener(this); 

        addStudentBtn.addActionListener(this);
        updateStudentBtn.addActionListener(this);
        delStudentBtn.addActionListener(this);
        viewStudentsBtn.addActionListener(this);
        logoutBtn.addActionListener(this);
        viewCoursesForStudentBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Add course to the database
        if (e.getSource() == addCourseBtn) {
            String code = courseCodetxt.getText();
            String description = courseDesctxt.getText();

            if (!code.equals("") && !description.equals("")) {
                Course course = new Course(code, description); //create a course object
                courseMethod.addCourse(course); //call the dao method
                JOptionPane.showMessageDialog(this, "Course saved successfully");
                courseCodetxt.setText(""); //clear textfield
                courseDesctxt.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Fill both fields.");
            }
        }
        if (e.getSource() == updateCourseBtn) {
            // course code to update
            String code = JOptionPane.showInputDialog(this, "Enter the course code to update:");
            if (code == null || code.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No course code entered.");
                return;
            }

            // new course description
            String description = JOptionPane.showInputDialog(this, "Enter the new course description:");
            if (description == null || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No description entered.");
                return;
            }

            // Call DAO to update
            int result = courseMethod.updateCourse(new Course(code, description));
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Course updated successfully");
                // Refresh table after update
                viewCoursesBtn.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Course not found or update failed");
            }

            //Display all the course
        } else if (e.getSource() == viewCoursesBtn) {
            courseTable.setRowCount(0);
            ArrayList<Course> course = courseMethod.displayCourse(); //Call  the method in DAO
            for (Course courseList : course) {  //iterate the data 1 by 1
                courseTable.addRow(new Object[]{courseList.getCourseCode(), courseList.getCourseDescription()}); //add the data on the table
            }
            //Delete course  by course code
        } else if (e.getSource() == delCourseBtn) {
            String cod = JOptionPane.showInputDialog(this, "Enter Course code to delete: ", "Delete Course", JOptionPane.QUESTION_MESSAGE);

            if (cod != null && !cod.isEmpty()) {
                int verify = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete course with code: " + cod + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (verify == JOptionPane.YES_OPTION) {
                    int result = courseMethod.delCourse(cod);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                    }
                    courseTable.setRowCount(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Course not found!");
                }
            }

            // Add Students to the database
        } else if (e.getSource() == addStudentBtn) {
            String studNum = studentNumtxt.getText();
            String studName = studentNametxt.getText();
            String studSurname = studentSurnametxt.getText();

            if (!studNum.equals("") && !studName.equals("") && !studSurname.equals("")) {
                Student stdnt = new Student(studNum, studName, studSurname);
                studMethod.addStudent(stdnt);
                JOptionPane.showMessageDialog(this, "Student saved successfully");

                //clear tetxfields
                studentNumtxt.setText("");
                studentNametxt.setText("");
                studentSurnametxt.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Fill both fields.");
            }
        }
        //update student information
        if (e.getSource() == updateStudentBtn) {
            // Student number to update
            String studNumber = JOptionPane.showInputDialog(this, "Enter the student number to update:");
            if (studNumber == null || studNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No student number entered.");
                return;
            }

            // new first name
            String firstName = JOptionPane.showInputDialog(this, "Enter the new student first name:");
            if (firstName == null || firstName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No name entered.");
                return;
            }
            // new last name
            String lastName = JOptionPane.showInputDialog(this, "Enter the new student last name:");
            if (lastName == null || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No name entered.");
                return;
            }

            // Call DAO to update
            int result = studMethod.updateStudent(new Student(studNumber, firstName, lastName));
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Student updated successfully");
                // Refresh table after the update method is executed
                viewStudentsBtn.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Student not found or update failed");
            }

            //Delete Student by student Number
        } else if (e.getSource() == delStudentBtn) {
            String stud = JOptionPane.showInputDialog(this, "Enter Student number to delete: ", "Delete Student", JOptionPane.WARNING_MESSAGE);

            if (stud != null && !stud.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete student with Student number: " + stud + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    int result = studMethod.delStudent(stud); //dao method
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    }
                    //clear the table
                    studentTable.setRowCount(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!");
                }
            }

        } // Display all students enrolled in a course
        else if (e.getSource() == viewStudentsInCourseBtn) {
            String code = JOptionPane.showInputDialog(this, "Enter course code:");
            if (code != null && !code.isEmpty()) {
                ArrayList<Student> students = enrollMethod.getStudentsInCourse(code);
                studentTable.setRowCount(0); // clear table
                for (Student s : students) {
                    studentTable.addRow(new Object[]{s.getStudentNum(), s.getFirstName(), s.getLastName()});
                }
            }
        } // Display all courses a student is enrolled in
        else if (e.getSource() == viewCoursesForStudentBtn) {
            String studNum = JOptionPane.showInputDialog(this, "Enter student number:");
            if (studNum != null && !studNum.isEmpty()) {
                ArrayList<Course> courses = enrollMethod.getCoursesForStudent(studNum);
                courseTable.setRowCount(0); // clear table
                for (Course c : courses) {
                    courseTable.addRow(new Object[]{c.getCourseCode(), c.getCourseDescription()});
                }
            }
        } else if (e.getSource() == viewStudentsBtn) {
            studentTable.setRowCount(0);
            ArrayList<Student> student = studMethod.displayStudents(); //Call the method in Student DAO and get data as arrayList
            for (Student studentList : student) { //iterate the data 1 by 1
                studentTable.addRow(new Object[]{studentList.getStudentNum(), studentList.getFirstName(), studentList.getLastName()});//adds data to the table
            }
        } else if (e.getSource() == logoutBtn) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "You have logged out.");
                setVisible(false); // hides the window                             
                //returning to the login page
                new LoginGUI();
            }
        }
    }

    public static void main(String[] args) {
        AdminGUI adminGUI = new AdminGUI();
    }
}
