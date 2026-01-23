package com.example.haikyuuspring.model.entity;

import com.example.haikyuuspring.model.enums.ManagementRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "management")
@Getter
@Setter
@NoArgsConstructor
public class Management extends Character{
    @Enumerated(EnumType.STRING)
    private ManagementRole managementRole;

}
