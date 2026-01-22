package com.example.haikyuuspring.controller.dto;

import java.util.List;

public record RosterDTO(
        List<CharacterDTO> players,
        List<CharacterDTO> staff,
        String motto
    ) {}


