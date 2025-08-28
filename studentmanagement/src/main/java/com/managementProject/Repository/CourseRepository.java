package com.managementProject.Repository;

import com.managementProject.Entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Courses,Long> {

    Optional<Courses> findByTitle(String title);

    void deleteByTitle(String title);
}
