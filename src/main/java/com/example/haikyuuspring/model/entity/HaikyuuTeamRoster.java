package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    private List<HaikyuuCharacter> members = new ArrayList<>();

    private List<Role> staffRoles = Arrays.asList(Role.COACH, Role.ADVISOR, Role.MANAGER);
    private String teamName;
    private String teamMotto;

    public void addCharacter(HaikyuuCharacter character) {
        if (staffRoles.contains(character.getRole())) {
            members.add(character);

        }
        if (character.getRole() == Role.PLAYER) {
            members.add(character);
        }
        character.setSchool(school);
        character.setTeam(this);
    }

    public void removeCharacterFromRoster(HaikyuuCharacter character) {
        if (staffRoles.contains(character.getRole())) {
            members.remove(character);
            character.setTeam(null);
        }
        if (character.getRole() == Role.PLAYER) {
            members.remove(character);
            character.setTeam(null);
        }
    }

    public List<HaikyuuCharacter> getPlayers() {
        return members.stream().filter((c) -> c.getRole() == Role.PLAYER).toList();
    }

    public List<HaikyuuCharacter> getStaff() {
        return members.stream().filter((c) -> staffRoles.contains(c.getRole())).toList();
    }

    public List<HaikyuuCharacter> getCoachesOnly() {
        return members.stream().filter((c) -> c.getRole() == Role.COACH).toList();
    }

    public List<HaikyuuCharacter> getManagersOnly() {
        return members.stream().filter((c) -> c.getRole() == Role.MANAGER).toList();
    }

    public List<HaikyuuCharacter> getAdvisorsOnly() {
        return members.stream().filter((c) -> c.getRole() == Role.ADVISOR).toList();
    }

    public HaikyuuTeamRoster(HaikyuuSchool school) {
        this.school = school;
        if (school != null) {
            this.id = school.getId();
        }
    }

}
