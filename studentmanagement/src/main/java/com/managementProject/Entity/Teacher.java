package com.managementProject.Entity;


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
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Email
    @NotBlank
    @Column(unique=true)
    private String email;
    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Courses> coursesTaught;
}
