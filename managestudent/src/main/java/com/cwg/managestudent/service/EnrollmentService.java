package com.cwg.managestudent.service;

import com.cwg.managestudent.dto.EnrollmentDTO;
import com.cwg.managestudent.dto.EnrollmentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

public interface EnrollmentService {

      void enrollStudentToCourses(EnrollmentDTO enrollmentDTO);

      Page<EnrollmentSummary> getEnrolledStudents(int page, int size);

      EnrollmentSummary findEnrolledStudentCourseDetails(Long studentId);


}
