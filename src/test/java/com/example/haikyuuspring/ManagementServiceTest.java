package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.ManagementRepository;
import com.example.haikyuuspring.services.ManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private ManagementRepository managementRepository;

    @InjectMocks
    private ManagementService managementService;

    @Test
    @DisplayName("Create management (ADVISOR/MANAGER) (stub)")
    void createManagement_Success() {
        ManagementDTO dto = new ManagementDTO(null, "Mgmt A", 1L, "School", null, 45, ManagementRole.ADVISOR, null);
        when(characterRepository.existsByName("Mgmt A")).thenReturn(false);

        // In a full implementation, you'd call: managementService.createManagement(dto);
        // verify(managementRepository).save(any(Management.class));
    }
}
