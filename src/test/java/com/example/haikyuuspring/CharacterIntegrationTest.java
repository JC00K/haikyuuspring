package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.controller.dto.SchoolDTO;
import com.example.haikyuuspring.controller.dto.RosterDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.CharacterService;
import com.example.haikyuuspring.services.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CharacterIntegrationTest {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private CharacterService characterService;

    private Long savedSchoolId;

    @BeforeEach
    void setup() {
        // Create the school first (this runs before every @Test)
        RosterDTO team = new RosterDTO( null, null, "School motto would go here");
        SchoolDTO schoolRequest = new SchoolDTO(
                null, "Inarizaki", "Hyogo", team, "Black and White", "Foxes", "https://img.url"
        );

        SchoolDTO savedSchool = schoolService.createSchool(schoolRequest);
        this.savedSchoolId = savedSchool.id(); // Now this works!
    }

    @Test
    void shouldCreateAndLinkCharacterToSchool() {
        // 1. Arrange: Prepare the character data
        CharacterDTO characterRequest = new CharacterDTO(
                null,"Atsumu Miya", savedSchoolId, "Inarizaki High", Role.PLAYER,
                Position.SETTER, 17, Year.SECOND, 183.6, "https://hinata.url"
        );

        // 2. Act: Call your service
        CharacterDTO savedCharacter = characterService.createCharacter(characterRequest);
        // 3. Assert: Verify the results
        assertThat(savedCharacter).isNotNull()
                        .extracting(CharacterDTO::name, CharacterDTO::schoolId)
                        .containsExactly("Atsumu Miya", savedSchoolId);

    }
    @Test
    @DisplayName("Should throw an exception when a character already exists")
    void shouldThrowException_WhenCharacterNameAlreadyExists() {
        // 1. Arrange: Create the first character
        CharacterDTO firstAtsumu = new CharacterDTO(
                null,"Atsumu Miya", savedSchoolId, "Inarizaki High", Role.PLAYER,
                Position.SETTER, 17, Year.SECOND, 183.6, "https://hinata.url"
        );
        characterService.createCharacter(firstAtsumu);

        // 2. Act & Assert: Try to create a second one with the same name
        // This tells JUnit: "We expect a RuntimeException here"
        assertThrows(RuntimeException.class, () -> {
            characterService.createCharacter(firstAtsumu);
        });
    }

    @Test
    @DisplayName("Should return empty list when no characters match the searched age")
    void shouldReturnEmptyList_WhenNoCharactersOfSpecificAgeFound() {
        int nonExistentAge = 99;
        List<CharacterDTO> result = characterService.findByAge(nonExistentAge);

        assertThat(result).isNotNull().isEmpty();
    }
}
