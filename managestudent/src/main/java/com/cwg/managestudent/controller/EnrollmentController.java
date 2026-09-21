package com.cwg.managestudent.controller;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.dto.EnrollmentSummary;
import com.cwg.managestudent.dto.StudentDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import com.cwg.managestudent.dto.EnrollmentDTO;
import com.cwg.managestudent.service.CourseService;
import com.cwg.managestudent.service.EnrollmentService;
import com.cwg.managestudent.service.StudentService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private static final Logger log= LoggerFactory.getLogger(EnrollmentController.class);

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public EnrollmentController(CourseService courseService, StudentService studentService,EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/showEnroll")
    public String showEnroll(Model model){
        log.info("Get /enrollments/showEnroll");

        model.addAttribute("enrollmentsDto", new EnrollmentDTO());

        Page<CourseDTO> courses=courseService.getCourses(0,100);
        model.addAttribute("courseList",courseService.getAllCourses());
        Page<StudentDTO> students=studentService.getStudents(0,100);
        model.addAttribute("studentList",studentService.getAllStudents());

        return "enroll-course";
    }


    @PostMapping("/enrollCourse")
    public String enrollCourse(@Valid @ModelAttribute  ("enrollmentsDto") EnrollmentDTO enrollmentDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("Post /enrollments/enrollCourse-enrollment request received");

        if (bindingResult.hasErrors()) {

            model.addAttribute("courseList", courseService.getAllCourses());
            model.addAttribute("studentList", studentService.getAllStudents());
            return "enroll-course";
        }

        enrollmentService.enrollStudentToCourses(enrollmentDto);
        redirectAttributes.addAttribute("message","Enrollment has been successfully enrolled");

        log.info("Enrollment has been successfully enrolled");


        return "enroll-course";
    }
    @GetMapping("/enrollmentList")
    public String enrollmentList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size,
        Model model,
        @RequestParam(value = "message", required = false) String message) {

        log.info("GET /enrollmentlist - showing enrolled list page");

        Page<EnrollmentSummary> students =
                enrollmentService.getEnrolledStudents(page, size);

        model.addAttribute("students", students);
        model.addAttribute("message", message);


        return "enrolled-students";
    }
    @GetMapping("/studentEnrollmentGetDetails/{studentId}")
    public String studentEnrollmentGetDetails(@PathVariable Long studentId,Model model,
                                              @RequestParam(defaultValue = "enrollments")String source){

        EnrollmentSummary  student
                =enrollmentService.findEnrolledStudentCourseDetails(studentId);

        model.addAttribute("student",student);
        model.addAttribute("source",source);


        return "enrollment-details";
    }


}
