package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.repository.TeamRosterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;
    private final TeamRosterRepository teamRosterRepository;

    @Transactional
    public CharacterDTO createCharacter(CharacterDTO characterInfo) {
        if (characterRepository.existsByName(characterInfo.name())) {
            throw new ResourceDuplicateException(characterInfo.name());
        }

        Character character = new Character();

        if (characterInfo.schoolId() != null) {
            School school = schoolRepository.findById(characterInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(characterInfo.schoolId()));
            character.setSchool(school);
        }

        character.setName(characterInfo.name());
        character.setAge(characterInfo.age());
        character.setYear(characterInfo.year());
        character.setHeight(characterInfo.height());
        character.setRole(characterInfo.role());
        character.setPosition(characterInfo.position());
        character.setImgUrl(characterInfo.imgUrl());

        Character newCharacter = characterRepository.save(character);

        return convertToDTO(newCharacter);
    }

    @Transactional
    public CharacterDTO assignSchoolToCharacter(Long characterId, String schoolName) {
        Character character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        School school =  schoolRepository.findByNameIgnoreCase(schoolName).orElseThrow(() -> new ResourceNotFoundException(schoolName));
        if (character.getRoster() != null) {
            character.getRoster().removeCharacterFromRoster(character);
        }
        if (character.getSchool() != null) {
            character.removeCharacterFromSchool(character);
        }
        character.setSchool(school);
        school.addCharacter(character);
        return convertToDTO(characterRepository.save(character));
    }

    @Transactional
    public CharacterDTO assignYearToCharacter(Long characterId, Year year) {
        Character character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setYear(year);
        return convertToDTO(characterRepository.save(character));
    }

    @Transactional
    public CharacterDTO assignAgeToCharacter(Long characterId, Integer age) {
        Character character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setAge(age);
        return convertToDTO(characterRepository.save(character));
    }

    public List<CharacterDTO> findBySchool(String school) {
        return mapListToDTO(characterRepository.findBySchoolNameIgnoreCase(school));
    }

    public List<CharacterDTO> findByHeightGreaterThan(Double height) {
        return mapListToDTO(characterRepository.findByHeightGreaterThan(height));
    }

    public List<CharacterDTO> findByHeightLessThan(Double height) {
        return mapListToDTO(characterRepository.findByHeightLessThan(height));
    }

    public List<CharacterDTO> findAllCharacters() {
        return mapListToDTO(characterRepository.findAll());
    }

    public List<CharacterDTO> findByAge(Integer age) {
        return findAllCharacters().stream().filter((character) -> Objects.equals(character.age(), age)).toList();
    }

    public List<CharacterDTO> findByRole(Role role) {
        return findAllCharacters().stream().filter((character) -> character.role() == role).toList();
    }

    public List<CharacterDTO> findByPosition(Position position) {
        return findAllCharacters().stream().filter((character) -> character.position() == position).toList();
    }

    public List<CharacterDTO> findCharacterByYear(Year year) {
        return findAllCharacters().stream().filter((character) -> character.year() == year).toList();
    }

    public List<CharacterDTO> findByYearAndPosition(Year year, Position position) {
        return findAllCharacters().stream().filter((character) -> character.year() == year && character.position() == position).toList();
    }

    public List<CharacterDTO> findByYearAndRole(Year year, Role role) {
        return findAllCharacters().stream().filter((character) -> character.year() == year && character.role() == role).toList();
    }

    public CharacterDTO findCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<CharacterDTO> mapListToDTO(List<Character> characters) {
        return characters.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CharacterDTO convertToDTO(Character character) {

        return new CharacterDTO(
                character.getId(),
                character.getName(),
                Optional.ofNullable(character.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(character.getSchool()).map(School::getName).orElse(null),
                character.getRole(),
                character.getPosition(),
                character.getAge(),
                character.getYear(),
                character.getHeight(),
                character.getImgUrl()
        );
    }

}
