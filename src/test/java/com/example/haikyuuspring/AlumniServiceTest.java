package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Alumni;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.repository.AlumniRepository;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.AlumniService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumniServiceTest {

    @Mock
    private AlumniRepository alumniRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private AlumniService alumniService;

    private Alumni mockAlumni;
    private AlumniDTO mockAlumniDTO;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        // Mock School
        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");

        // Mock Alumni entity
        mockAlumni = new Alumni();
        mockAlumni.setId(1L);
        mockAlumni.setName("Test Alumni");
        mockAlumni.setHeight(175.5);
        mockAlumni.setAge(25);
        mockAlumni.setRole(Role.ALUMNI);
        mockAlumni.setSchool(mockSchool);
        mockAlumni.setImgUrl("https://example.url/alumni.png");
        mockAlumni.setFormerPlayer(false);
        mockAlumni.setPosition(null);
        mockAlumni.setJerseyNumber(null);
        mockAlumni.setFormerCoach(false);
        mockAlumni.setCoachingStyle(CoachingStyle.NONCOACH);

        // Mock AlumniDTO
        mockAlumniDTO = new AlumniDTO(
                null,
                "Test Alumni",
                175.5,
                25,
                Role.ALUMNI,
                1L,
                "Karasuno High",
                "https://example.url/alumni.png",
                false,
                null,
                null,
                false,
                CoachingStyle.NONCOACH
        );
    }

    @Test
    @DisplayName("Create alumni - Success")
    void createAlumni_Success() {
        when(characterRepository.existsByName(mockAlumniDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockAlumniDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(alumniRepository.save(any(Alumni.class))).thenReturn(mockAlumni);

        AlumniDTO result = alumniService.createAlumni(mockAlumniDTO);

        assertNotNull(result);
        assertEquals(mockAlumniDTO.name(), result.name());
        verify(characterRepository).existsByName(mockAlumniDTO.name());
        verify(schoolRepository).findById(mockAlumniDTO.schoolId());
        verify(alumniRepository).save(any(Alumni.class));
    }

    @Test
    @DisplayName("Create alumni - Duplicate name throws exception")
    void createAlumni_DuplicateName() {
        when(characterRepository.existsByName(mockAlumniDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                alumniService.createAlumni(mockAlumniDTO)
        );

        verify(characterRepository).existsByName(mockAlumniDTO.name());
        verify(alumniRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create alumni - School not found")
    void createAlumni_SchoolNotFound() {
        when(characterRepository.existsByName(mockAlumniDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockAlumniDTO.schoolId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                alumniService.createAlumni(mockAlumniDTO)
        );

        verify(characterRepository).existsByName(mockAlumniDTO.name());
        verify(schoolRepository).findById(mockAlumniDTO.schoolId());
        verify(alumniRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create alumni - Former player success")
    void createAlumni_FormerPlayerSuccess() {
        AlumniDTO formerPlayerDTO = new AlumniDTO(
                null,
                "Former Player Alumni",
                180.0,
                23,
                Role.ALUMNI,
                1L,
                "Karasuno High",
                "https://example.url/player.png",
                true,
                Position.WING_SPIKER,
                5,
                false,
                CoachingStyle.NONCOACH
        );

        Alumni formerPlayerAlumni = new Alumni();
        formerPlayerAlumni.setId(2L);
        formerPlayerAlumni.setName("Former Player Alumni");
        formerPlayerAlumni.setFormerPlayer(true);
        formerPlayerAlumni.setPosition(Position.WING_SPIKER);
        formerPlayerAlumni.setJerseyNumber(5);
        formerPlayerAlumni.setSchool(mockSchool);

        when(characterRepository.existsByName(formerPlayerDTO.name())).thenReturn(false);
        when(schoolRepository.findById(formerPlayerDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(alumniRepository.save(any(Alumni.class))).thenReturn(formerPlayerAlumni);

        AlumniDTO result = alumniService.createAlumni(formerPlayerDTO);

        assertNotNull(result);
        assertEquals(formerPlayerDTO.name(), result.name());
        verify(characterRepository).existsByName(formerPlayerDTO.name());
        verify(alumniRepository).save(any(Alumni.class));
    }

    @Test
    @DisplayName("Create alumni - Former player without position throws exception")
    void createAlumni_FormerPlayerWithoutPosition() {
        AlumniDTO invalidDTO = new AlumniDTO(
                null,
                "Invalid Former Player",
                180.0,
                23,
                Role.ALUMNI,
                1L,
                "Karasuno High",
                "https://example.url/player.png",
                true,
                null, // Missing position
                5,
                false,
                CoachingStyle.NONCOACH
        );

        when(characterRepository.existsByName(invalidDTO.name())).thenReturn(false);
        when(schoolRepository.findById(invalidDTO.schoolId())).thenReturn(Optional.of(mockSchool));

        assertThrows(RuntimeException.class, () ->
                alumniService.createAlumni(invalidDTO)
        );

        verify(characterRepository).existsByName(invalidDTO.name());
        verify(alumniRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create alumni - Non-former player with position throws exception")
    void createAlumni_NonFormerPlayerWithPosition() {
        AlumniDTO invalidDTO = new AlumniDTO(
                null,
                "Invalid Non-Player",
                175.0,
                25,
                Role.ALUMNI,
                1L,
                "Karasuno High",
                "https://example.url/alumni.png",
                false,
                Position.SETTER, // Should not have position
                null,
                false,
                CoachingStyle.NONCOACH
        );

        when(characterRepository.existsByName(invalidDTO.name())).thenReturn(false);
        when(schoolRepository.findById(invalidDTO.schoolId())).thenReturn(Optional.of(mockSchool));

        assertThrows(IllegalArgumentException.class, () ->
                alumniService.createAlumni(invalidDTO)
        );

        verify(characterRepository).existsByName(invalidDTO.name());
        verify(alumniRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create alumni - Former coach success")
    void createAlumni_FormerCoachSuccess() {
        AlumniDTO formerCoachDTO = new AlumniDTO(
                null,
                "Former Coach Alumni",
                170.0,
                40,
                Role.ALUMNI,
                1L,
                "Karasuno High",
                "https://example.url/coach.png",
                false,
                null,
                null,
                true,
                CoachingStyle.ATTACK
        );

        Alumni formerCoachAlumni = new Alumni();
        formerCoachAlumni.setId(3L);
        formerCoachAlumni.setName("Former Coach Alumni");
        formerCoachAlumni.setFormerCoach(true);
        formerCoachAlumni.setCoachingStyle(CoachingStyle.DEFENSE);
        formerCoachAlumni.setSchool(mockSchool);

        when(characterRepository.existsByName(formerCoachDTO.name())).thenReturn(false);
        when(schoolRepository.findById(formerCoachDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(alumniRepository.save(any(Alumni.class))).thenReturn(formerCoachAlumni);

        AlumniDTO result = alumniService.createAlumni(formerCoachDTO);

        assertNotNull(result);
        assertEquals(formerCoachDTO.name(), result.name());
        verify(characterRepository).existsByName(formerCoachDTO.name());
        verify(alumniRepository).save(any(Alumni.class));
    }

    @Test
    @DisplayName("Create alumni - Without school ID success")
    void createAlumni_WithoutSchoolId() {
        AlumniDTO noSchoolDTO = new AlumniDTO(
                null,
                "No School Alumni",
                175.0,
                25,
                Role.ALUMNI,
                null, // No school
                null,
                "https://example.url/alumni.png",
                false,
                null,
                null,
                false,
                CoachingStyle.NONCOACH
        );

        Alumni noSchoolAlumni = new Alumni();
        noSchoolAlumni.setId(4L);
        noSchoolAlumni.setName("No School Alumni");

        when(characterRepository.existsByName(noSchoolDTO.name())).thenReturn(false);
        when(alumniRepository.save(any(Alumni.class))).thenReturn(noSchoolAlumni);

        AlumniDTO result = alumniService.createAlumni(noSchoolDTO);

        assertNotNull(result);
        assertEquals(noSchoolDTO.name(), result.name());
        verify(characterRepository).existsByName(noSchoolDTO.name());
        verify(schoolRepository, never()).findById(anyLong());
        verify(alumniRepository).save(any(Alumni.class));
    }

    @Test
    @DisplayName("Find all alumni - Success")
    void findAllAlumni_Success() {
        List<Alumni> alumniList = List.of(mockAlumni);

        when(alumniRepository.findAll()).thenReturn(alumniList);

        List<AlumniDTO> result = alumniService.findAllAlumni();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockAlumni.getName(), result.getFirst().name());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Find all alumni - Empty list")
    void findAllAlumni_EmptyList() {
        when(alumniRepository.findAll()).thenReturn(new ArrayList<>());

        List<AlumniDTO> result = alumniService.findAllAlumni();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Find all former players - Success")
    void findAllFormerPlayers_Success() {
        Alumni formerPlayer = new Alumni();
        formerPlayer.setId(2L);
        formerPlayer.setName("Former Player");
        formerPlayer.setFormerPlayer(true);
        formerPlayer.setPosition(Position.SETTER);
        formerPlayer.setSchool(mockSchool);

        List<Alumni> alumniList = List.of(mockAlumni, formerPlayer);

        when(alumniRepository.findAll()).thenReturn(alumniList);

        List<AlumniDTO> result = alumniService.findAllFormerPlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.getFirst().formerPlayer());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Find all former players - None found")
    void findAllFormerPlayers_NoneFound() {
        List<Alumni> alumniList = List.of(mockAlumni);

        when(alumniRepository.findAll()).thenReturn(alumniList);

        List<AlumniDTO> result = alumniService.findAllFormerPlayers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Find alumni by school ID - Success")
    void findAlumniBySchoolId_Success() {
        Long schoolId = 1L;
        List<Alumni> alumniList = List.of(mockAlumni);

        when(alumniRepository.findAll()).thenReturn(alumniList);

        List<AlumniDTO> result = alumniService.findAlumniBySchoolId(schoolId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(schoolId, result.getFirst().schoolId());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Find alumni by school ID - None found")
    void findAlumniBySchoolId_NoneFound() {
        Long schoolId = 999L;
        List<Alumni> alumniList = List.of(mockAlumni);

        when(alumniRepository.findAll()).thenReturn(alumniList);

        List<AlumniDTO> result = alumniService.findAlumniBySchoolId(schoolId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumniRepository).findAll();
    }

    @Test
    @DisplayName("Map alumni list to DTO - Success")
    void mapAlumniListToDTO_Success() {
        Alumni alumni1 = new Alumni();
        alumni1.setId(1L);
        alumni1.setName("Alumni 1");
        alumni1.setSchool(mockSchool);
        alumni1.setFormerPlayer(false);
        alumni1.setFormerCoach(false);

        Alumni alumni2 = new Alumni();
        alumni2.setId(2L);
        alumni2.setName("Alumni 2");
        alumni2.setSchool(mockSchool);
        alumni2.setFormerPlayer(true);
        alumni2.setPosition(Position.LIBERO);
        alumni2.setFormerCoach(false);

        List<Alumni> alumniList = List.of(alumni1, alumni2);

        List<AlumniDTO> result = alumniService.mapAlumniListToDTO(alumniList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alumni 1", result.get(0).name());
        assertEquals("Alumni 2", result.get(1).name());
    }

    @Test
    @DisplayName("Map alumni list to DTO - Empty list")
    void mapAlumniListToDTO_EmptyList() {
        List<Alumni> emptyList = new ArrayList<>();

        List<AlumniDTO> result = alumniService.mapAlumniListToDTO(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert alumni to DTO - With all fields")
    void convertAlumniToDTO_WithAllFields() {
        Alumni completeAlumni = getAlumni();

        List<Alumni> alumniList = List.of(completeAlumni);

        List<AlumniDTO> result = alumniService.mapAlumniListToDTO(alumniList);

        assertNotNull(result);
        assertEquals(1, result.size());
        AlumniDTO dto = result.getFirst();
        assertEquals(5L, dto.id());
        assertEquals("Complete Alumni", dto.name());
        assertEquals(182.5, dto.height());
        assertEquals(28, dto.age());
        assertEquals(Role.ALUMNI, dto.role());
        assertEquals(1L, dto.schoolId());
        assertEquals("Karasuno High", dto.schoolName());
        assertEquals("https://example.url/complete.png", dto.imgUrl());
        assertTrue(dto.formerPlayer());
        assertEquals(Position.WING_SPIKER, dto.position());
        assertEquals(4, dto.jerseyNumber());
        assertTrue(dto.formerCoach());
        assertEquals(CoachingStyle.BLOCK, dto.coachingStyle());
    }

    private Alumni getAlumni() {
        Alumni completeAlumni = new Alumni();
        completeAlumni.setId(5L);
        completeAlumni.setName("Complete Alumni");
        completeAlumni.setHeight(182.5);
        completeAlumni.setAge(28);
        completeAlumni.setRole(Role.ALUMNI);
        completeAlumni.setSchool(mockSchool);
        completeAlumni.setImgUrl("https://example.url/complete.png");
        completeAlumni.setFormerPlayer(true);
        completeAlumni.setPosition(Position.WING_SPIKER);
        completeAlumni.setJerseyNumber(4);
        completeAlumni.setFormerCoach(true);
        completeAlumni.setCoachingStyle(CoachingStyle.BLOCK);
        return completeAlumni;
    }

    @Test
    @DisplayName("Convert alumni to DTO - Without school")
    void convertAlumniToDTO_WithoutSchool() {
        Alumni alumniWithoutSchool = new Alumni();
        alumniWithoutSchool.setId(6L);
        alumniWithoutSchool.setName("No School Alumni");
        alumniWithoutSchool.setSchool(null);
        alumniWithoutSchool.setFormerPlayer(false);
        alumniWithoutSchool.setFormerCoach(false);

        List<Alumni> alumniList = List.of(alumniWithoutSchool);

        List<AlumniDTO> result = alumniService.mapAlumniListToDTO(alumniList);

        assertNotNull(result);
        assertEquals(1, result.size());
        AlumniDTO dto = result.getFirst();
        assertNull(dto.schoolId());
        assertNull(dto.schoolName());
    }
}