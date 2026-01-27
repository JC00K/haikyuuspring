package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach, Long> {
}
