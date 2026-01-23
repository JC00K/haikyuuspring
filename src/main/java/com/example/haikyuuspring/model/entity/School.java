package com.example.haikyuuspring.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String prefecture;
    private String imgUrl;
    private String mascot;
    private String colors;

    @OneToOne(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private Roster roster;
}
