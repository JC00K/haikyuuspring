package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.Management;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "characters")
public class HaikyuuCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private HaikyuuSchool school;

    @ManyToOne()
    @JoinColumn(name = "team_id")
    private HaikyuuTeamRoster team;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Management managementRole;

    @Enumerated(EnumType.STRING)
    private Position position;

    private Integer age;
    private Year year;
    private Double height;

    private String imgUrl;

    public void removeCharacterFromSchool(HaikyuuCharacter character) {
        HaikyuuSchool charSchool = character.getSchool();
        if (character.getSchool() != null) {
            character.setSchool(null);
            System.out.println("Character " + character.getName() + " has been removed from " + charSchool.getName());
        }
        if (character.getRole() == Role.PLAYER) {
            team.removeCharacterFromRoster(character);
            character.setTeam(null);
        } else System.out.println("Character " + character.getName() + " is not a player, and it not associated with " + charSchool + "'s roster");
    }
}
