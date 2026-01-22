package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {
        List<Character> findBySchoolNameIgnoreCase(String name);

        List<Character> findByHeightGreaterThan(Double height);

        List<Character> findByHeightLessThan(Double height);

        Boolean existsByName(String name);

}
