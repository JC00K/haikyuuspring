package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanRepository extends JpaRepository<Fan, Long> {
}
