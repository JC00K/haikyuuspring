package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.FanDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Fan;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.FanRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.FanService;
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
class FanServiceTest {

    @Mock
    private FanRepository fanRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private FanService fanService;

    private Fan mockFan;
    private FanDTO mockFanDTO;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        // Mock School
        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");

        // Mock Fan entity
        mockFan = new Fan();
        mockFan.setId(1L);
        mockFan.setName("Test Fan");
        mockFan.setHeight(165.5);
        mockFan.setAge(16);
        mockFan.setRole(Role.FAN);
        mockFan.setSchool(mockSchool);
        mockFan.setImgUrl("https://example.url/fan.png");

        // Mock FanDTO
        mockFanDTO = new FanDTO(
                null,
                "Test Fan",
                165.5,
                16,
                Role.FAN,
                1L,
                "Karasuno High",
                "https://example.url/fan.png"
        );
    }

    @Test
    @DisplayName("Create fan - Success")
    void createFan_Success() {
        when(characterRepository.existsByName(mockFanDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockFanDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(fanRepository.save(any(Fan.class))).thenReturn(mockFan);

        FanDTO result = fanService.createFan(mockFanDTO);

        assertNotNull(result);
        assertEquals(mockFanDTO.name(), result.name());
        assertEquals(mockFanDTO.age(), result.age());
        assertEquals(mockFanDTO.height(), result.height());
        verify(characterRepository).existsByName(mockFanDTO.name());
        verify(schoolRepository).findById(mockFanDTO.schoolId());
        verify(fanRepository).save(any(Fan.class));
    }

    @Test
    @DisplayName("Create fan - Duplicate name throws exception")
    void createFan_DuplicateName() {
        when(characterRepository.existsByName(mockFanDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                fanService.createFan(mockFanDTO)
        );

        verify(characterRepository).existsByName(mockFanDTO.name());
        verify(fanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create fan - School not found")
    void createFan_SchoolNotFound() {
        when(characterRepository.existsByName(mockFanDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockFanDTO.schoolId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                fanService.createFan(mockFanDTO)
        );

        verify(characterRepository).existsByName(mockFanDTO.name());
        verify(schoolRepository).findById(mockFanDTO.schoolId());
        verify(fanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create fan - Without school ID success")
    void createFan_WithoutSchoolId() {
        FanDTO noSchoolDTO = new FanDTO(
                null,
                "Independent Fan",
                170.0,
                18,
                Role.FAN,
                null, // No school
                null,
                "https://example.url/fan.png"
        );

        Fan noSchoolFan = new Fan();
        noSchoolFan.setId(2L);
        noSchoolFan.setName("Independent Fan");
        noSchoolFan.setRole(Role.FAN);

        when(characterRepository.existsByName(noSchoolDTO.name())).thenReturn(false);
        when(fanRepository.save(any(Fan.class))).thenReturn(noSchoolFan);

        FanDTO result = fanService.createFan(noSchoolDTO);

        assertNotNull(result);
        assertEquals(noSchoolDTO.name(), result.name());
        verify(characterRepository).existsByName(noSchoolDTO.name());
        verify(schoolRepository, never()).findById(anyLong());
        verify(fanRepository).save(any(Fan.class));
    }

    @Test
    @DisplayName("Create fan - With all fields")
    void createFan_WithAllFields() {
        FanDTO completeDTO = new FanDTO(
                null,
                "Complete Fan",
                172.0,
                17,
                Role.FAN,
                1L,
                "Karasuno High",
                "https://example.url/complete.png"
        );

        Fan completeFan = new Fan();
        completeFan.setId(3L);
        completeFan.setName("Complete Fan");
        completeFan.setHeight(172.0);
        completeFan.setAge(17);
        completeFan.setRole(Role.FAN);
        completeFan.setSchool(mockSchool);
        completeFan.setImgUrl("https://example.url/complete.png");

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(fanRepository.save(any(Fan.class))).thenReturn(completeFan);

        FanDTO result = fanService.createFan(completeDTO);

        assertNotNull(result);
        assertEquals(completeDTO.name(), result.name());
        assertEquals(completeDTO.height(), result.height());
        assertEquals(completeDTO.age(), result.age());
        assertEquals(completeDTO.role(), result.role());
        assertEquals(completeDTO.imgUrl(), result.imgUrl());
        verify(characterRepository).existsByName(completeDTO.name());
        verify(fanRepository).save(any(Fan.class));
    }

    @Test
    @DisplayName("Find all fans - Success")
    void findAllFans_Success() {
        List<Fan> fanList = List.of(mockFan);

        when(fanRepository.findAll()).thenReturn(fanList);

        List<FanDTO> result = fanService.findAllFans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockFan.getName(), result.getFirst().name());
        verify(fanRepository).findAll();
    }

    @Test
    @DisplayName("Find all fans - Empty list")
    void findAllFans_EmptyList() {
        when(fanRepository.findAll()).thenReturn(new ArrayList<>());

        List<FanDTO> result = fanService.findAllFans();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fanRepository).findAll();
    }

    @Test
    @DisplayName("Find all fans - Multiple fans")
    void findAllFans_MultipleFans() {
        Fan fan1 = new Fan();
        fan1.setId(1L);
        fan1.setName("Fan 1");
        fan1.setSchool(mockSchool);

        Fan fan2 = new Fan();
        fan2.setId(2L);
        fan2.setName("Fan 2");
        fan2.setSchool(mockSchool);

        Fan fan3 = new Fan();
        fan3.setId(3L);
        fan3.setName("Fan 3");
        fan3.setSchool(mockSchool);

        List<Fan> fanList = List.of(fan1, fan2, fan3);

        when(fanRepository.findAll()).thenReturn(fanList);

        List<FanDTO> result = fanService.findAllFans();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Fan 1", result.get(0).name());
        assertEquals("Fan 2", result.get(1).name());
        assertEquals("Fan 3", result.get(2).name());
        verify(fanRepository).findAll();
    }

    @Test
    @DisplayName("Map fan list to DTO - Success")
    void mapFanListToDTO_Success() {
        Fan fan1 = new Fan();
        fan1.setId(1L);
        fan1.setName("Fan 1");
        fan1.setSchool(mockSchool);

        Fan fan2 = new Fan();
        fan2.setId(2L);
        fan2.setName("Fan 2");
        fan2.setSchool(mockSchool);

        List<Fan> fanList = List.of(fan1, fan2);

        List<FanDTO> result = fanService.mapFanListToDTO(fanList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Fan 1", result.get(0).name());
        assertEquals("Fan 2", result.get(1).name());
    }

    @Test
    @DisplayName("Map fan list to DTO - Empty list")
    void mapFanListToDTO_EmptyList() {
        List<Fan> emptyList = new ArrayList<>();

        List<FanDTO> result = fanService.mapFanListToDTO(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert fan to DTO - With all fields")
    void convertFanToDTO_WithAllFields() {
        Fan completeFan = new Fan();
        completeFan.setId(5L);
        completeFan.setName("Complete Fan");
        completeFan.setHeight(168.5);
        completeFan.setAge(19);
        completeFan.setRole(Role.FAN);
        completeFan.setSchool(mockSchool);
        completeFan.setImgUrl("https://example.url/complete.png");

        List<Fan> fans = List.of(completeFan);

        List<FanDTO> result = fanService.mapFanListToDTO(fans);

        assertNotNull(result);
        assertEquals(1, result.size());
        FanDTO dto = result.getFirst();
        assertEquals(5L, dto.id());
        assertEquals("Complete Fan", dto.name());
        assertEquals(168.5, dto.height());
        assertEquals(19, dto.age());
        assertEquals(Role.FAN, dto.role());
        assertEquals(1L, dto.schoolId());
        assertEquals("Karasuno High", dto.schoolName());
        assertEquals("https://example.url/complete.png", dto.imgUrl());
    }

    @Test
    @DisplayName("Convert fan to DTO - Without school")
    void convertFanToDTO_WithoutSchool() {
        Fan fanWithoutSchool = new Fan();
        fanWithoutSchool.setId(6L);
        fanWithoutSchool.setName("No School Fan");
        fanWithoutSchool.setSchool(null);
        fanWithoutSchool.setRole(Role.FAN);

        List<Fan> fans = List.of(fanWithoutSchool);

        List<FanDTO> result = fanService.mapFanListToDTO(fans);

        assertNotNull(result);
        assertEquals(1, result.size());
        FanDTO dto = result.getFirst();
        assertEquals(6L, dto.id());
        assertEquals("No School Fan", dto.name());
        assertNull(dto.schoolId());
        assertNull(dto.schoolName());
        assertEquals(Role.FAN, dto.role());
    }

    @Test
    @DisplayName("Create fan - Minimal fields")
    void createFan_MinimalFields() {
        FanDTO minimalDTO = new FanDTO(
                null,
                "Minimal Fan",
                null, // No height
                null, // No age
                Role.FAN,
                null, // No school
                null,
                null  // No image
        );

        Fan minimalFan = new Fan();
        minimalFan.setId(7L);
        minimalFan.setName("Minimal Fan");
        minimalFan.setRole(Role.FAN);

        when(characterRepository.existsByName(minimalDTO.name())).thenReturn(false);
        when(fanRepository.save(any(Fan.class))).thenReturn(minimalFan);

        FanDTO result = fanService.createFan(minimalDTO);

        assertNotNull(result);
        assertEquals(minimalDTO.name(), result.name());
        assertEquals(Role.FAN, result.role());
        verify(characterRepository).existsByName(minimalDTO.name());
        verify(fanRepository).save(any(Fan.class));
    }

    @Test
    @DisplayName("Create fan - Different ages")
    void createFan_DifferentAges() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test young fan
        FanDTO youngFanDTO = new FanDTO(null, "Young Fan", 160.0, 14, Role.FAN,
                1L, "Karasuno High", "url");
        Fan youngFan = new Fan();
        youngFan.setName("Young Fan");
        youngFan.setAge(14);
        youngFan.setSchool(mockSchool);
        when(fanRepository.save(any(Fan.class))).thenReturn(youngFan);
        FanDTO result1 = fanService.createFan(youngFanDTO);
        assertEquals(14, result1.age());

        // Test older fan
        FanDTO olderFanDTO = new FanDTO(null, "Older Fan", 170.0, 25, Role.FAN,
                1L, "Karasuno High", "url");
        Fan olderFan = new Fan();
        olderFan.setName("Older Fan");
        olderFan.setAge(25);
        olderFan.setSchool(mockSchool);
        when(fanRepository.save(any(Fan.class))).thenReturn(olderFan);
        FanDTO result2 = fanService.createFan(olderFanDTO);
        assertEquals(25, result2.age());

        verify(fanRepository, times(2)).save(any(Fan.class));
    }

    @Test
    @DisplayName("Create fan - Different heights")
    void createFan_DifferentHeights() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test short fan
        FanDTO shortFanDTO = new FanDTO(null, "Short Fan", 155.0, 16, Role.FAN,
                1L, "Karasuno High", "url");
        Fan shortFan = new Fan();
        shortFan.setName("Short Fan");
        shortFan.setHeight(155.0);
        shortFan.setSchool(mockSchool);
        when(fanRepository.save(any(Fan.class))).thenReturn(shortFan);
        FanDTO result1 = fanService.createFan(shortFanDTO);
        assertEquals(155.0, result1.height());

        // Test tall fan
        FanDTO tallFanDTO = new FanDTO(null, "Tall Fan", 180.0, 16, Role.FAN,
                1L, "Karasuno High", "url");
        Fan tallFan = new Fan();
        tallFan.setName("Tall Fan");
        tallFan.setHeight(180.0);
        tallFan.setSchool(mockSchool);
        when(fanRepository.save(any(Fan.class))).thenReturn(tallFan);
        FanDTO result2 = fanService.createFan(tallFanDTO);
        assertEquals(180.0, result2.height());

        verify(fanRepository, times(2)).save(any(Fan.class));
    }

    @Test
    @DisplayName("Find all fans - Fans from different schools")
    void findAllFans_DifferentSchools() {
        School nekoma = new School();
        nekoma.setId(2L);
        nekoma.setName("Nekoma High");

        Fan karasunoFan = new Fan();
        karasunoFan.setId(1L);
        karasunoFan.setName("Karasuno Fan");
        karasunoFan.setSchool(mockSchool);

        Fan nekomaFan = new Fan();
        nekomaFan.setId(2L);
        nekomaFan.setName("Nekoma Fan");
        nekomaFan.setSchool(nekoma);

        Fan independentFan = new Fan();
        independentFan.setId(3L);
        independentFan.setName("Independent Fan");
        independentFan.setSchool(null);

        List<Fan> fanList = List.of(karasunoFan, nekomaFan, independentFan);

        when(fanRepository.findAll()).thenReturn(fanList);

        List<FanDTO> result = fanService.findAllFans();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Karasuno High", result.get(0).schoolName());
        assertEquals("Nekoma High", result.get(1).schoolName());
        assertNull(result.get(2).schoolName());
        verify(fanRepository).findAll();
    }

    @Test
    @DisplayName("Create fan - Verify all fields are set correctly")
    void createFan_VerifyAllFieldsSet() {
        FanDTO completeDTO = new FanDTO(
                null,
                "Verified Fan",
                175.5,
                20,
                Role.FAN,
                1L,
                "Karasuno High",
                "https://example.url/verified.png"
        );

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(fanRepository.save(any(Fan.class))).thenAnswer(invocation -> {
            Fan savedFan = invocation.getArgument(0);
            // Verify all fields are set correctly
            assertEquals("Verified Fan", savedFan.getName());
            assertEquals(175.5, savedFan.getHeight());
            assertEquals(20, savedFan.getAge());
            assertEquals(Role.FAN, savedFan.getRole());
            assertEquals("https://example.url/verified.png", savedFan.getImgUrl());
            assertEquals(mockSchool, savedFan.getSchool());
            return savedFan;
        });

        FanDTO result = fanService.createFan(completeDTO);

        assertNotNull(result);
        verify(fanRepository).save(any(Fan.class));
    }
}