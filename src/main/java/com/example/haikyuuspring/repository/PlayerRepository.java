package com.example.haikyuuspring.repository;

import com.example.haikyuuspring.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByHeightGreaterThan(Double height);

    List<Player> findByHeightLessThan(Double height);

    Boolean existsByName(String name);
}
