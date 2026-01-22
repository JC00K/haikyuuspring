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

    private List<StaffEnum> staffRoles = Arrays.asList(StaffEnum.ADVISOR, StaffEnum.MANAGER);
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
        character.setRoster(this);
    }

    public void removeCharacterFromRoster(Character character) {
        if (staffRoles.contains(character.getManagementRole())) {
            members.remove(character);
            character.setRoster(null);
        }
        if (character.getRole() == Role.PLAYER) {
            members.remove(character);
            character.setRoster(null);
        }
        if (character.getRole() == Role.COACH) {
            character.setRoster(null);
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
        return members.stream().filter((c) -> c.getManagementRole() == StaffEnum.MANAGER).toList();
    }

    public List<Character> getAdvisorsOnly() {
        return members.stream().filter((c) -> c.getManagementRole() == StaffEnum.ADVISOR).toList();
    }

    public Roster(School school) {
        this.school = school;
        if (school != null) {
            this.id = school.getId();
        }
    }

}
