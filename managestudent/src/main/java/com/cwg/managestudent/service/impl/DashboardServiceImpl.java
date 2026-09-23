package com.cwg.managestudent.service.impl;

import com.cwg.managestudent.dto.DashboardStatsDTO;
import com.cwg.managestudent.repository.CourseRepository;
import com.cwg.managestudent.repository.EnrollmentRepository;
import com.cwg.managestudent.repository.StudentRepository;
import com.cwg.managestudent.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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
        LocalDateTime startDate=currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate=currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        long studentEnrolledThisMonth=enrollmentRepository.countDistinctStudentByEnrollDateBetween(startDate, endDate);

        DashboardStatsDTO dashboardStatsDTO=new DashboardStatsDTO();
        dashboardStatsDTO.setTotalStudents(totalStudents);
        dashboardStatsDTO.setTotalCourses(totalCourse);
        dashboardStatsDTO.setTopPerformingCourse(topPerformingCourse);
        dashboardStatsDTO.setStudentsEnrolledThisMonth(studentEnrolledThisMonth);

        return dashboardStatsDTO;
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
