package za.ac.cput.serverside;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import za.ac.cput.domain.Course;
import za.ac.cput.domain.Student;
import za.ac.cput.serverside.dao.CourseDAO;
import za.ac.cput.serverside.dao.EnrolmentDAO;
import za.ac.cput.serverside.dao.StudentDAO;

public class ServerSide {

    //Components
    private ServerSocket listener; // Server socket
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private StudentDAO studMethod;
    private CourseDAO courseMethod;
    private EnrolmentDAO enrolMethod;
    private Socket client;  // Client connection

    public ServerSide() {
        try {
            // Create server socket
            listener = new ServerSocket(7777, 1);
            //initialize dao objects
            studMethod = new StudentDAO();
            courseMethod = new CourseDAO();
            enrolMethod = new EnrolmentDAO();
            listenForClient();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    private void listenForClient() throws IOException {
        System.out.println("Server is listening!!");
        while (true) {
            client = listener.accept();
            System.out.println("Client connected!");

            Streams();
            requests();
        }
    }

    private void Streams() throws IOException {
        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();
        in = new ObjectInputStream(client.getInputStream());
    }

    //Handles client requests
    private void requests() {
        try {
            while (true) {
                //request type
                String request = (String) in.readObject();
                if (request.equals("Enrol_Student")) {
                    String[] enrolData = (String[]) in.readObject();
                    String studentNum = enrolData[0];
                    String courseCode = enrolData[1];

                    // Call your DAO to insert into Enrolment table
                    enrolMethod.enrolStudent(studentNum, courseCode);

                    out.writeObject("Student enrolled successfully.");
                    out.flush();
                }
                //Add course
                if (request.equalsIgnoreCase("Add_Course")) {
                    //expect an object
                    Course course = (Course) in.readObject();
                    courseMethod.addCourse(course);// add course into database
                    out.writeObject("Course added successfully");
                    out.flush();
                    //add Student
                } else if (request.equalsIgnoreCase("Add_Student")) {
                    Student student = (Student) in.readObject();
                    studMethod.addStudent(student);
                    out.writeObject("Student added successfully");
                    out.flush();

                    //retrieve all courses 
                } else if (request.equalsIgnoreCase("Display_Course")) {
                    // get the list of all courses from database
                    ArrayList<Course> list = courseMethod.displayCourse();
                    out.writeObject(list);
                    out.flush();

                } else if (request.equalsIgnoreCase("Display_Students")) {
                    // get the list of all students from database
                    ArrayList<Student> list = studMethod.displayStudents();
                    out.writeObject(list);
                    out.flush();
                }// Request: Get all students enrolled in a particular course
                else if (request.equalsIgnoreCase("Get_Students_In_Course")) {
                    // Receive course code from client
                    String courseCode = (String) in.readObject();
                    // Retrieve students from EnrolmentDAO
                    ArrayList<Student> studentsInCourse = enrolMethod.getStudentsInCourse(courseCode);
                    // Send back to client
                    out.writeObject(studentsInCourse);
                    out.flush();
                } // Request: Get all courses for a particular student
                else if (request.equalsIgnoreCase("Get_Courses_For_Student")) {
                    // Receive student number from client
                    String studentNum = (String) in.readObject();
                    // Retrieve courses from EnrolmentDAO
                    ArrayList<Course> coursesForStudent = enrolMethod.getCoursesForStudent(studentNum);
                    // Send back to client
                    out.writeObject(coursesForStudent);
                    out.flush();
                }

            }
        } catch (IOException ieo) {
            System.out.println("Connection closed: " + ieo.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (IOException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }
        }
    }
    //close connections

    private void closeConnection() throws IOException {
        out.close();
        in.close();
        client.close();
        System.out.println("Client connection closed.\n");
    }

    public static void main(String[] args) {
        new ServerSide();
    }
}
