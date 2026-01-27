package com.example.haikyuuspring.services;


import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.entity.Player;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.PlayerRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public PlayerDTO createPlayer(PlayerDTO playerInfo) {
        if (characterRepository.existsByName(playerInfo.name())) {
            throw new ResourceDuplicateException(playerInfo.name());
        }

        Player player = new Player();

        if (playerInfo.schoolId() != null) {
            School school = schoolRepository.findById(playerInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(playerInfo.schoolId()));
            player.setSchool(school);
        }

        Player newPlayer = playerRepository.save(player);

        return convertPlayerToDTO(newPlayer);
    }

    public List<PlayerDTO> findAllPlayers() {
        return mapPlayerListToDTO(playerRepository.findAll());
    }

    public List<PlayerDTO> findByPosition(Position position) {
        return findAllPlayers().stream().filter((player) -> player.position() == position).toList();
    }

    public List<PlayerDTO> findByYearAndPosition(Year year, Position position) {
        return findAllPlayers().stream().filter((player) -> player.year() == year && player.position() == position).toList();
    }

    public List<PlayerDTO> findByJerseyNumber(Integer jerseyNumber) {
        return findAllPlayers().stream().filter((player) -> player.jerseyNumber().equals(jerseyNumber)).toList();
    }

    public List<PlayerDTO> mapPlayerListToDTO(List<Player> players) {
        return players.stream()
                .map(this::convertPlayerToDTO)
                .toList();
    }

    public PlayerDTO convertPlayerToDTO(Player player) {

        return new PlayerDTO(
                player.getId(),
                player.getName(),
                Optional.ofNullable(player.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(player.getSchool()).map(School::getName).orElse(null),
                player.getRole(),
                player.getPosition(),
                player.getAge(),
                player.getYear(),
                player.getJerseyNumber(),
                player.getImgUrl()
        );
    }
}
