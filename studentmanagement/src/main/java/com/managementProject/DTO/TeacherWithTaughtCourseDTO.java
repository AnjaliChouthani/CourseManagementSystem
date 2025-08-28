package com.managementProject.DTO;


import com.managementProject.Entity.Courses;
import lombok.Data;

import java.util.List;

@Data
public class TeacherWithTaughtCourseDTO {

    private String email;
    private String name;
    private List<CourseBasicDTO> coursesList;
}
