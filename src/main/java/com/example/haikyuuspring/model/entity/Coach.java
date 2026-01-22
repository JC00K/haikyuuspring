package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.CoachEnum;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import jakarta.persistence.Entity;
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
    private CoachEnum coachType;
    private CoachingStyle coachingStyle;
}
