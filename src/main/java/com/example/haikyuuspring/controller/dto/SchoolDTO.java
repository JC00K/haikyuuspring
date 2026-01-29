package com.example.haikyuuspring.controller.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record SchoolDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        String name,
        String prefecture,
        RosterDTO roster,
        String motto,
        String colors,
        String mascot,
        String imgUrl
) {}
