package com.managementProject.DTO;


import com.managementProject.Entity.Teacher;
import lombok.Data;

@Data
public class CourseWithTeacherDTO {
    private String title;
    private String description;
    private TeacherBasicDTO teacher;
}
