package com.cwg.managestudent.service.impl;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.dto.EnrollmentDTO;
import com.cwg.managestudent.dto.EnrollmentSummary;
import com.cwg.managestudent.model.Courses;
import com.cwg.managestudent.model.Enrollment;
import com.cwg.managestudent.model.Students;
import com.cwg.managestudent.repository.CourseRepository;
import com.cwg.managestudent.repository.EnrollmentRepository;
import com.cwg.managestudent.repository.StudentRepository;
import com.cwg.managestudent.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequestMapping("/enrollments")
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger log= LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final  EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                          StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          ModelMapper modelMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository=studentRepository;
        this.courseRepository=courseRepository;
        this.modelMapper=modelMapper;
    }


    @Override
    public void enrollStudentToCourses(EnrollmentDTO enrollmentDTO) {
        log.info("request from enrollStudentToCourses ");

        Students student=studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(()-> new RuntimeException("student not found"));
        for(Long coursesId:enrollmentDTO.getCourseIds()){
            Courses course=courseRepository.findById(coursesId)
                    .orElseThrow(()-> new RuntimeException("course not found"));
            if(enrollmentRepository.existsByStudentIdAndCourseId(enrollmentDTO.getStudentId(),coursesId)){
                continue;
            }

            Enrollment enrollment=new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);

             student.getEnrollments().add(enrollment);
             course.getEnrollments().add(enrollment);

            enrollmentRepository.save(enrollment);
        }



    }

    @Override
    public Page<EnrollmentSummary> getEnrolledStudents(int page, int size) {
    log.info("list of enrolled students from: {} ",page);

        PageRequest pageRequest=PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"id"));
        return studentRepository.findEnrolledStudents(pageRequest)
                .map(students -> {
                    EnrollmentSummary dto=new EnrollmentSummary();
                    dto.setStudentId(students.getId());
                    dto.setStudentName(students.getFirstName() + " " + students.getLastName());
                    dto.setEmail(students.getEmail());

                    dto.setCourseCount(students.getEnrollments().size());
                    BigDecimal totalFee=students.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getFee())
                            .filter(fee -> fee != null)
                            .reduce(BigDecimal.ZERO,BigDecimal::add);
                    dto.setTotalFee(totalFee);
                    return dto;

                });


    }

    @Override
    public EnrollmentSummary findEnrolledStudentCourseDetails(Long studentId) {

        return studentRepository.findEnrolledStudentCourseDetails(studentId)
                .map(students ->{
                    EnrollmentSummary dto=new EnrollmentSummary();
                    dto.setStudentId(students.getId());
                    dto.setStudentName(students.getFirstName() + " " + students.getLastName());
                    dto.setEmail(students.getEmail());

                    dto.setCourseCount(students.getEnrollments().size());
                    BigDecimal totalFee=students.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getFee())
                            .filter(fee -> fee != null)
                            .reduce(BigDecimal.ZERO,BigDecimal::add);
                    dto.setTotalFee(totalFee);

                    List<CourseDTO> courseList=students.getEnrollments().stream()
                            .map(Enrollment::getCourse)
                            .map(course ->modelMapper.map(course,CourseDTO.class))
                            .collect(Collectors.toList());
                    dto.setCourseList(courseList);
                    return dto;
                })
        .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public List<EnrollmentSummary> getRecentlyEnrolledStudents() {
        log.info("list of recently enrolled students");

        PageRequest pageRequest=PageRequest.of(0, 5,Sort.by(Sort.Direction.DESC,"id"));
        return studentRepository.findEnrolledStudents(pageRequest)
                .map(students -> {
                    EnrollmentSummary dto=new EnrollmentSummary();
                    dto.setStudentId(students.getId());
                    dto.setStudentName(students.getFirstName() + " " + students.getLastName());
                    dto.setEmail(students.getEmail());

                    dto.setCourseCount(students.getEnrollments().size());
                    BigDecimal totalFee= students.getEnrollments().stream()
                            .map(enrollment -> enrollment.getCourse().getFee())
                            .filter(fee -> fee!=null)
                            .reduce(BigDecimal.ZERO,BigDecimal::add);
                    dto.setTotalFee(totalFee);
                    return dto;
                })
                .getContent();

    }
}
