package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.PlayerRepository;
import com.example.haikyuuspring.services.PlayerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    @DisplayName("Create player should persist via PlayerRepository (stub)")
    void createPlayer_Success() {
        PlayerDTO dto = new PlayerDTO(null, "John Doe", 150.3, 16, null, null, 20L, "School", "https://example.url/image.png", Position.LIBERO, 10);
    }

    @Test
    @DisplayName("Duplicate player name should be rejected (stub)")
    void createPlayer_NameExists() {
        PlayerDTO dto = new PlayerDTO(null, "Existing", 150.7, 17, null, null, 20L, "School", "https://example.url/image.png", Position.MIDDLE_BLOCKER, 3);


    }
}
