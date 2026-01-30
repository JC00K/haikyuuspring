package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.CoachDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Coach;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.CoachRole;
import com.example.haikyuuspring.model.enums.CoachingStyle;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.CoachRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.CoachService;
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
class CoachServiceTest {

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private CoachService coachService;

    private Coach mockCoach;
    private CoachDTO mockCoachDTO;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        // Mock School
        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");

        // Mock Coach entity
        mockCoach = new Coach();
        mockCoach.setId(1L);
        mockCoach.setName("Keishin Ukai");
        mockCoach.setHeight(178.2);
        mockCoach.setAge(26);
        mockCoach.setRole(Role.COACH);
        mockCoach.setSchool(mockSchool);
        mockCoach.setImgUrl("https://example.url/ukai.png");
        mockCoach.setIsRetired(false);
        mockCoach.setCoachRole(CoachRole.HEAD);
        mockCoach.setCoachingStyle(CoachingStyle.DEFENSE);

        // Mock CoachDTO
        mockCoachDTO = new CoachDTO(
                null,
                "Keishin Ukai",
                178.2,
                26,
                Role.COACH,
                1L,
                "Karasuno High",
                "https://example.url/ukai.png",
                false,
                CoachRole.HEAD,
                CoachingStyle.DEFENSE
        );
    }

    @Test
    @DisplayName("Create coach - Success")
    void createCoach_Success() {
        when(characterRepository.existsByName(mockCoachDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockCoachDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(coachRepository.save(any(Coach.class))).thenReturn(mockCoach);

        CoachDTO result = coachService.createCoach(mockCoachDTO);

        assertNotNull(result);
        assertEquals(mockCoachDTO.name(), result.name());
        assertEquals(mockCoachDTO.coachRole(), result.coachRole());
        assertEquals(mockCoachDTO.coachingStyle(), result.coachingStyle());
        verify(characterRepository).existsByName(mockCoachDTO.name());
        verify(schoolRepository).findById(mockCoachDTO.schoolId());
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - Duplicate name throws exception")
    void createCoach_DuplicateName() {
        when(characterRepository.existsByName(mockCoachDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                coachService.createCoach(mockCoachDTO)
        );

        verify(characterRepository).existsByName(mockCoachDTO.name());
        verify(coachRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create coach - School not found")
    void createCoach_SchoolNotFound() {
        when(characterRepository.existsByName(mockCoachDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockCoachDTO.schoolId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                coachService.createCoach(mockCoachDTO)
        );

        verify(characterRepository).existsByName(mockCoachDTO.name());
        verify(schoolRepository).findById(mockCoachDTO.schoolId());
        verify(coachRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create coach - Without school ID success")
    void createCoach_WithoutSchoolId() {
        CoachDTO noSchoolDTO = new CoachDTO(
                null,
                "Independent Coach",
                175.0,
                30,
                Role.COACH,
                null, // No school
                null,
                "https://example.url/coach.png",
                false,
                CoachRole.HEAD,
                CoachingStyle.ATTACK
        );

        Coach noSchoolCoach = new Coach();
        noSchoolCoach.setId(2L);
        noSchoolCoach.setName("Independent Coach");
        noSchoolCoach.setCoachRole(CoachRole.HEAD);
        noSchoolCoach.setCoachingStyle(CoachingStyle.DEFENSE);

        when(characterRepository.existsByName(noSchoolDTO.name())).thenReturn(false);
        when(coachRepository.save(any(Coach.class))).thenReturn(noSchoolCoach);

        CoachDTO result = coachService.createCoach(noSchoolDTO);

        assertNotNull(result);
        assertEquals(noSchoolDTO.name(), result.name());
        verify(characterRepository).existsByName(noSchoolDTO.name());
        verify(schoolRepository, never()).findById(anyLong());
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - Assistant coach with coaching style")
    void createCoach_AssistantCoachWithCoachingStyle() {
        CoachDTO assistantDTO = new CoachDTO(
                null,
                "Assistant Coach",
                172.0,
                28,
                Role.COACH,
                1L,
                "Karasuno High",
                "https://example.url/assistant.png",
                false,
                CoachRole.ASSISTANT,
                CoachingStyle.DEFENSE
        );

        Coach assistantCoach = new Coach();
        assistantCoach.setId(3L);
        assistantCoach.setName("Assistant Coach");
        assistantCoach.setCoachRole(CoachRole.ASSISTANT);
        assistantCoach.setCoachingStyle(CoachingStyle.ATTACK);
        assistantCoach.setSchool(mockSchool);

        when(characterRepository.existsByName(assistantDTO.name())).thenReturn(false);
        when(schoolRepository.findById(assistantDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(coachRepository.save(any(Coach.class))).thenReturn(assistantCoach);

        CoachDTO result = coachService.createCoach(assistantDTO);

        assertNotNull(result);
        assertEquals(assistantDTO.name(), result.name());
        assertEquals(CoachRole.ASSISTANT, result.coachRole());
        assertEquals(CoachingStyle.BLOCK, result.coachingStyle());
        verify(characterRepository).existsByName(assistantDTO.name());
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - Head coach with coaching style")
    void createCoach_HeadCoachWithCoachingStyle() {
        CoachDTO headCoachDTO = new CoachDTO(
                null,
                "Head Coach",
                180.0,
                35,
                Role.COACH,
                1L,
                "Karasuno High",
                "https://example.url/head.png",
                false,
                CoachRole.HEAD,
                CoachingStyle.ATTACK
        );

        Coach headCoach = new Coach();
        headCoach.setId(4L);
        headCoach.setName("Head Coach");
        headCoach.setCoachRole(CoachRole.HEAD);
        headCoach.setCoachingStyle(CoachingStyle.ATTACK);
        headCoach.setSchool(mockSchool);

        when(characterRepository.existsByName(headCoachDTO.name())).thenReturn(false);
        when(schoolRepository.findById(headCoachDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(coachRepository.save(any(Coach.class))).thenReturn(headCoach);

        CoachDTO result = coachService.createCoach(headCoachDTO);

        assertNotNull(result);
        assertEquals(headCoachDTO.name(), result.name());
        assertEquals(CoachRole.HEAD, result.coachRole());
        assertEquals(CoachingStyle.ATTACK, result.coachingStyle());
        verify(characterRepository).existsByName(headCoachDTO.name());
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - Retired coach")
    void createCoach_RetiredCoach() {
        CoachDTO retiredDTO = new CoachDTO(
                null,
                "Retired Coach",
                175.0,
                55,
                Role.COACH,
                null,
                null,
                "https://example.url/retired.png",
                true,
                CoachRole.HEAD,
                CoachingStyle.ATTACK
        );

        Coach retiredCoach = new Coach();
        retiredCoach.setId(5L);
        retiredCoach.setName("Retired Coach");
        retiredCoach.setIsRetired(true);
        retiredCoach.setCoachRole(CoachRole.HEAD);
        retiredCoach.setCoachingStyle(CoachingStyle.ATTACK);

        when(characterRepository.existsByName(retiredDTO.name())).thenReturn(false);
        when(coachRepository.save(any(Coach.class))).thenReturn(retiredCoach);

        CoachDTO result = coachService.createCoach(retiredDTO);

        assertNotNull(result);
        assertEquals(retiredDTO.name(), result.name());
        assertTrue(result.isRetired());
        verify(characterRepository).existsByName(retiredDTO.name());
        verify(coachRepository).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - With all fields")
    void createCoach_WithAllFields() {
        CoachDTO completeDTO = new CoachDTO(
                null,
                "Complete Coach",
                182.5,
                32,
                Role.COACH,
                1L,
                "Karasuno High",
                "https://example.url/complete.png",
                false,
                CoachRole.HEAD,
                CoachingStyle.BLOCK
        );

        Coach completeCoach = getCoach();

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(coachRepository.save(any(Coach.class))).thenReturn(completeCoach);

        CoachDTO result = coachService.createCoach(completeDTO);

        assertNotNull(result);
        assertEquals(completeDTO.name(), result.name());
        assertEquals(completeDTO.height(), result.height());
        assertEquals(completeDTO.age(), result.age());
        assertEquals(completeDTO.role(), result.role());
        assertEquals(completeDTO.coachRole(), result.coachRole());
        assertEquals(completeDTO.coachingStyle(), result.coachingStyle());
        assertFalse(result.isRetired());
        verify(characterRepository).existsByName(completeDTO.name());
        verify(coachRepository).save(any(Coach.class));
    }

    private Coach getCoach() {
        Coach completeCoach = new Coach();
        completeCoach.setId(6L);
        completeCoach.setName("Complete Coach");
        completeCoach.setHeight(182.5);
        completeCoach.setAge(32);
        completeCoach.setRole(Role.COACH);
        completeCoach.setSchool(mockSchool);
        completeCoach.setImgUrl("https://example.url/complete.png");
        completeCoach.setIsRetired(false);
        completeCoach.setCoachRole(CoachRole.HEAD);
        completeCoach.setCoachingStyle(CoachingStyle.DEFENSE);
        return completeCoach;
    }

    @Test
    @DisplayName("Find all coaches - Success")
    void findAllCoaches_Success() {
        List<Coach> coachList = List.of(mockCoach);

        when(coachRepository.findAll()).thenReturn(coachList);

        List<CoachDTO> result = coachService.findALlCoaches();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockCoach.getName(), result.getFirst().name());
        verify(coachRepository).findAll();
    }

    @Test
    @DisplayName("Find all coaches - Empty list")
    void findAllCoaches_EmptyList() {
        when(coachRepository.findAll()).thenReturn(new ArrayList<>());

        List<CoachDTO> result = coachService.findALlCoaches();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(coachRepository).findAll();
    }

    @Test
    @DisplayName("Find all coaches - Multiple coaches")
    void findAllCoaches_MultipleCoaches() {
        Coach coach1 = new Coach();
        coach1.setId(1L);
        coach1.setName("Coach 1");
        coach1.setCoachRole(CoachRole.HEAD);
        coach1.setSchool(mockSchool);

        Coach coach2 = new Coach();
        coach2.setId(2L);
        coach2.setName("Coach 2");
        coach2.setCoachRole(CoachRole.ASSISTANT);
        coach2.setSchool(mockSchool);

        Coach coach3 = new Coach();
        coach3.setId(3L);
        coach3.setName("Coach 3");
        coach3.setCoachRole(CoachRole.HEAD);
        coach3.setSchool(mockSchool);

        List<Coach> coachList = List.of(coach1, coach2, coach3);

        when(coachRepository.findAll()).thenReturn(coachList);

        List<CoachDTO> result = coachService.findALlCoaches();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Coach 1", result.get(0).name());
        assertEquals("Coach 2", result.get(1).name());
        assertEquals("Coach 3", result.get(2).name());
        verify(coachRepository).findAll();
    }

    @Test
    @DisplayName("Map coach list to DTO - Success")
    void mapCoachListToDTO_Success() {
        Coach coach1 = new Coach();
        coach1.setId(1L);
        coach1.setName("Coach 1");
        coach1.setCoachRole(CoachRole.HEAD);
        coach1.setSchool(mockSchool);

        Coach coach2 = new Coach();
        coach2.setId(2L);
        coach2.setName("Coach 2");
        coach2.setCoachRole(CoachRole.ASSISTANT);
        coach2.setSchool(mockSchool);

        List<Coach> coachList = List.of(coach1, coach2);

        List<CoachDTO> result = coachService.mapCoachListToDTO(coachList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Coach 1", result.get(0).name());
        assertEquals("Coach 2", result.get(1).name());
    }

    @Test
    @DisplayName("Map coach list to DTO - Empty list")
    void mapCoachListToDTO_EmptyList() {
        List<Coach> emptyList = new ArrayList<>();

        List<CoachDTO> result = coachService.mapCoachListToDTO(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert coach to DTO - With all fields")
    void convertCoachToDTO_WithAllFields() {
        Coach completeCoach = getCompleteCoach();

        List<Coach> coaches = List.of(completeCoach);

        List<CoachDTO> result = coachService.mapCoachListToDTO(coaches);

        assertNotNull(result);
        assertEquals(1, result.size());
        CoachDTO dto = result.getFirst();
        assertEquals(7L, dto.id());
        assertEquals("Complete Coach", dto.name());
        assertEquals(180.0, dto.height());
        assertEquals(40, dto.age());
        assertEquals(Role.COACH, dto.role());
        assertEquals(1L, dto.schoolId());
        assertEquals("Karasuno High", dto.schoolName());
        assertEquals("https://example.url/complete.png", dto.imgUrl());
        assertFalse(dto.isRetired());
        assertEquals(CoachRole.HEAD, dto.coachRole());
        assertEquals(CoachingStyle.ATTACK, dto.coachingStyle());
    }

    private Coach getCompleteCoach() {
        Coach completeCoach = new Coach();
        completeCoach.setId(7L);
        completeCoach.setName("Complete Coach");
        completeCoach.setHeight(180.0);
        completeCoach.setAge(40);
        completeCoach.setRole(Role.COACH);
        completeCoach.setSchool(mockSchool);
        completeCoach.setImgUrl("https://example.url/complete.png");
        completeCoach.setIsRetired(false);
        completeCoach.setCoachRole(CoachRole.HEAD);
        completeCoach.setCoachingStyle(CoachingStyle.ATTACK);
        return completeCoach;
    }

    @Test
    @DisplayName("Convert coach to DTO - Without school")
    void convertCoachToDTO_WithoutSchool() {
        Coach coachWithoutSchool = new Coach();
        coachWithoutSchool.setId(8L);
        coachWithoutSchool.setName("No School Coach");
        coachWithoutSchool.setSchool(null);
        coachWithoutSchool.setCoachRole(CoachRole.HEAD);
        coachWithoutSchool.setCoachingStyle(CoachingStyle.BLOCK);

        List<Coach> coaches = List.of(coachWithoutSchool);

        List<CoachDTO> result = coachService.mapCoachListToDTO(coaches);

        assertNotNull(result);
        assertEquals(1, result.size());
        CoachDTO dto = result.getFirst();
        assertEquals(8L, dto.id());
        assertEquals("No School Coach", dto.name());
        assertNull(dto.schoolId());
        assertNull(dto.schoolName());
    }

    @Test
    @DisplayName("Convert coach to DTO - Assistant coach with coaching style")
    void convertCoachToDTO_AssistantCoachWithStyle() {
        Coach assistantCoach = new Coach();
        assistantCoach.setId(9L);
        assistantCoach.setName("Assistant Coach");
        assistantCoach.setSchool(mockSchool);
        assistantCoach.setCoachRole(CoachRole.ASSISTANT);
        assistantCoach.setCoachingStyle(CoachingStyle.ATTACK);
        assistantCoach.setIsRetired(false);

        List<Coach> coaches = List.of(assistantCoach);

        List<CoachDTO> result = coachService.mapCoachListToDTO(coaches);

        assertNotNull(result);
        assertEquals(1, result.size());
        CoachDTO dto = result.getFirst();
        assertEquals(CoachRole.ASSISTANT, dto.coachRole());
        assertEquals(CoachingStyle.BLOCK, dto.coachingStyle());
    }

    @Test
    @DisplayName("Create coach - All coaching styles")
    void createCoach_AllCoachingStyles() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test SUPPORTIVE
        CoachDTO supportiveDTO = new CoachDTO(null, "Supportive Coach", 175.0, 30, Role.COACH,
                1L, "Karasuno High", "url", false, CoachRole.HEAD, CoachingStyle.DEFENSE);
        Coach supportiveCoach = new Coach();
        supportiveCoach.setName("Supportive Coach");
        supportiveCoach.setCoachingStyle(CoachingStyle.DEFENSE);
        supportiveCoach.setSchool(mockSchool);
        when(coachRepository.save(any(Coach.class))).thenReturn(supportiveCoach);
        CoachDTO result1 = coachService.createCoach(supportiveDTO);
        assertEquals(CoachingStyle.DEFENSE, result1.coachingStyle());

        // Test DEMANDING
        CoachDTO demandingDTO = new CoachDTO(null, "Demanding Coach", 175.0, 30, Role.COACH,
                1L, "Karasuno High", "url", false, CoachRole.HEAD, CoachingStyle.ATTACK);
        Coach demandingCoach = new Coach();
        demandingCoach.setName("Demanding Coach");
        demandingCoach.setCoachingStyle(CoachingStyle.ATTACK);
        demandingCoach.setSchool(mockSchool);
        when(coachRepository.save(any(Coach.class))).thenReturn(demandingCoach);
        CoachDTO result2 = coachService.createCoach(demandingDTO);
        assertEquals(CoachingStyle.ATTACK, result2.coachingStyle());

        verify(coachRepository, times(2)).save(any(Coach.class));
    }

    @Test
    @DisplayName("Create coach - All coach roles")
    void createCoach_AllCoachRoles() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test HEAD_COACH
        CoachDTO headCoachDTO = new CoachDTO(null, "Head Coach", 175.0, 30, Role.COACH,
                1L, "Karasuno High", "url", false, CoachRole.HEAD, CoachingStyle.DEFENSE);
        Coach headCoach = new Coach();
        headCoach.setName("Head Coach");
        headCoach.setCoachRole(CoachRole.HEAD);
        headCoach.setSchool(mockSchool);
        when(coachRepository.save(any(Coach.class))).thenReturn(headCoach);
        CoachDTO result1 = coachService.createCoach(headCoachDTO);
        assertEquals(CoachRole.HEAD, result1.coachRole());

        // Test ASSISTANT
        CoachDTO assistantDTO = new CoachDTO(null, "Assistant Coach", 175.0, 30, Role.COACH,
                1L, "Karasuno High", "url", false, CoachRole.ASSISTANT, null);
        Coach assistant = new Coach();
        assistant.setName("Assistant Coach");
        assistant.setCoachRole(CoachRole.ASSISTANT);
        assistant.setSchool(mockSchool);
        when(coachRepository.save(any(Coach.class))).thenReturn(assistant);
        CoachDTO result2 = coachService.createCoach(assistantDTO);
        assertEquals(CoachRole.ASSISTANT, result2.coachRole());

        verify(coachRepository, times(2)).save(any(Coach.class));
    }
}