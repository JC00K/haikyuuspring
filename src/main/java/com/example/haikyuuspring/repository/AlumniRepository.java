package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumniRepository extends JpaRepository<Alumni, Long> {
}
