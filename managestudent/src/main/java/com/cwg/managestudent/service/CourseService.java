package com.cwg.managestudent.service;

import com.cwg.managestudent.dto.CourseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);



    boolean existsByCourseCode(String courseCode);

    boolean existsByCourseCodeAndIdNot(String code,Long id);

    Page<CourseDTO> getCourses(int page ,int size);

    CourseDTO getCourseById(Long id);
    CourseDTO updateCourse(Long id,CourseDTO courseDTO);

    List<CourseDTO> getAllCourses();


}
