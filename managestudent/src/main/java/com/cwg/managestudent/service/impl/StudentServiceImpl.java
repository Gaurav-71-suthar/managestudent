package com.cwg.managestudent.service.impl;

import com.cwg.managestudent.dto.CourseDTO;
import com.cwg.managestudent.dto.StudentDTO;
import com.cwg.managestudent.model.Courses;
import com.cwg.managestudent.model.Students;
import com.cwg.managestudent.repository.StudentRepository;
import com.cwg.managestudent.service.StudentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger log =
            LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {

        log.info("Checking student email: {}", email);

        return studentRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByEmailIgnoreCaseAndIdNot(String email,Long id) {
        log.info("email from update student");
        return studentRepository.existsByEmailIgnoreCaseAndIdNot(email,id);
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {

        log.info("Saving student data");

        Students students =
                mapper.map(studentDTO, Students.class);

        // Important
        students.setActive(true);

        studentRepository.save(students);

        return mapper.map(students, StudentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> getStudents(int page, int size) {

        log.info("Getting students from page: {}", page);

        PageRequest pageRequest =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "id")
                );

        return studentRepository
                .findByActiveTrue(pageRequest)
                .map(student ->
                        mapper.map(student, StudentDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        Students student=studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return mapper.map(student,StudentDTO.class);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Students student=studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No course found"));

        mapper.map(studentDTO,student);

        Students updated=studentRepository.save(student);

        return mapper.map(updated, StudentDTO.class);
    }

    @Override
    public List<Students> getAllStudents() {

        return studentRepository.findAll();
    }



}




