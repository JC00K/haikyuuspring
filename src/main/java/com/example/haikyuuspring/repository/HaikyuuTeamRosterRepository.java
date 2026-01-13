package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.HaikyuuTeamRoster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HaikyuuTeamRosterRepository extends JpaRepository<HaikyuuTeamRoster, Long> {

    Optional<HaikyuuTeamRoster> findBySchoolName(String name);

    boolean existsBySchoolId(Long schoolId);
}
