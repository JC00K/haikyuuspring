package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;

public record AlumniDTO(
        Long id,
        String name,
        Double height,
        Integer age,
        Role role,
        Long schoolId,
        String schoolName,
        String imgUrl,
        Boolean formerPlayer,
        Position position,
        Integer jerseyNumber
) {
}
