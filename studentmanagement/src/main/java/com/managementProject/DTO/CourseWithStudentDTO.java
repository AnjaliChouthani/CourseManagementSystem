package com.managementProject.DTO;

import com.managementProject.Entity.Student;
import lombok.Data;

import java.util.List;

@Data
public class CourseWithStudentDTO {

    private String title;
    private String description;
    private List<StudentBasicDTO> studentList;
}

