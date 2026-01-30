package com.example.haikyuuspring.model.entity;


import com.example.haikyuuspring.model.enums.CoachingStyle;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fans")
@Getter
@Setter
@NoArgsConstructor
public class Fan extends Character {
}
