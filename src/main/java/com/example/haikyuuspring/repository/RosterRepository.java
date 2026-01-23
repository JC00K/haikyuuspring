package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RosterRepository extends JpaRepository<Roster, Long> {

    Optional<Roster> findBySchoolName(String name);

    boolean existsBySchoolId(Long schoolId);
}
