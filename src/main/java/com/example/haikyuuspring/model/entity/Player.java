package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.Position;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
public class Player extends Character {
    private Integer jerseyNumber;
    private Position position;
}
