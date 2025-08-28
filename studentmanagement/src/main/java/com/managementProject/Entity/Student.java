package com.managementProject.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @ManyToMany(mappedBy ="studentList",cascade = CascadeType.ALL



    )
    private List<Courses> coursesList;
}
