package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerDTO (
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        String name,
        Double height,
        Integer age,
        Year year,
        Role role,
        Long schoolId,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String schoolName,
        String imgUrl,
        Position position,
        Integer jerseyNumber
) {}
