package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.HaikyuuCharacter;
import com.example.haikyuuspring.model.entity.HaikyuuSchool;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.HaikyuuCharacterRepository;
import com.example.haikyuuspring.repository.HaikyuuSchoolRepository;
import com.example.haikyuuspring.repository.HaikyuuTeamRosterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HaikyuuCharacterService {
    private final HaikyuuCharacterRepository characterRepository;
    private final HaikyuuSchoolRepository schoolRepository;
    private final HaikyuuTeamRosterRepository teamRosterRepository;

    @Transactional
    public HaikyuuCharacterDTO createCharacter(HaikyuuCharacterDTO characterInfo) {
        if (characterRepository.existsByName(characterInfo.name())) {
            throw new ResourceDuplicateException(characterInfo.name());
        }

        HaikyuuCharacter character = new HaikyuuCharacter();

        if (characterInfo.schoolId() != null) {
            HaikyuuSchool school = schoolRepository.findById(characterInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(characterInfo.schoolId()));
            character.setSchool(school);
        }

        character.setName(characterInfo.name());
        character.setAge(characterInfo.age());
        character.setYear(characterInfo.year());
        character.setHeight(characterInfo.height());
        character.setRole(characterInfo.role());
        character.setPosition(characterInfo.position());
        character.setImgUrl(characterInfo.imgUrl());

        HaikyuuCharacter newCharacter = characterRepository.save(character);

        return convertToDTO(newCharacter);
    }

    @Transactional
    public HaikyuuCharacterDTO assignSchoolToCharacter(Long characterId, String schoolName) {
        HaikyuuCharacter character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        HaikyuuSchool school =  schoolRepository.findByNameIgnoreCase(schoolName).orElseThrow(() -> new ResourceNotFoundException(schoolName));
        if (character.getTeam() != null) {
            character.getTeam().removeCharacterFromRoster(character);
        }
        if (character.getSchool() != null) {
            character.removeCharacterFromSchool(character);
        }
        character.setSchool(school);
        school.addCharacter(character);
        return convertToDTO(characterRepository.save(character));
    }

    @Transactional
    public HaikyuuCharacterDTO assignYearToCharacter(Long characterId, Year year) {
        HaikyuuCharacter character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setYear(year);
        return convertToDTO(characterRepository.save(character));
    }

    @Transactional
    public HaikyuuCharacterDTO assignAgeToCharacter(Long characterId, Integer age) {
        HaikyuuCharacter character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setAge(age);
        return convertToDTO(characterRepository.save(character));
    }

    public List<HaikyuuCharacterDTO> findBySchool(String school) {
        return mapListToDTO(characterRepository.findBySchoolNameIgnoreCase(school));
    }

    public List<HaikyuuCharacterDTO> findByHeightGreaterThan(Double height) {
        return mapListToDTO(characterRepository.findByHeightGreaterThan(height));
    }

    public List<HaikyuuCharacterDTO> findByHeightLessThan(Double height) {
        return mapListToDTO(characterRepository.findByHeightLessThan(height));
    }

    public List<HaikyuuCharacterDTO> findAllCharacters() {
        return mapListToDTO(characterRepository.findAll());
    }

    public List<HaikyuuCharacterDTO> findByAge(Integer age) {
        return findAllCharacters().stream().filter((character) -> Objects.equals(character.age(), age)).toList();
    }

    public List<HaikyuuCharacterDTO> findByRole(Role role) {
        return findAllCharacters().stream().filter((character) -> character.role() == role).toList();
    }

    public List<HaikyuuCharacterDTO> findByPosition(Position position) {
        return findAllCharacters().stream().filter((character) -> character.position() == position).toList();
    }

    public List<HaikyuuCharacterDTO> findCharacterByYear(Year year) {
        return findAllCharacters().stream().filter((character) -> character.year() == year).toList();
    }

    public List<HaikyuuCharacterDTO> findByYearAndPosition(Year year, Position position) {
        return findAllCharacters().stream().filter((character) -> character.year() == year && character.position() == position).toList();
    }

    public List<HaikyuuCharacterDTO> findByYearAndRole(Year year, Role role) {
        return findAllCharacters().stream().filter((character) -> character.year() == year && character.role() == role).toList();
    }

    public HaikyuuCharacterDTO findCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<HaikyuuCharacterDTO> mapListToDTO(List<HaikyuuCharacter> characters) {
        return characters.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public HaikyuuCharacterDTO convertToDTO(HaikyuuCharacter character) {

        return new HaikyuuCharacterDTO(
                character.getId(),
                character.getName(),
                Optional.ofNullable(character.getSchool()).map(HaikyuuSchool::getId).orElse(null),
                Optional.ofNullable(character.getSchool()).map(HaikyuuSchool::getName).orElse(null),
                character.getRole(),
                character.getPosition(),
                character.getAge(),
                character.getYear(),
                character.getHeight(),
                character.getImgUrl()
        );
    }

}
