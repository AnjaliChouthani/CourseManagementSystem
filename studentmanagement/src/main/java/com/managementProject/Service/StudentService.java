package com.managementProject.Service;


import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.CourseBasicDTO;
import com.managementProject.DTO.StudentBasicDTO;
import com.managementProject.DTO.StudentWithCourseDTO;
import com.managementProject.Entity.Courses;
import com.managementProject.Entity.Student;
import com.managementProject.Repository.CourseRepository;
import com.managementProject.Repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.html.HTMLTableCaptionElement;

import java.util.*;


@Service
public class StudentService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    public ResponseEntity<APIResponseDTO> createStudent(StudentWithCourseDTO studentWithCourseDTO) {


         Optional<Student> studentOptional= studentRepository.findByNameAndEmail(studentWithCourseDTO.getName(),studentWithCourseDTO.getEmail());
         if(studentOptional.isPresent()){
              APIResponseDTO responseDTO=new APIResponseDTO();
              responseDTO.setMessage("Already Exist Student with Name "+studentWithCourseDTO.getName()+" and Email "+studentWithCourseDTO.getEmail());
              responseDTO.setError(true);
              responseDTO.setMeta(new HashMap<>());
              return new ResponseEntity<>(responseDTO,HttpStatus.OK);
         }
         //dto to entity

        Student student=new Student();
         student.setName(studentWithCourseDTO.getName());
         student.setEmail(studentWithCourseDTO.getEmail());

         //iterate over dto list to fetch the courses
        List<Courses> coursesList=studentWithCourseDTO.getCoursesList().stream().map(
                dto->{
                    Courses courses=new Courses();
                    courses.setTitle(dto.getTitle());
                    courses.setDescription(dto.getDescription());
                    return courses;
                }
        ).toList();

         student.setCoursesList(coursesList);
        coursesList.forEach(c ->
        {
            if(c.getStudentList()==null){
                c.setStudentList(new ArrayList<>());
            }
            c.getStudentList().add(student);
        });

        Student savedStudent= studentRepository.save(student);
         //entity to dto


        StudentWithCourseDTO studentdto=new StudentWithCourseDTO();
        studentdto.setName(savedStudent.getName());
        studentdto.setEmail(savedStudent.getEmail());
        List<CourseBasicDTO> list=savedStudent.getCoursesList().stream().map(courses ->
                {
                    CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
                    courseBasicDTO.setDescription(courses.getDescription());
                    courseBasicDTO.setTitle(courses.getTitle());
                    return courseBasicDTO;
                }
                ).toList();
        studentdto.setCoursesList(list);
        //api response

        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setMessage("succesfully enrolled student");
        responseDTO.setError(false);
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("student details",studentdto);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }


    public ResponseEntity<APIResponseDTO> getAllStudent() {
        List<Student>studentlist=studentRepository.findAll();
        if(studentlist.isEmpty()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMessage("No student found ");
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        //if exist
       List<StudentWithCourseDTO>studentdto=studentlist.stream().map(
               student -> {
                   StudentWithCourseDTO student1=new StudentWithCourseDTO();
                   student1.setName(student.getName());
                   student1.setEmail(student.getEmail());
                   List<CourseBasicDTO>temp=student.getCoursesList().stream().map(courses -> {
                       CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
                       courseBasicDTO.setTitle(courses.getTitle());
                       courseBasicDTO.setDescription(courses.getDescription());
                       return courseBasicDTO;
                   }).toList();
                  student1.setCoursesList(temp);
                  return student1;
               }
       ).toList();
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMessage("StudentList");
        HashMap<String, Object>meta=new HashMap<>();
        meta.put("students",studentdto);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    //getStudentById

    public ResponseEntity<APIResponseDTO> getStudentById(Long id) {

        Optional<Student> optionalStudent=studentRepository.findById(id);
        if(!optionalStudent.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMessage("not found any student with ID "+id);
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }

        //convert entity to dto for response
       Student student= optionalStudent.get();
        StudentWithCourseDTO studentWithCourseDTO=new StudentWithCourseDTO();
        studentWithCourseDTO.setName(student.getName());
        studentWithCourseDTO.setEmail(student.getEmail());

        List<CourseBasicDTO> courseList=student.getCoursesList().stream().map(course->{
              CourseBasicDTO list=new CourseBasicDTO();
              list.setTitle(course.getTitle());
              list.setDescription(course.getDescription());
              return list;
        }).toList();
        studentWithCourseDTO.setCoursesList(courseList);
        //make response
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMessage("student found with Id " +id);
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("student",studentWithCourseDTO);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<APIResponseDTO> deleteStudentByEmail(String email) {

       Optional<Student>studentOptional= studentRepository.findByEmail(email);
       if(!studentOptional.isPresent()){
           APIResponseDTO responseDTO=new APIResponseDTO();
           responseDTO.setMeta(new HashMap<>());
           responseDTO.setError(true);
           responseDTO.setMessage("Can't Deleted, No Student Found with Email "+ email);
           return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
       }
       //if present delete from DB
        //entity to dto
        StudentBasicDTO studentBasicDTO=new StudentBasicDTO();
       studentBasicDTO.setEmail(studentOptional.get().getEmail());
       studentBasicDTO.setName(studentOptional.get().getName());

    if(studentOptional.get().getCoursesList()!=null) {
        studentOptional.get().getCoursesList().forEach(courses -> {
            courses.getStudentList().remove(studentOptional.get());
        });
    }
        studentRepository.delete(studentOptional.get());



        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setMessage("deleted Successfully");
        responseDTO.setError(false);
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("student",studentBasicDTO);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    public ResponseEntity<APIResponseDTO> updateStudentByEmail(String email,StudentWithCourseDTO studentWithCourseDTO) {

       Optional<Student>optionalStudent= studentRepository.findByEmail(email);
       if(!optionalStudent.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMeta(new HashMap<>());
            responseDTO.setError(true);
            responseDTO.setMessage("Not Found Any Student with Email "+ email);
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
       }
       Student student=optionalStudent.get();
       student.setName(studentWithCourseDTO.getName());
       student.setEmail(studentWithCourseDTO.getEmail());
       //course bhi update kr skte hai
        if(studentWithCourseDTO.getCoursesList()!=null && !studentWithCourseDTO.getCoursesList().isEmpty()) {
            student.getCoursesList().clear();

            List<Courses>coursesList=studentWithCourseDTO.getCoursesList().stream().map(course->{
                Courses course1=new Courses();
                course1.setTitle(course.getTitle());
                course1.setDescription(course.getDescription());
                return course1;

            }).toList();
            student.getCoursesList().addAll(coursesList);
        }

        Student savedStudent=studentRepository.save(student);


        //entity to dto

        StudentWithCourseDTO studentdto=new StudentWithCourseDTO();
        studentdto.setEmail(savedStudent.getEmail());
        studentdto.setName(savedStudent.getName());
       List<CourseBasicDTO>courseBasicDTOList= savedStudent.getCoursesList().stream().map(courses -> {
            CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
            courseBasicDTO.setTitle(courses.getTitle());
            courseBasicDTO.setDescription(courses.getDescription());
            return courseBasicDTO;

        }).toList();
       studentdto.setCoursesList(courseBasicDTOList);
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setMessage("updated!");
        responseDTO.setError(false);
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("student",studentdto);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }


}

