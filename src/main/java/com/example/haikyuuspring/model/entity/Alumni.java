package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Position;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alumni")
@Getter
@Setter
@NoArgsConstructor
public class Alumni extends Character {
    private Boolean formerPlayer;

    @Enumerated(EnumType.STRING)
    private Position position;

    private Integer jerseyNumber;

    private Boolean formerCoach;
    private CoachingStyle coachingStyle;
}
