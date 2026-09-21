package com.cwg.managestudent.service.impl;

import com.cwg.managestudent.dto.DashboardStatsDTO;
import com.cwg.managestudent.repository.CourseRepository;
import com.cwg.managestudent.repository.EnrollmentRepository;
import com.cwg.managestudent.repository.StudentRepository;
import com.cwg.managestudent.service.DashboardService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardServiceImpl implements DashboardService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    DashboardServiceImpl(EnrollmentRepository enrollmentRepository,
                         StudentRepository studentRepository,
                         CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalCourse = courseRepository.count();

        String topPerformingCourse=getTopPerformingCourse();

        YearMonth currentMonth=YearMonth.now();
        LocalDate startDate=currentMonth.atDay(1);
        LocalDate endDate=currentMonth.atEndOfMonth();

        long studentEnrolledThisMonth=enrollmentRepository.countDistinctStudentByEnrollDateBetween(startDate, endDate);


        return null;
    }
    private String getTopPerformingCourse(){
        return enrollmentRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(e-> e.getCourse().getCourseName(),Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

    }

}
