package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FanDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        String name,
        Double height,
        Integer age,
        Role role,
        Long schoolId,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String schoolName,
        String imgUrl,
        Boolean formerCoach,
        CoachingStyle coachingStyle
) {
}
