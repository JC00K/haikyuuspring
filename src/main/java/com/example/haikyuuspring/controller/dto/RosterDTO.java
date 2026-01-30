package com.example.haikyuuspring.controller.dto;

import java.util.List;

public record RosterDTO(
        List<PlayerDTO> players,
        List<CoachDTO> coaches,
        List<ManagementDTO> management
    ) {}


