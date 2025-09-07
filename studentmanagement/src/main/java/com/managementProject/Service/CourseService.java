package com.managementProject.Service;

import com.managementProject.DTO.APIResponseDTO;
import com.managementProject.DTO.CourseBasicDTO;
import com.managementProject.DTO.TeacherWithTaughtCourseDTO;
import com.managementProject.Entity.Courses;
import com.managementProject.Entity.Teacher;
import com.managementProject.Repository.CourseRepository;
import com.managementProject.Repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CourseService {

     @Autowired
    private CourseRepository courseRepository;

     @Autowired
     private TeacherRepository teacherRepository;
    public ResponseEntity<APIResponseDTO> addCourse(CourseBasicDTO courseDto) {
        Optional<Courses> coursesOptional=courseRepository.findByTitle(courseDto.getTitle());
        if(coursesOptional.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            responseDTO.setMessage("Already Existed " + courseDto.getTitle()+ " course ");
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_ACCEPTABLE);
        }
        //dto to entity
        Courses courseEntity=new Courses();
        courseEntity.setTitle(courseDto.getTitle());
        courseEntity.setDescription(courseDto.getDescription());
//save the entity into the db
         Courses savedCourse= courseRepository.save(courseEntity);
         //return dto so convert itt
         CourseBasicDTO basicDto=new CourseBasicDTO();

         basicDto.setDescription(savedCourse.getDescription());
         basicDto.setTitle(savedCourse.getTitle());
          APIResponseDTO responseDTO=new APIResponseDTO();
          responseDTO.setMessage("Course Added");
          responseDTO.setError(false);
          HashMap<String,Object>meta=new HashMap<>();
          meta.put("course ",basicDto);
          responseDTO.setMeta(meta);
         return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }
    public ResponseEntity<APIResponseDTO> getAllCourseList() {
           List<Courses>coursesList=courseRepository.findAll();
           if(coursesList.isEmpty()){
               APIResponseDTO responseDTO=new APIResponseDTO();
               responseDTO.setMessage("No Courses Found ");
               responseDTO.setError(true);
               responseDTO.setMeta(new HashMap<>());
               return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
           }
           List<CourseBasicDTO>dtoList=coursesList.stream().map(courses -> {
               CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
               courseBasicDTO.setDescription(courses.getDescription());
               courseBasicDTO.setTitle(courses.getTitle());
               return courseBasicDTO;
           }).distinct().toList();
           APIResponseDTO responseDTO=new APIResponseDTO();
           responseDTO.setError(false);
           responseDTO.setMessage("Courses List Found");
           responseDTO.getMeta().put("courseList",dtoList);
           return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    public ResponseEntity<APIResponseDTO> getCourseById(Long id) {

        Optional<Courses>courseOpt=courseRepository.findById(id);
        if(!courseOpt.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMessage("Not Found any Course with this Id "+ id);
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }
        CourseBasicDTO courseBasicDTO=new CourseBasicDTO();

        courseBasicDTO.setTitle(courseOpt.get().getTitle());
        courseBasicDTO.setDescription(courseOpt.get().getDescription());
       APIResponseDTO responseDTO=new APIResponseDTO();
       Map<String, Object>meta=new HashMap<>();
       meta.put("course ", courseBasicDTO);
       responseDTO.setMeta(meta);
       responseDTO.setMessage("Found Courses with this ID "+ id);
       responseDTO.setError(false);
       return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }
    @Transactional
    public  ResponseEntity<APIResponseDTO> deleteCourseByTitle(String title) {

        Optional<Courses> coursesOpt=courseRepository.findByTitle(title);
        if(!coursesOpt.isPresent()){
            APIResponseDTO responseDTO=new APIResponseDTO();
            responseDTO.setMessage("not found any course with this "+ title);
            responseDTO.setError(true);
            responseDTO.setMeta(new HashMap<>());
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }
        courseRepository.deleteByTitle(title);
        APIResponseDTO responseDTO=new APIResponseDTO();
        responseDTO.setMessage("course "+title+" deleted successfully ");
        responseDTO.setError(false);
        responseDTO.setMeta(new HashMap<>());
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    public ResponseEntity<APIResponseDTO> updateCourse(CourseBasicDTO courseBasicDTO, String title) {
       Optional<Courses>coursesOptional= courseRepository.findByTitle(title);
       if(!coursesOptional.isPresent()){
           APIResponseDTO responseDTO=new APIResponseDTO();
           responseDTO.setMeta(new HashMap<>());
           responseDTO.setMessage("Cannot Update, No Course found with name "+ title);
           responseDTO.setError(true);
           return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
       }
      Courses coursesDetail= coursesOptional.get();
       coursesDetail.setTitle(courseBasicDTO.getTitle());
       coursesDetail.setDescription(courseBasicDTO.getDescription());
       Courses updatedCourseDetails=courseRepository.save(coursesDetail);
       CourseBasicDTO dto=new CourseBasicDTO();
       dto.setTitle(updatedCourseDetails.getTitle());
       dto.setDescription(updatedCourseDetails.getDescription());

       APIResponseDTO responseDTO=new APIResponseDTO();
       responseDTO.setMessage("Successfully Updated the course ");
       responseDTO.setError(false);
       Map<String,Object>meta=new HashMap<>();
       meta.put("updatedCourse",dto);
       responseDTO.setMeta(meta);
       return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

}
 //@Transactional
//    public ResponseEntity<APIResponseDTO> assignedCourse(String courseTitle ,String email) {
//             Optional<Courses>optionalCourses=courseRepository.findByTitle(courseTitle);
//             Optional<Teacher>optionalTeacher=teacherRepository.findByEmail(email);
//             if(!optionalCourses.isPresent()){
//                 APIResponseDTO responseDTO=new APIResponseDTO();
//                 responseDTO.setMeta(new HashMap<>());
//                 responseDTO.setError(true);
//                 responseDTO.setMessage("not found "+ courseTitle+" course");
//                 return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
//             }
//             if(!optionalTeacher.isPresent()){
//                 APIResponseDTO responseDTO=new APIResponseDTO();
//                 responseDTO.setMeta(new HashMap<>());
//                 responseDTO.setError(true);
//                 responseDTO.setMessage("not found  any teacher with Email "+email);
//                 return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
//             }
//
//
//      Courses courses=optionalCourses.get();
//             Teacher teacher=optionalTeacher.get();
//      courses.setTeacher(teacher);
//             courseRepository.save(courses);
//        //entity to dto
//        TeacherWithTaughtCourseDTO teacherWithTaughtCourseDTO=new TeacherWithTaughtCourseDTO();
//             teacherWithTaughtCourseDTO.setName(teacher.getName());
//             teacherWithTaughtCourseDTO.setEmail(teacher.getEmail());
////for courses
//             List<CourseBasicDTO>courseBasicDTOS=new ArrayList<>();
//             CourseBasicDTO courseBasicDTO=new CourseBasicDTO();
//             courseBasicDTO.setTitle(courses.getTitle());
//             courseBasicDTO.setDescription(courses.getDescription());
//             courseBasicDTOS.add(courseBasicDTO);
//             teacherWithTaughtCourseDTO.setCoursesList(courseBasicDTOS);
//
//             APIResponseDTO responseDTO=new APIResponseDTO();
//             responseDTO.setMessage("assigned !");
//             responseDTO.setError(false);
//             responseDTO.setMeta(Map.of("assigned course",teacherWithTaughtCourseDTO));
//             return new ResponseEntity<>(responseDTO,HttpStatus.OK);
//    }
//}
