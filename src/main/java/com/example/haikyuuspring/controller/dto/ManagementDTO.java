package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ManagementDTO(
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
        ManagementRole managementRole
) {
}
