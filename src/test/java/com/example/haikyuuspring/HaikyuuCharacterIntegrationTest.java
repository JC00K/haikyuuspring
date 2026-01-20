package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuSchoolDTO;
import com.example.haikyuuspring.controller.dto.HaikyuuTeamRosterDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.HaikyuuCharacterService;
import com.example.haikyuuspring.services.HaikyuuSchoolService;
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
public class HaikyuuCharacterIntegrationTest {

    @Autowired
    private HaikyuuSchoolService schoolService;

    @Autowired
    private HaikyuuCharacterService characterService;

    private Long savedSchoolId;

    @BeforeEach
    void setup() {
        // Create the school first (this runs before every @Test)
        HaikyuuTeamRosterDTO team = new HaikyuuTeamRosterDTO( null, null, "School motto would go here");
        HaikyuuSchoolDTO schoolRequest = new HaikyuuSchoolDTO(
                null, "Inarizaki", "Hyogo", team, "Black and White", "Foxes", "https://img.url"
        );

        HaikyuuSchoolDTO savedSchool = schoolService.createSchool(schoolRequest);
        this.savedSchoolId = savedSchool.id(); // Now this works!
    }

    @Test
    void shouldCreateAndLinkCharacterToSchool() {
        // 1. Arrange: Prepare the character data
        HaikyuuCharacterDTO characterRequest = new HaikyuuCharacterDTO(
                null,"Atsumu Miya", savedSchoolId, "Inarizaki High", Role.PLAYER,
                Position.SETTER, 17, Year.SECOND, 183.6, "https://hinata.url"
        );

        // 2. Act: Call your service
        HaikyuuCharacterDTO savedCharacter = characterService.createCharacter(characterRequest);
        // 3. Assert: Verify the results
        assertThat(savedCharacter).isNotNull()
                        .extracting(HaikyuuCharacterDTO::name, HaikyuuCharacterDTO::schoolId)
                        .containsExactly("Atsumu Miya", savedSchoolId);

    }
    @Test
    @DisplayName("Should throw an exception when a character already exists")
    void shouldThrowException_WhenCharacterNameAlreadyExists() {
        // 1. Arrange: Create the first character
        HaikyuuCharacterDTO firstAtsumu = new HaikyuuCharacterDTO(
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
        List<HaikyuuCharacterDTO> result = characterService.findByAge(nonExistentAge);

        assertThat(result).isNotNull().isEmpty();
    }
}
