package com.cwg.managestudent.dto;

public class DashboardStatsDTO {

    private long totalStudents;
    private long totalCourses;
    private String topPerformingCourse;
    private long studentsEnrolledThisMonth;
    public long getTotalStudents() {
        return totalStudents;
    }

    public long getStudentsEnrolledThisMonth() {
        return studentsEnrolledThisMonth;
    }

    public void setStudentsEnrolledThisMonth(long studentsEnrolledThisMonth) {
        this.studentsEnrolledThisMonth = studentsEnrolledThisMonth;
    }

    public String getTopPerformingCourse() {
        return topPerformingCourse;
    }

    public void setTopPerformingCourse(String topPerformingCourse) {
        this.topPerformingCourse = topPerformingCourse;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }
}
