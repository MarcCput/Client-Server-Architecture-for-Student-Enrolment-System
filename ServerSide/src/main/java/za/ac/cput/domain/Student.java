package za.ac.cput.domain;

import java.io.Serializable;

public class Student implements Serializable {

    private String studentNum;
    private String firstName;
    private String lastName;

    public Student() {
        
    }
    
    public Student(String studentNum, String firstName, String lastName) {
        this.studentNum = studentNum;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Student{" + "studentNum=" + studentNum + ", firstName=" + firstName + ", lastName=" + lastName + '}';
    }
    
}