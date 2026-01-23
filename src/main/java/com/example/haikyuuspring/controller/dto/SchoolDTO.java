package com.example.haikyuuspring.controller.dto;


public record SchoolDTO(
        Long id,
        String name,
        String prefecture,
        RosterDTO roster,
        String motto,
        String colors,
        String mascot,
        String imgUrl
) {}
