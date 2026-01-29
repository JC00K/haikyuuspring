package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.FanDTO;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.repository.FanRepository;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.services.FanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FanServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private FanRepository fanRepository;

    @InjectMocks
    private FanService fanService;

    @Test
    @DisplayName("Create fan (school-bound)")
    void createFan_Success() {
        FanDTO dto = new FanDTO(null, "Fanatic", 159.2, 18, Role.FAN, 1L, "School", "https://example.url/image.png");
    }
}
