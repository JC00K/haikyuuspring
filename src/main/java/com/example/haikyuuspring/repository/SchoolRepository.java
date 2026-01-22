package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.controller.dto.SchoolLookupView;
import com.example.haikyuuspring.model.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByNameIgnoreCase(String name);

    List<School> findByPrefectureIgnoreCase(String prefecture);

    List<SchoolLookupView> findAllProjectedBy();

    Boolean existsByName(String name);

}
