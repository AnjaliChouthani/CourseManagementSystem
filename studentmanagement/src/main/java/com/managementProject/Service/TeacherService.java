package com.managementProject.Service;


import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.CourseBasicDTO;
import com.managementProject.DTO.TeacherBasicDTO;
import com.managementProject.DTO.TeacherWithTaughtCourseDTO;
import com.managementProject.Entity.Teacher;
import com.managementProject.Repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherService {


    @Autowired
    private TeacherRepository teacherRepository;
    public ResponseEntity<APIResponseDTO> createTeacher(TeacherBasicDTO teacherBasicDTO) {
        //dto to entity
          Teacher teacher=new Teacher();
          teacher.setName(teacherBasicDTO.getName());
          teacher.setEmail(teacherBasicDTO.getEmail());

        Optional<Teacher>optionalTeacher= teacherRepository.findByNameAndEmail(teacher.getName(),teacher.getEmail());
        if(optionalTeacher.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMessage("Already Exist teacher with name "+ teacherBasicDTO.getName()+ " and email "+ teacherBasicDTO.getEmail());
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
        }
        //not found
         //save
       Teacher savedTeacher=teacherRepository.save(teacher);
        //entity to dto
        TeacherBasicDTO dto=new TeacherBasicDTO();
        dto.setEmail(savedTeacher.getEmail());
        dto.setName(savedTeacher.getName());
        APIResponseDTO responseDTO=new APIResponseDTO();
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("teacher",dto);
        responseDTO.setMeta(meta);
        responseDTO.setError(false);
        responseDTO.setMessage("new teacher addded");
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }
    //GetTeacherBYID
    public ResponseEntity<APIResponseDTO> getTeacherById(Long id) {
         Optional<Teacher>optionalTeacher= teacherRepository.findById(id);
         if(!optionalTeacher.isPresent()){
             APIResponseDTO responseDTO=new APIResponseDTO();
             responseDTO.setMessage("not found any teacher with this id "+id);
             responseDTO.setError(true);
             responseDTO.setMeta(new HashMap<>());
             return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
         }
        Teacher teacherDetails=optionalTeacher.get();
         //entity to dto
        TeacherWithTaughtCourseDTO teacherWithTaughtCourseDTO=new TeacherWithTaughtCourseDTO();
        teacherWithTaughtCourseDTO.setName(teacherDetails.getName());
        teacherWithTaughtCourseDTO.setEmail(teacherDetails.getEmail());


        //convert the list of course into DTO

        List<CourseBasicDTO> dtolist= teacherDetails.getCoursesTaught().stream().map(courses ->
        {
            CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
            courseBasicDTO.setTitle(courses.getTitle());
            courseBasicDTO.setDescription(courses.getDescription());
            return courseBasicDTO;
        }).toList();

        teacherWithTaughtCourseDTO.setCoursesList(dtolist);
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMessage("teacher with Id "+ id+" exist");
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("teacher",teacherWithTaughtCourseDTO);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    public ResponseEntity<APIResponseDTO> getAllTeacher() {
        List<Teacher>teacherList=teacherRepository.findAll();

        if(teacherList.isEmpty()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMeta(new HashMap<>());
            responseDTO.setMessage("No Teacher found ");
            responseDTO.setError(true);
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }

        List<TeacherBasicDTO>dtoList= teacherList.stream().map(teacher -> {
            TeacherBasicDTO details=new TeacherBasicDTO();
            details.setName(teacher.getName());
            details.setEmail(teacher.getEmail());
            return details;
        }).toList();
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMessage("teacher'sList are found ");
        HashMap<String,Object>meta=new HashMap<>();
        meta.put("teacherList",dtoList);
        responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
@Transactional
    public ResponseEntity<APIResponseDTO> deleteTeacherById(Long id) {
         Optional<Teacher> optionalTeacher= teacherRepository.findById(id);
         if(!optionalTeacher.isPresent()){
             APIResponseDTO responseDTO=new APIResponseDTO();
             responseDTO.setMeta(new HashMap<>());
             responseDTO.setMessage("Can't Deleted, No Teacher found with Id "+id);
             responseDTO.setError(true);
             return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
         }
         Teacher teacher=optionalTeacher.get();
         TeacherBasicDTO teacherBasicDTO=new TeacherBasicDTO();
         teacherBasicDTO.setName(teacher.getName());
         teacherBasicDTO.setEmail(teacher.getEmail());
         teacherRepository.delete(teacher);
         APIResponseDTO responseDTO=new APIResponseDTO();
         responseDTO.setError(false);
         responseDTO.setMessage("successfully teacher deleted ");
         HashMap<String,Object>meta=new HashMap<>();
         meta.put("deleted teacher info",teacherBasicDTO);
         responseDTO.setMeta(meta);
         return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
    public ResponseEntity<APIResponseDTO> updateTeacherById(Long id, TeacherBasicDTO teacherBasicDTO) {
        //dto to entity
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (!optionalTeacher.isPresent()) {
            APIResponseDTO responseDTO = new APIResponseDTO();
            responseDTO.setMeta(new HashMap<>());
            responseDTO.setMessage("No Teacher Found with Id " + id);
            responseDTO.setError(true);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        Teacher teacher = optionalTeacher.get();
        teacher.setName(teacherBasicDTO.getName());
        teacher.setEmail(teacherBasicDTO.getEmail());
        //doubt if i want to update the courselist with particular course then how to do it

        teacherRepository.save(teacher);

        //entity to dto
          TeacherBasicDTO dto=new TeacherBasicDTO();
          dto.setEmail(teacher.getEmail());
          dto.setName(teacher.getName());
          APIResponseDTO responseDTO=new APIResponseDTO();
          responseDTO.setError(false);
          responseDTO.setMessage("sucessfully updated ! ");
          HashMap<String,Object>meta=new HashMap<>();
          meta.put("updated teacher",dto);
          responseDTO.setMeta(meta);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
}
