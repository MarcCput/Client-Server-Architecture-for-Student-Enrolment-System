package za.ac.cput.domain;

import java.io.Serializable;

public class Enrolment implements Serializable{
    private String StudentNum;
    private String courseCode;

    public Enrolment() {
    }

    public Enrolment(String StudentNum, String courseCode) {
        this.StudentNum = StudentNum;
        this.courseCode = courseCode;
    }

    public String getStudentNum() {
        return StudentNum;
    }

    public void setStudentNum(String StudentNum) {
        this.StudentNum = StudentNum;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "Enrollment{" + "StudentNum=" + StudentNum + ", courseCode=" + courseCode + '}';
    }
    
    
}
