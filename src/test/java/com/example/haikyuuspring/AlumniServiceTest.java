package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.repository.AlumniRepository;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.services.AlumniService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumniServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private AlumniRepository alumniRepository;

    @InjectMocks
    private AlumniService alumniService;

    @Test
    @DisplayName("Create alumni (former player optional) (stub)")
    void createAlumni_Success() {
        AlumniDTO dto = new AlumniDTO(null, "Alumni A", 1L, "School", true, Position.SETTER, 12, null);
        when(characterRepository.existsByName("Alumni A")).thenReturn(false);

        // alumniService.createAlumni(dto);
        // verify(alumniRepository).save(any(Alumni.class));
    }
}
