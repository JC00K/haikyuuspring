package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.HaikyuuCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HaikyuuCharacterRepository extends JpaRepository<HaikyuuCharacter, Long> {
        List<HaikyuuCharacter> findBySchoolNameIgnoreCase(String name);

        List<HaikyuuCharacter> findByHeightGreaterThan(Double height);

        List<HaikyuuCharacter> findByHeightLessThan(Double height);

        Boolean existsByName(String name);

}
