package com.managementProject.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courses extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
    @ManyToMany
    @JoinTable(name="courses_student",
    joinColumns = @JoinColumn(name="courses_id"),
    inverseJoinColumns = @JoinColumn(name="student_id"))
    private List<Student>studentList;
}
