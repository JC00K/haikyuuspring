package com.example.haikyuuspring.controller.dto;

import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.model.enums.Role;

public record ManagementDTO(
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Role role,
        Integer age,
        ManagementRole managementRole,
        String imgUrl
) {
}
