package com.managementProject.Repository;

import com.managementProject.DTO.CourseBasicDTO;
import com.managementProject.Entity.Courses;
import com.managementProject.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {


    Optional<Student> findByNameAndEmail(String name, String email);


    Optional<Student> findByEmail(String email);
}
