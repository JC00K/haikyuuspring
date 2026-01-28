package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.FanDTO;
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
    @DisplayName("Create fan (school-bound) (stub)")
    void createFan_Success() {
        FanDTO dto = new FanDTO(null, "Fanatic", 1L, "School", false, null);
        when(characterRepository.existsByName("Fanatic")).thenReturn(false);

        // In a full implementation, you'd call: fanService.createFan(dto);
        // verify(fanRepository).save(any(Fan.class));
    }
}
