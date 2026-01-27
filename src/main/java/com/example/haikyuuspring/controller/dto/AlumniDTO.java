package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;

public record AlumniDTO(
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Boolean formerPlayer,
        Position position,
        Integer jerseyNumber,
        String imgUrl
) {
}
