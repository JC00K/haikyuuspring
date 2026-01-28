package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.CoachRepository;
import com.example.haikyuuspring.services.CoachService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CoachRepository coachRepository;

    @InjectMocks
    private CoachService coachService;

    @Test
    @DisplayName("Create coach with HEAD role (stub)")
    void createHeadCoach_Success() {
        CoachDTO dto = new CoachDTO(null, "Coach A", 1L, "School", null, null, CoachingStyle.ATTACK,  CoachRole.HEAD);
        when(characterRepository.existsByName("Coach A")).thenReturn(false);

        // In a full implementation, you'd call: coachService.createCoach(dto);
        // verify(coachRepository).save(any(Coach.class));
    }
}
