package com.example.haikyuuspring.controller.dto;

public record FanDTO(
        Long id,
        String name,
        Long schoolId,
        String schoolName,
        Boolean formerCoach,
        String imgUrl
) {
}
