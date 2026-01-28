package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;

public record PlayerDTO (
        Long id,
        String name,
        Double height,
        Integer age,
        Year year,
        Role role,
        Long schoolId,
        String schoolName,
        String imgUrl,
        Position position,
        Integer jerseyNumber
) {}
