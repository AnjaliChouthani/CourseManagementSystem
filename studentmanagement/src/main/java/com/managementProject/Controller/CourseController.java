package com.managementProject.Controller;


import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.CourseBasicDTO;
import com.managementProject.Entity.Courses;
import com.managementProject.Repository.TeacherRepository;
import com.managementProject.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherRepository teacherRepository;

  @PostMapping("/createCourse")
    public ResponseEntity<APIResponseDTO> addCourse(@RequestBody CourseBasicDTO courseDto) {
      return courseService.addCourse(courseDto);
  }
//get all course
  @GetMapping("/getAllCourses")
    public ResponseEntity<APIResponseDTO> getAllCourseList(){
   return courseService.getAllCourseList();

  }
//getCourse by id
  @GetMapping("/getCourseById/{id}")
    public ResponseEntity<APIResponseDTO> getCourseById(@PathVariable Long id){
      return courseService.getCourseById(id);
  }
  //delete API's
  @DeleteMapping("/deleteCourseByTitle")
    public ResponseEntity<APIResponseDTO>deleteCourseByTitle(@RequestParam String title){
     return courseService.deleteCourseByTitle(title);
  }
  @PutMapping("/updateCourse")
    public ResponseEntity<APIResponseDTO>updateCourse(@RequestBody CourseBasicDTO courseBasicDTO,@RequestParam String title){
      return courseService.updateCourse(courseBasicDTO,title);
  }

  //courseassignedtoteacher
  @PostMapping("/assignedCourse")
  public ResponseEntity<APIResponseDTO>assignedCourse(@RequestParam String courseTitle, @RequestParam String email){
          return courseService.assignedCourse(courseTitle,email);
  }

}
