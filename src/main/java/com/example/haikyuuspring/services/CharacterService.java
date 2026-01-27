package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.repository.RosterRepository;
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


    @Transactional
    public CharacterDTO assignSchoolToCharacter(Long characterId, String schoolName) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException(characterId));
        School school = schoolRepository.findByNameIgnoreCase(schoolName)
                .orElseThrow(() -> new ResourceNotFoundException(schoolName));

        if (character.getRoster() != null) {
            character.getRoster().removeCharacterFromRoster(character);
        }

        character.setSchool(school);

        return convertCharacterToDTO(characterRepository.save(character));
    }

    @Transactional
    public CharacterDTO assignYearToCharacter(Long characterId, Year year) {
        Character character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setYear(year);
        return convertCharacterToDTO(characterRepository.save(character));
    }

    @Transactional
    public CharacterDTO assignAgeToCharacter(Long characterId, Integer age) {
        Character character = characterRepository.findById(characterId).orElseThrow(() -> new ResourceNotFoundException(characterId));
        character.setAge(age);
        return convertCharacterToDTO(characterRepository.save(character));
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

    public List<CharacterDTO> findCharacterByYear(Year year) {
        return findAllCharacters().stream().filter((character) -> character.year() == year).toList();
    }

    public List<CharacterDTO> findByYearAndRole(Year year, Role role) {
        return findAllCharacters().stream().filter((character) -> character.year() == year && character.role() == role).toList();
    }

    public CharacterDTO findCharacterById(Long id) {
        return characterRepository.findById(id)
                .map(this::convertCharacterToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<CharacterDTO> mapListToDTO(List<Character> characters) {
        return characters.stream()
                .map(this::convertCharacterToDTO)
                .toList();
    }

    public CharacterDTO convertCharacterToDTO(Character character) {

        return new CharacterDTO(
                character.getId(),
                Optional.ofNullable(character.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(character.getSchool()).map(School::getName).orElse(null),
                character.getName(),
                character.getRole(),
                character.getAge(),
                character.getYear(),
                character.getHeight(),
                character.getImgUrl()
        );
    }
}
