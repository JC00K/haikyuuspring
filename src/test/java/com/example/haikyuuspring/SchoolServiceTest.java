package com.example.haikyuuspring;

import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.SchoolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolService schoolService;

    @Test
    @DisplayName("Should successfully delete school and rely on cascade for roster")
    void deleteSchool_Success() {
        Long schoolId = 1L;
        School school = new School();
        school.setId(schoolId);

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));

        schoolService.deleteSchool(schoolId);

        verify(schoolRepository, times(1)).delete(school);
    }
}