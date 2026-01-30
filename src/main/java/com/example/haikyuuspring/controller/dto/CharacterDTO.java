package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CharacterDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        Long schoolId,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String schoolName,
        String name,
        Role role,
        Integer age,
        Year year,
        Double height,
        String imgUrl
) {}
