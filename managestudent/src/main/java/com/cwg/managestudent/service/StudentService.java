package com.cwg.managestudent.service;

import com.cwg.managestudent.dto.StudentDTO;
import com.cwg.managestudent.model.Students;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email,Long id);
    StudentDTO createStudent(StudentDTO studentDTO);

    Page<StudentDTO> getStudents(int page, int size);

    StudentDTO getStudentById(Long id);

    StudentDTO updateStudent(Long id, StudentDTO studentDTO);

    List<Students>getAllStudents();


}
