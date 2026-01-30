package com.example.haikyuuspring.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    public void addPlayerToRoster(Player player) {
        player.setRoster(this);
        this.players.add(player);
    }

    public void addCoachToRoster(Coach coach) {
        coach.setRoster(this);
        this.coaches.add(coach);
    }

    public void addManagementToRoster(Management management) {
        management.setRoster(this);
        this.management.add(management);
    }

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
