package com.cwg.managestudent.controller;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.service.CourseService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class CourseController {

    private static final Logger log =
            LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }




    // =========================
    // ADD COURSE PAGE
    // =========================

    @GetMapping("/new")
    public String showCreateCourse(Model model) {

        log.info("GET /course/new - showing create course page");

        model.addAttribute("courseDto", new CourseDTO());

        return "add-course";
    }


    // =========================
    // COURSE LIST
    // =========================

    @GetMapping("/list")
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            @RequestParam(value = "message", required = false) String message) {

        log.info("======================================");
        log.info("PAGE REQUESTED: {}", page);
        log.info("PAGE SIZE: {}", size);

        Page<CourseDTO> courses =
                courseService.getCourses(page, size);

        log.info("CURRENT PAGE: {}", courses.getNumber());
        log.info("TOTAL PAGES: {}", courses.getTotalPages());
        log.info("TOTAL ELEMENTS: {}", courses.getTotalElements());
        log.info("FIRST: {}", courses.isFirst());
        log.info("LAST: {}", courses.isLast());
        log.info("======================================");

        model.addAttribute("courses", courses);
        model.addAttribute("message", message);

        return "courses";
    }


    // =========================
    // CREATE COURSE
    // =========================

    @PostMapping
    public String createCourse(
            @Valid @ModelAttribute("courseDto") CourseDTO courseDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("POST /course - create course request received");

        // Validation errors
        if (bindingResult.hasErrors()) {

            log.info("Validation error while creating course");

            return "add-course";
        }


        // Check duplicate course code
        if (courseService.existsByCourseCode(
                courseDTO.getCourseCode())) {

            log.info("Course code already exists: {}",
                    courseDTO.getCourseCode());

            bindingResult.rejectValue(
                    "courseCode",
                    null,
                    "Course code must be unique"
            );

            return "add-course";
        }


        // Create course
        courseService.createCourse(courseDTO);

        redirectAttributes.addAttribute(
                "message",
                "Course is created successfully"
        );

        log.info("Course successfully created");

        return "redirect:/course/list";
    }


    // =========================
    // VIEW COURSE
    // =========================

    @GetMapping("/{id}")
    public String getCourseById(
            @PathVariable Long id,
            Model model) {

        log.info("GET /course/{} - showing course", id);

        CourseDTO courseDTO =
                courseService.getCourseById(id);

        model.addAttribute("course", courseDTO);

        return "view-course";
    }


    // =========================
    // EDIT COURSE PAGE
    // =========================

    @GetMapping("/{id}/edit")
    public String editCourse(
            @PathVariable Long id,
            Model model) {

        log.info("GET /course/{}/edit - showing edit page", id);

        CourseDTO courseDTO =
                courseService.getCourseById(id);

        model.addAttribute("courseDto", courseDTO);

        return "edit-course";
    }


    // =========================
    // UPDATE COURSE
    // =========================

    @PostMapping("/{id}/update")
    public String updateCourse(
            @PathVariable Long id,
            @Valid @ModelAttribute("courseDto") CourseDTO courseDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info(
                "POST /course/{}/update - update course request received",
                id
        );


        // Validation errors
        if (bindingResult.hasErrors()) {

            log.info(
                    "Validation error while updating course {}",
                    id
            );

            return "edit-course";
        }


        // Check duplicate course code
        if (courseService.existsByCourseCode(
                courseDTO.getCourseCode())) {

            log.info(
                    "Course code already exists: {}",
                    courseDTO.getCourseCode()
            );

            bindingResult.rejectValue(
                    "courseCode",
                    null,
                    "Course code must be unique"
            );

            return "edit-course";
        }


        // Update course
        courseService.updateCourse(id, courseDTO);

        redirectAttributes.addAttribute(
                "message",
                "Course is updated successfully"
        );

        log.info(
                "Course {} successfully updated",
                id
        );

        return "redirect:/course/list";
    }
}