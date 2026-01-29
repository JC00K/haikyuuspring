package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AlumniDTO(
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
        Boolean formerPlayer,
        Position position,
        Integer jerseyNumber,
        Boolean formerCoach,
        CoachingStyle coachingStyle
) {
}
