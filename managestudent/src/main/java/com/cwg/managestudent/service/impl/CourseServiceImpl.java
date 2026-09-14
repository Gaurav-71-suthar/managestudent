package com.cwg.managestudent.service.impl;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.model.Courses;
import com.cwg.managestudent.repository.CourseRepository;
import com.cwg.managestudent.service.CourseService;
import org.apache.commons.logging.Log;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private static final Logger log =
            LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final ModelMapper mapper;

    public CourseServiceImpl(CourseRepository courseRepository,
                             ModelMapper mapper) {
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {

        log.info("Creating course with code: {}", courseDTO.getCourseCode());

        Courses courses = mapper.map(courseDTO, Courses.class);
        courses.setActive(true);

        courseRepository.save(courses);

        return mapper.map(courses, CourseDTO.class);
    }

    @Override
    public boolean existsByCourseCode(String code) {

        log.info("Checking if course code exists: {}", code);

        return courseRepository.existsByCourseCodeIgnoreCase(code);
    }

    @Override
    public boolean existsByCourseCodeAndIdNot(String code, Long id) {
        log.info("code from update page:{},id: {}",code,id);
        return courseRepository.existsByCourseCodeIgnoreCaseAndIdNot(code, id) ;
    }

    @Override
    @Transactional(readOnly = true)

    public Page<CourseDTO> getCourses(int page, int size) {

        log.info("Getting list of courses from page: {}", page);

        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "id")
                );

        return courseRepository
                .findByActiveTrue(pageRequest)
                .map(courses -> mapper.map(courses, CourseDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
       Courses course= courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No course found"));

        return mapper.map(course, CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Courses course=courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No course found"));

        mapper.map(courseDTO,course);

        Courses updated=courseRepository.save(course);

        return mapper.map(updated,CourseDTO.class);
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> mapper.map(course,CourseDTO.class))
                .collect(Collectors.toList());
    }
}