package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.StaffEnum;
import com.example.haikyuuspring.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "team_rosters")
@Getter
@Setter
public class Roster {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "roster")
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "roster")
    private List<Coach> coaches = new ArrayList<>();

    @OneToMany(mappedBy = "roster")
    private List<Management> management = new ArrayList<>();

    public void removeCharacterFromRoster(Character character) {
        character.setRoster(null);
        switch (character) {
            case Player player -> this.players.remove(character);
            case Coach coach -> this.coaches.remove(character);
            case Management management1 -> this.management.remove(character);
            default -> {
            }
        }
    }
}
