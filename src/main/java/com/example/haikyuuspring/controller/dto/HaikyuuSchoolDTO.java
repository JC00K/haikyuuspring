package com.example.haikyuuspring.controller.dto;


public record HaikyuuSchoolDTO(
        Long id,
        String name,
        String prefecture,
        HaikyuuTeamRosterDTO team,
        String colors,
        String mascot,
        String imgUrl
) {}
