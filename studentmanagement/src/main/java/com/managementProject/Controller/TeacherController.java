package com.managementProject.Controller;


import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.TeacherBasicDTO;
import com.managementProject.DTO.TeacherWithTaughtCourseDTO;
import com.managementProject.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;



    //create Teacher
    @PostMapping("/AddTeacher")
    public ResponseEntity<APIResponseDTO>createTeacher(@RequestBody TeacherBasicDTO teacherBasicDTO){
           return  teacherService.createTeacher(teacherBasicDTO);
    }

      //getTeacherById with its coursetaught
    @GetMapping("/getTeacherById/{id}")
    public ResponseEntity<APIResponseDTO>getTeacherById(@PathVariable Long id){
          return teacherService.getTeacherById(id);
    }

    //GetALlTeacher
    @GetMapping("/getAllTeacher")
    public ResponseEntity<APIResponseDTO>getAllTeacher(){
        return teacherService.getAllTeacher();
    }
    //DeleteTeacherById

    @DeleteMapping("/deleteTeacherById/{id}")
    public ResponseEntity<APIResponseDTO>deleteTeacherById(@PathVariable Long id){
        return teacherService.deleteTeacherById(id);
    }

    //UpdateTeacherById
    @PutMapping("/updateTeacherById/{id}")
    public ResponseEntity<APIResponseDTO>updateTeacherById(@PathVariable Long id, @RequestBody TeacherBasicDTO teacherBasicDTO){
        return teacherService.updateTeacherById(id,teacherBasicDTO);
    }


}
