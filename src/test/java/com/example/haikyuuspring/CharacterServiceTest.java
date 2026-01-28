package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.services.CharacterService;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterService characterService;

    @Test
    @DisplayName("Should perform cross-type name existence check before creating a character (stub)")
    void checkExistsByNameBeforeCreate() {
        CharacterDTO dto = new CharacterDTO(null, 1L, "SomeSchool", "Unique Name", Role.PLAYER, 20, Year.SECOND, 180.0, "https://example.url/image.png");
        when(characterRepository.existsByName("Unique Name")).thenReturn(false);

        // In a full implementation you would call: characterService.createCharacter(dto);
        // verify(characterRepository, atLeastOnce()).existsByName("Unique Name");
    }

    @Test
    @DisplayName("Should handle duplicate name scenario (stub)")
    void handleDuplicateName() {
        CharacterDTO dto = new CharacterDTO(null, 1L, "SomeSchool", "Existing Name", Role.PLAYER, 22, Year.THIRD, 182.0, "https://example.url/image.png");
        when(characterRepository.existsByName("Existing Name")).thenReturn(true);

        // In a full implementation you would assert an exception path:
        // assertThrows(RuntimeException.class, () -> characterService.createCharacter(dto));
    }
}
