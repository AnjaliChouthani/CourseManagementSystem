package com.managementProject.Controller;


import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.StudentWithCourseDTO;
import com.managementProject.Service.StudentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studentcontroller")
public class StudentController{

     @Autowired
    private StudentService studentService;
//
     @PostMapping("/createStudent")
    public ResponseEntity<APIResponseDTO>createStudent(@RequestBody StudentWithCourseDTO studentWithCourseDTO){
         return studentService.createStudent(studentWithCourseDTO);
     }

     @GetMapping("/getAllStudent")

     public ResponseEntity<APIResponseDTO>getAllStudent(){
          return   studentService.getAllStudent();
     }


    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<APIResponseDTO>getStudentById(@PathVariable Long id){
         return studentService.getStudentById(id);
    }

    //updation

    @PutMapping("/updateStudent")

    public ResponseEntity<APIResponseDTO>updateStudentByEmail(@RequestParam String email,@RequestBody StudentWithCourseDTO studentWithCourseDTO){


         return studentService.updateStudentByEmail(email,studentWithCourseDTO);
    }
    @DeleteMapping("/deleteStudent")
    public ResponseEntity<APIResponseDTO>deleteStudentByEmail(@RequestParam String email){
         return studentService.deleteStudentByEmail(email);

    }
}
