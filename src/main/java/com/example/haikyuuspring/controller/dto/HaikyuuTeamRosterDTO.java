package com.example.haikyuuspring.controller.dto;

import java.util.Collections;
import java.util.List;

public record HaikyuuTeamRosterDTO (
        List<HaikyuuCharacterDTO> roster,
        String motto
    ) {}


