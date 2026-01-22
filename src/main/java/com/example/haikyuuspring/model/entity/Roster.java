package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.Management;
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
@NoArgsConstructor
public class Roster {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Character> members = new ArrayList<>();

    private List<Management> staffRoles = Arrays.asList(Management.ADVISOR, Management.MANAGER);
    private String teamName;
    private String teamMotto;

    public void addCharacter(Character character) {
        if (staffRoles.contains(character.getManagementRole())) {
            members.add(character);

        }
        if (character.getRole() == Role.PLAYER) {
            members.add(character);
        }


        character.setSchool(school);
        character.setTeam(this);
    }

    public void removeCharacterFromRoster(Character character) {
        if (staffRoles.contains(character.getManagementRole())) {
            members.remove(character);
            character.setTeam(null);
        }
        if (character.getRole() == Role.PLAYER) {
            members.remove(character);
            character.setTeam(null);
        }
        if (character.getRole() == Role.COACH) {
            character.setTeam(null);
        }
    }

    public List<Character> getPlayers() {
        return members.stream().filter((c) -> c.getRole() == Role.PLAYER).toList();
    }

    public List<Character> getStaff() {
        return members.stream().filter((c) -> staffRoles.contains(c.getManagementRole())).toList();
    }

    public List<Character> getCoachesOnly() {
        return members.stream().filter((c) -> c.getRole() == Role.COACH).toList();
    }

    public List<Character> getManagersOnly() {
        return members.stream().filter((c) -> c.getManagementRole() == Management.MANAGER).toList();
    }

    public List<Character> getAdvisorsOnly() {
        return members.stream().filter((c) -> c.getManagementRole() == Management.ADVISOR).toList();
    }

    public Roster(School school) {
        this.school = school;
        if (school != null) {
            this.id = school.getId();
        }
    }

}
