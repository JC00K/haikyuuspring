package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.controller.dto.HaikyuuSchoolLookupView;
import com.example.haikyuuspring.model.entity.HaikyuuSchool;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface HaikyuuSchoolRepository extends JpaRepository<HaikyuuSchool, Long> {
    Optional<HaikyuuSchool> findByNameIgnoreCase(String name);

    List<HaikyuuSchool> findByPrefectureIgnoreCase(String prefecture);

    List<HaikyuuSchoolLookupView> findAllProjectedBy();

    Boolean existsByName(String name);

}
