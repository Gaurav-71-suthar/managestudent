package com.cwg.managestudent.repository;


import com.cwg.managestudent.model.Students;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Students,Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    Page<Students> findByActiveTrue(Pageable pageable);
    List<Students> findByActiveTrue();

    @EntityGraph(attributePaths = {"enrollments","enrollments.course"})
    @Query(value="""
           select distinct s
           from Students s
           join s.enrollments e
           """,
           countQuery = """
               select count(distinct s)
               from Students s
               join s.enrollments e
               """)

    Page<Students>findEnrolledStudents(Pageable pageable);



    @Query("""
           select distinct s
           from Students s
           join fetch s.enrollments e
           join fetch  e.course 
           where s.id= :studentId           
           """)
    Optional<Students> findEnrolledStudentCourseDetails(@Param("studentId") Long studentId);
}
