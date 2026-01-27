package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Role;

public record CoachDTO(
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Role role,
        Integer age,
        CoachingStyle coachingStyle,
        CoachRole coachRole
) {
}
