package za.ac.cput.serverside.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.domain.Course;
import za.ac.cput.serverside.dao.CourseDAO;
import za.ac.cput.serverside.dao.EnrolmentDAO;

public class StudentGUI extends JFrame implements ActionListener {

    private JLabel studentNumlbl, courseLbl;
    private JTextField studentNumtxt, coursetxt;
    private JButton btnEnrol, btnSearch, btnLogout, btnView, btnViewMyCourses; 
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel pnlNorth, pnlSouth;
    private CourseDAO dao;
    private EnrolmentDAO enrolmentDao;
   

    public StudentGUI() {
        setTitle("Student Portal");
        //initialize the dao to access the methods
        dao = new CourseDAO();
        enrolmentDao = new EnrolmentDAO();

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //Student number 
        //set the layout to 2 rows and 1 column. add gaps of 5px
        pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));
        studentNumlbl = new JLabel("Student Number:");
        studentNumtxt = new JTextField(12);
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        firstRow.add(studentNumlbl);
        firstRow.add(studentNumtxt);

        //search and view course 
        courseLbl = new JLabel("Search Course:");
        coursetxt = new JTextField(10);
        btnSearch = new JButton("Search");
        btnView = new JButton("Display Courses");

        JPanel secRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        secRow.add(courseLbl);
        secRow.add(coursetxt);
        secRow.add(btnSearch);
        secRow.add(btnView);

        // Add the first and second row to the panel
        pnlNorth.add(firstRow);
        pnlNorth.add(secRow);
        //add the panel to the top of the window
        add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {"Course Code", "Course Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        //allow selecting multiple courses
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(scrollPane, BorderLayout.CENTER);

        pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnEnrol = new JButton("Enrol");
        btnLogout = new JButton("Logout");
        btnViewMyCourses = new JButton("View My Courses");

        pnlSouth.add(btnEnrol);
        pnlSouth.add(btnLogout);
        pnlSouth.add(btnViewMyCourses);

        add(pnlSouth, BorderLayout.SOUTH);

        //Action listeners
        btnSearch.addActionListener(this);
        btnView.addActionListener(this);
        btnEnrol.addActionListener(this);
        btnLogout.addActionListener(this);
        btnViewMyCourses.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnView) {
            tableModel.setRowCount(0);
            ArrayList<Course> courseList = dao.displayCourse(); //calling a dao message to retrieve all courses
            for (Course course : courseList) {
                //Add the 2 rows to the table
                tableModel.addRow(new Object[]{
                    course.getCourseCode(),
                    course.getCourseDescription()
                });
            }
            //search button
        } else if (e.getSource() == btnSearch) {
            String courseCode = coursetxt.getText();
            if (courseCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a course code to search.");
                return;
            }

            tableModel.setRowCount(0);
            ArrayList<Course> courseList = dao.displayCourse();//call the dao method
            boolean found = false;
            for (Course course : courseList) {
                if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                    tableModel.addRow(new Object[]{course.getCourseCode(), course.getCourseDescription()}); //add data to the table
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Course not found.");
            }
            //enrol button
        } else if (e.getSource() == btnEnrol) {
            int[] selectedRows = table.getSelectedRows(); // Get all selected courses

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Select a course to enrol.");
                return;
            }

            String studentNum = studentNumtxt.getText();
            if (studentNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your student number.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to enrol in the selected course(s)?",
                    "Confirm Enrolment",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                //hold all the enrolled courses
                ArrayList<String> enrolledCourses = new ArrayList<>();

                // Loop through all selected courses and enrol one by one
                for (int row : selectedRows) {
                    int modelRow = table.convertRowIndexToModel(row);
                    String courseCode = tableModel.getValueAt(modelRow, 0).toString(); //toString() since it might be an object
                    String courseDescription = tableModel.getValueAt(modelRow, 1).toString();

                    //call dao method
                    enrolmentDao.enrolStudent(studentNum, courseCode);
                    enrolledCourses.add(courseCode + " - " + courseDescription); //adds the enrolledcourses to the list
                }

                JOptionPane.showMessageDialog(this,
                        "Student " + studentNum + " successfully enrolled in:\n"
                        + String.join("\n", enrolledCourses));
            }

        } else if (e.getSource() == btnViewMyCourses) { 
            String studentNum = studentNumtxt.getText();
            if (studentNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your student number.");
                return;
            }

            ArrayList<Course> courses = enrolmentDao.getCoursesForStudent(studentNum);
            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No courses found for this student.");
                return;
            }
            StringBuilder sb = new StringBuilder("Courses enrolled for student " + studentNum + ":\n\n");
            for (Course c : courses) {
                sb.append("- ").append(c.getCourseCode())
                        .append(": ").append(c.getCourseDescription())
                        .append("\n");
            }

            tableModel.setRowCount(0); // clear table
            for (Course c : courses) {
                tableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseDescription()});
            } 

        } else if (e.getSource() == btnLogout) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            tableModel.setRowCount(0);   // clear the table
            studentNumtxt.setText("");

            if (confirmation == JOptionPane.YES_OPTION) {
                setVisible(false); // hides the window
                JOptionPane.showMessageDialog(null, "You have logged out. Goodbye");
                //returning to the login page
                new LoginGUI();
            }
        }
    }

    public static void main(String[] args) {
        new StudentGUI();
    }
}
