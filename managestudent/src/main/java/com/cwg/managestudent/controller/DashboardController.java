package com.cwg.managestudent.controller;

import com.cwg.managestudent.service.DashboardService;
import com.cwg.managestudent.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private static final Logger logger =
            LoggerFactory.getLogger(DashboardController.class);

    private final EnrollmentService enrollmentService;
    private final DashboardService dashboardService;

    public DashboardController(EnrollmentService enrollmentService,
                               DashboardService dashboardService) {
        this.enrollmentService = enrollmentService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("dashboardStats",dashboardService.getDashboardStats());
        model.addAttribute("students",
                enrollmentService.getRecentlyEnrolledStudents());

        return "dashboard";
    }
}