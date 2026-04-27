package za.ac.cput.clientsocket;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import za.ac.cput.domain.Course;
import za.ac.cput.domain.Student;

public class ClientSocket {

    private Socket server;          // Used to connect to the server
    private ObjectOutputStream out; // Send data to the server
    private ObjectInputStream in;   // Receives data from the server

    // Constructor: establishes the connection to the server
    public ClientSocket() {
        try {
            // Connect to the server running on localhost (127.0.0.1) at port 6666
            server = new Socket("127.0.0.1", 7777);
            System.out.println("Connected to the server.");

            // Create streams for object communication
            out = new ObjectOutputStream(server.getOutputStream());
            out.flush(); // flush ensures the stream header is sent immediately
            in = new ObjectInputStream(server.getInputStream());
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    // Sends a request 
    public void AddCourse(String courseCode, String description) throws IOException {
        out.writeObject("Add_Course");
        out.flush();
        // sends object request to the server
        out.writeObject(new Course(courseCode, description));
        out.flush();
    }

    public void AddStudent(String studentNum, String firstName, String lastName) throws IOException {
        out.writeObject("Add_Student");
        out.flush();
        // sends object request to the server
        out.writeObject(new Student(studentNum, firstName, lastName));
        out.flush();
    }

    // Sends a course enrollment request (for students)
    public void enrolStudent(String studentNum, String courseCode) {
        try {
            out.writeObject("Enrol_Student");
            out.flush();
            //sends a string array
            out.writeObject(new String[]{studentNum, courseCode});
            out.flush();

            //response from the server
            String response = (String) in.readObject();
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error: " + cnfe.getMessage());
        }
    }

    public void displayCourse() {
        try {
            String request = "Display_Course";
            out.writeObject(request);
            out.flush();

            // Expecting a list of courses from the server
            ArrayList<Course> courses = (ArrayList<Course>) in.readObject();
            System.out.println("Available Courses: ");
            for (Course c : courses) { //iterate each course 
                System.out.println(c.getCourseCode() + " - " + c.getCourseDescription());
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error requesting courses: " + e.getMessage());
        }
    }

    public void displayStudents() {
        try {
            String request = "Display_Students";
            out.writeObject(request);
            out.flush();
            // // Expecting a list of students from the server
            ArrayList<Student> students = (ArrayList<Student>) in.readObject();
            System.out.println("Students:");
            for (Student s : students) {
                System.out.println(s.getStudentNum() + " - " + s.getFirstName() + " " + s.getLastName());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error requesting students: " + e.getMessage());
        }
    }

   
    public void displayStudentsInCourse(String courseCode) {
        try {
            out.writeObject("Get_Students_In_Course");
            out.flush();
            out.writeObject(courseCode);
            out.flush();

            // Expecting a list of students from the server
            ArrayList<Student> students = (ArrayList<Student>) in.readObject();
            System.out.println("Students enrolled in " + courseCode + ":");
            for (Student s : students) {
                System.out.println(s.getStudentNum() + " - " + s.getFirstName() + " " + s.getLastName());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error fetching students for course: " + e.getMessage());
        }
    }

    public void displayCoursesForStudent(String studentNum) {
        try {
            out.writeObject("Get_Courses_For_Student");
            out.flush();
            out.writeObject(studentNum);
            out.flush();

            // Expecting a list of courses from the server
            ArrayList<Course> courses = (ArrayList<Course>) in.readObject();
            System.out.println("Courses for student " + studentNum + ":");
            for (Course c : courses) {
                System.out.println(c.getCourseCode() + " - " + c.getCourseDescription());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error fetching courses for student: " + e.getMessage());
        }
    }

    // Closes the connection safely.
    public void closeConnection() {
        try {
            out.close();
            in.close();
            server.close();
            System.out.println("Connection closed.");
        } catch (IOException ioe) {
            System.out.println("Error closing connection: " + ioe.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        //create an instance of a client
        ClientSocket client = new ClientSocket();

        try {
            client.AddCourse("ADF262S", "APP DEV FUNDAMENTALS");
            System.out.println((String) client.in.readObject()); //  confirmation

            client.displayCourse();

            client.AddStudent("21003648", "David", "Le Roux");
            System.out.println((String) client.in.readObject()); // confirmation

            client.displayStudents();

            client.displayStudentsInCourse("ADF262S");
            client.displayCoursesForStudent("21003648");

            client.enrolStudent("21003648", "ADF262S");
            //close connection
            client.closeConnection();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error: " + cnfe.getMessage());

        }

    }
}
