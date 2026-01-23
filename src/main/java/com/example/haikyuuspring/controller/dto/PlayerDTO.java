package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;

public record PlayerDTO (
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Role role,
        Position position,
        Integer age,
        Integer jerseyNumber,
        String imgUrl
) {}
