package com.managementProject.Exception;


import com.managementProject.DTO.APIResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
       @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponseDTO>exceptionHandler(AppException appException) {
       APIResponseDTO responseDTO=new APIResponseDTO();
       responseDTO.setMessage(appException.getMessage());
       responseDTO.setError(true);
           HashMap<String,Object>meta=new HashMap<>();
           meta.put("data",appException.getData());
       responseDTO.setMeta(meta);
       return new ResponseEntity<>(responseDTO,appException.getHttpStatus());

       }
}
