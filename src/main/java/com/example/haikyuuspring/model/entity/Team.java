package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private String teamName;
    private String teamMotto;
    private String colors;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Character> characters;

    public Character getCoach() {
        if (characters == null) return null;
        return characters.stream().filter(c -> c.getRole() == Role.COACH).findFirst().orElse(null);
    }
}
