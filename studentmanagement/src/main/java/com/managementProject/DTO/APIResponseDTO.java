package com.managementProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO {
    private Map<String,Object> meta=new HashMap<>();
    private String message;
    private boolean error;

}
