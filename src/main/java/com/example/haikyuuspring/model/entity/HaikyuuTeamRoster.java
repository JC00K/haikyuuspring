package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team_rosters")
@Getter
@Setter
@NoArgsConstructor
public class HaikyuuTeamRoster {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "school_id")
    private HaikyuuSchool school;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HaikyuuCharacter> roster = new ArrayList<>();

    private String teamName;
    private String teamMotto;

    public void addCharacter(HaikyuuCharacter character) {
        if (character.getRole() != Role.PLAYER) {
            System.out.println("Character is not a player, and cannot be added to a team");
        } else {
        roster.add(character);
        }
        character.setSchool(school);
        character.setTeam(this);
    }

    public void removeCharacterFromRoster(HaikyuuCharacter character) {
        if (character.getRole() == Role.PLAYER) {
        roster.remove(character);
        character.setTeam(null);
        } else System.out.println("Character " + character.getName() + " is not a player, and it not associated with a roster");
    }




    public HaikyuuTeamRoster(HaikyuuSchool school) {
        this.school = school;
        if (school != null) {
            this.id = school.getId();
        }
    }

}
