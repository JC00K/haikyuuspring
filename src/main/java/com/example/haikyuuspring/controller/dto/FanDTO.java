package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Role;

public record FanDTO(
        Long id,
        String name,
        Double height,
        Integer age,
        Role role,
        Long schoolId,
        String schoolName,
        String imgUrl,
        Boolean formerCoach,
        CoachingStyle coachingStyle
) {
}
