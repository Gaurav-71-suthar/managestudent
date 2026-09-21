package com.cwg.managestudent.repository;

import com.cwg.managestudent.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

     boolean existsByStudentIdAndCourseId(Long studentId,Long courseId);
}
