package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CharacterDTO(
        Long id,
        String name,
        Long schoolId,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String schoolName,
        Role role,
        Position position,
        Integer age,
        Year year,
        Double height,
        String imgUrl
) {}
