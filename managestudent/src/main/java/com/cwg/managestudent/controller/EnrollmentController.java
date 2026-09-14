package com.cwg.managestudent.controller;

import org.springframework.ui.Model;
import com.cwg.managestudent.dto.EnrollmentDTO;
import com.cwg.managestudent.service.CourseService;
import com.cwg.managestudent.service.EnrollmentService;
import com.cwg.managestudent.service.StudentService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private static final Logger log= LoggerFactory.getLogger(EnrollmentController.class);

    private final CourseService courseService;
    private final StudentService studentService;

    public EnrollmentController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("/showEnroll")
    public String showEnroll(Model model){
        log.info("Get /enrollments/showEnroll - showing enrollment details");

        model.addAttribute("enrollmentsDto", new EnrollmentDTO());
        model.addAttribute("courseList",courseService.getAllCourses());
        model.addAttribute("studentList",studentService.getAllStudents());

        return "enroll-course";
    }

}
