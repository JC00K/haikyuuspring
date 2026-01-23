package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;

public record CoachDTO(
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Role role,
        Position position,
        Integer age,
        String coachingStyle,
        CoachRole coachRole
) {
}
