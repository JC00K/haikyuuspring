package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
public class Coach extends Character {
    @Enumerated(EnumType.STRING)
    private CoachRole coachRole;
    private Boolean isRetired;
    private CoachingStyle coachingStyle;
}
