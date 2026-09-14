package com.cwg.managestudent.controller;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.dto.StudentDTO;
import com.cwg.managestudent.service.StudentService;

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
@RequestMapping("/students")
public class StudentController {

    private static final Logger log =
            LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // =========================
    // ADD STUDENT PAGE
    // =========================

    @GetMapping("/new")
    public String showCreateStudent(Model model) {

        log.info("GET /students/new - showing create student page");

        model.addAttribute("studentDto", new StudentDTO());

        return "add-student";
    }


    // =========================
    // STUDENT LIST
    // =========================

    @GetMapping("/list")
    public String listStudent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            @RequestParam(value = "message", required = false) String message) {

        log.info("GET /students/list - showing student list page");

        Page<StudentDTO> students =
                studentService.getStudents(page, size);

        model.addAttribute("students", students);
        model.addAttribute("message", message);

        return "students";
    }


    // =========================
    // CREATE STUDENT
    // =========================

    @PostMapping("/save")
    public String createStudent(
            @Valid @ModelAttribute("studentDto") StudentDTO studentDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        log.info("POST /students/save - create student request received");

        if (bindingResult.hasErrors()) {
            return "add-student";
        }

        if (studentService.existsByEmailIgnoreCase(studentDto.getEmail())) {

            log.error("POST /students/save - email must be unique");

            bindingResult.rejectValue(
                    "email",
                    null,
                    "Email must be unique"
            );

            return "add-student";
        }

        studentService.createStudent(studentDto);

        redirectAttributes.addAttribute(
                "message",
                "Student created successfully!!"
        );

        return "redirect:/students/list";
    }

    @GetMapping("/{id}")
    public String getStudentById(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "view-student";
    }

    @GetMapping("/{id}/edit")
    public String editStudent(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("studentDto", student);
        return "edit-student";
    }

    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("studentDto") StudentDTO studentDto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.info("POST /students/update - update student request received");

        if (bindingResult.hasErrors()) {
            return "edit-student";
        }
        if (studentService.existsByEmailIgnoreCaseAndIdNot(studentDto.getEmail(), id)) {
            log.error("Post /update -email must be unique.");
            bindingResult.rejectValue("email", null, "Email must be unique.");
            return "edit-student";
        }
        studentService.updateStudent(id, studentDto);
        redirectAttributes.addAttribute("message", "Student is added ");

        return "redirect:/students/list";

    }
}