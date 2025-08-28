package com.managementProject.Repository;


import com.managementProject.Entity.Teacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    Optional<Teacher> findByNameAndEmail(String name, @Email @NotBlank String email);

    Optional<Teacher> findByEmail(String email);
}
