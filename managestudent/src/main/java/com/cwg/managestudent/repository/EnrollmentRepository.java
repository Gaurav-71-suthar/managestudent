package com.cwg.managestudent.repository;

import com.cwg.managestudent.model.Enrollment;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

     boolean existsByStudentIdAndCourseId(Long studentId,Long courseId);

     @Query("""
            select count (distinct e.student.id) from
            Enrollment e
            where e.enrolledDate between :startDate and :endDate            

            """)
     long countDistinctStudentByEnrollDateBetween(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

}
