package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Management;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.ManagementRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.ManagementService;
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
class ManagementServiceTest {

    @Mock
    private ManagementRepository managementRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private ManagementService managementService;

    private Management mockManagement;
    private ManagementDTO mockManagementDTO;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        // Mock School
        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");

        // Mock Management entity
        mockManagement = new Management();
        mockManagement.setId(1L);
        mockManagement.setName("Kiyoko Shimizu");
        mockManagement.setHeight(166.2);
        mockManagement.setAge(17);
        mockManagement.setYear(Year.THIRD);
        mockManagement.setRole(Role.MANAGEMENT);
        mockManagement.setSchool(mockSchool);
        mockManagement.setImgUrl("https://example.url/shimizu.png");
        mockManagement.setManagementRole(ManagementRole.MANAGER);

        // Mock ManagementDTO
        mockManagementDTO = new ManagementDTO(
                null,
                "Kiyoko Shimizu",
                166.2,
                17,
                Year.THIRD,
                Role.MANAGEMENT,
                1L,
                "Karasuno High",
                "https://example.url/shimizu.png",
                ManagementRole.MANAGER
        );
    }

    @Test
    @DisplayName("Create management - Success")
    void createManagement_Success() {
        when(characterRepository.existsByName(mockManagementDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockManagementDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(managementRepository.save(any(Management.class))).thenReturn(mockManagement);

        ManagementDTO result = managementService.createManagement(mockManagementDTO);

        assertNotNull(result);
        assertEquals(mockManagementDTO.name(), result.name());
        assertEquals(mockManagementDTO.managementRole(), result.managementRole());
        verify(characterRepository).existsByName(mockManagementDTO.name());
        verify(schoolRepository).findById(mockManagementDTO.schoolId());
        verify(managementRepository).save(any(Management.class));
    }

    @Test
    @DisplayName("Create management - Duplicate name throws exception")
    void createManagement_DuplicateName() {
        when(characterRepository.existsByName(mockManagementDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                managementService.createManagement(mockManagementDTO)
        );

        verify(characterRepository).existsByName(mockManagementDTO.name());
        verify(managementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create management - School not found")
    void createManagement_SchoolNotFound() {
        when(characterRepository.existsByName(mockManagementDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockManagementDTO.schoolId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                managementService.createManagement(mockManagementDTO)
        );

        verify(characterRepository).existsByName(mockManagementDTO.name());
        verify(schoolRepository).findById(mockManagementDTO.schoolId());
        verify(managementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create management - Without school ID success")
    void createManagement_WithoutSchoolId() {
        ManagementDTO noSchoolDTO = new ManagementDTO(
                null,
                "Independent Manager",
                160.0,
                20,
                Year.SECOND,
                Role.MANAGEMENT,
                null, // No school
                null,
                "https://example.url/manager.png",
                ManagementRole.MANAGER
        );

        Management noSchoolManagement = new Management();
        noSchoolManagement.setId(2L);
        noSchoolManagement.setName("Independent Manager");
        noSchoolManagement.setManagementRole(ManagementRole.MANAGER);

        when(characterRepository.existsByName(noSchoolDTO.name())).thenReturn(false);
        when(managementRepository.save(any(Management.class))).thenReturn(noSchoolManagement);

        ManagementDTO result = managementService.createManagement(noSchoolDTO);

        assertNotNull(result);
        assertEquals(noSchoolDTO.name(), result.name());
        verify(characterRepository).existsByName(noSchoolDTO.name());
        verify(schoolRepository, never()).findById(anyLong());
        verify(managementRepository).save(any(Management.class));
    }

    @Test
    @DisplayName("Create management - With all fields")
    void createManagement_WithAllFields() {
        ManagementDTO completeDTO = new ManagementDTO(
                null,
                "Complete Manager",
                165.5,
                18,
                Year.THIRD,
                Role.MANAGEMENT,
                1L,
                "Karasuno High",
                "https://example.url/complete.png",
                ManagementRole.ADVISOR
        );

        Management completeManagement = getCompleteManagement();

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(managementRepository.save(any(Management.class))).thenReturn(completeManagement);

        ManagementDTO result = managementService.createManagement(completeDTO);

        assertNotNull(result);
        assertEquals(completeDTO.name(), result.name());
        assertEquals(completeDTO.height(), result.height());
        assertEquals(completeDTO.age(), result.age());
        assertEquals(completeDTO.year(), result.year());
        assertEquals(completeDTO.role(), result.role());
        assertEquals(completeDTO.managementRole(), result.managementRole());
        verify(characterRepository).existsByName(completeDTO.name());
        verify(managementRepository).save(any(Management.class));
    }

    private Management getCompleteManagement() {
        Management completeManagement = new Management();
        completeManagement.setId(3L);
        completeManagement.setName("Complete Manager");
        completeManagement.setHeight(165.5);
        completeManagement.setAge(18);
        completeManagement.setYear(Year.THIRD);
        completeManagement.setRole(Role.MANAGEMENT);
        completeManagement.setSchool(mockSchool);
        completeManagement.setImgUrl("https://example.url/complete.png");
        completeManagement.setManagementRole(ManagementRole.ADVISOR);
        return completeManagement;
    }

    @Test
    @DisplayName("Find all management - Success")
    void findAllManagement_Success() {
        List<Management> managementList = List.of(mockManagement);

        when(managementRepository.findAll()).thenReturn(managementList);

        List<ManagementDTO> result = managementService.findAllManagement();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockManagement.getName(), result.getFirst().name());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find all management - Empty list")
    void findAllManagement_EmptyList() {
        when(managementRepository.findAll()).thenReturn(new ArrayList<>());

        List<ManagementDTO> result = managementService.findAllManagement();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find all management - Multiple managers")
    void findAllManagement_MultipleManagers() {
        Management manager1 = new Management();
        manager1.setId(1L);
        manager1.setName("Manager 1");
        manager1.setManagementRole(ManagementRole.MANAGER);
        manager1.setSchool(mockSchool);

        Management advisor = new Management();
        advisor.setId(2L);
        advisor.setName("Advisor");
        advisor.setManagementRole(ManagementRole.ADVISOR);
        advisor.setSchool(mockSchool);

        Management manager2 = new Management();
        manager2.setId(3L);
        manager2.setName("Manager 2");
        manager2.setManagementRole(ManagementRole.MANAGER);
        manager2.setSchool(mockSchool);

        List<Management> managementList = List.of(manager1, advisor, manager2);

        when(managementRepository.findAll()).thenReturn(managementList);

        List<ManagementDTO> result = managementService.findAllManagement();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Manager 1", result.get(0).name());
        assertEquals("Advisor", result.get(1).name());
        assertEquals("Manager 2", result.get(2).name());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find by school ID - Success")
    void findBySchoolId_Success() {
        Long schoolId = 1L;

        Management manager1 = new Management();
        manager1.setId(1L);
        manager1.setName("Manager 1");
        manager1.setSchool(mockSchool);

        Management manager2 = new Management();
        manager2.setId(2L);
        manager2.setName("Manager 2");
        manager2.setSchool(mockSchool);

        School otherSchool = new School();
        otherSchool.setId(2L);
        otherSchool.setName("Nekoma High");

        Management manager3 = new Management();
        manager3.setId(3L);
        manager3.setName("Manager 3");
        manager3.setSchool(otherSchool);

        List<Management> allManagement = List.of(manager1, manager2, manager3);

        when(managementRepository.findAll()).thenReturn(allManagement);

        List<ManagementDTO> result = managementService.findBySchoolId(schoolId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.schoolId().equals(schoolId)));
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find by school ID - None found")
    void findBySchoolId_NoneFound() {
        Long schoolId = 999L;
        List<Management> managementList = List.of(mockManagement);

        when(managementRepository.findAll()).thenReturn(managementList);

        List<ManagementDTO> result = managementService.findBySchoolId(schoolId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find by management role - Success")
    void findByManagementRole_Success() {
        Management manager1 = new Management();
        manager1.setId(1L);
        manager1.setName("Manager 1");
        manager1.setManagementRole(ManagementRole.MANAGER);
        manager1.setSchool(mockSchool);

        Management advisor = new Management();
        advisor.setId(2L);
        advisor.setName("Advisor");
        advisor.setManagementRole(ManagementRole.ADVISOR);
        advisor.setSchool(mockSchool);

        Management manager2 = new Management();
        manager2.setId(3L);
        manager2.setName("Manager 2");
        manager2.setManagementRole(ManagementRole.MANAGER);
        manager2.setSchool(mockSchool);

        List<Management> allManagement = List.of(manager1, advisor, manager2);

        when(managementRepository.findAll()).thenReturn(allManagement);

        List<ManagementDTO> result = managementService.findByManagementRole(ManagementRole.MANAGER);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.managementRole() == ManagementRole.MANAGER));
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find by management role - None found")
    void findByManagementRole_NoneFound() {
        List<Management> managementList = List.of(mockManagement);

        when(managementRepository.findAll()).thenReturn(managementList);

        List<ManagementDTO> result = managementService.findByManagementRole(ManagementRole.ADVISOR);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(managementRepository).findAll();
    }

    @Test
    @DisplayName("Find by management role - All roles")
    void findByManagementRole_AllRoles() {
        Management manager = new Management();
        manager.setId(1L);
        manager.setName("Manager");
        manager.setManagementRole(ManagementRole.MANAGER);
        manager.setSchool(mockSchool);

        Management advisor = new Management();
        advisor.setId(2L);
        advisor.setName("Advisor");
        advisor.setManagementRole(ManagementRole.ADVISOR);
        advisor.setSchool(mockSchool);

        List<Management> allManagement = List.of(manager, advisor);

        when(managementRepository.findAll()).thenReturn(allManagement);

        // Test MANAGER
        List<ManagementDTO> managers = managementService.findByManagementRole(ManagementRole.MANAGER);
        assertEquals(1, managers.size());
        assertEquals(ManagementRole.MANAGER, managers.getFirst().managementRole());

        // Test ADVISOR
        List<ManagementDTO> advisors = managementService.findByManagementRole(ManagementRole.ADVISOR);
        assertEquals(1, advisors.size());
        assertEquals(ManagementRole.ADVISOR, advisors.getFirst().managementRole());

        verify(managementRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Map management list to DTO - Success")
    void mapManagementListToDTO_Success() {
        Management manager1 = new Management();
        manager1.setId(1L);
        manager1.setName("Manager 1");
        manager1.setManagementRole(ManagementRole.MANAGER);
        manager1.setSchool(mockSchool);

        Management advisor = new Management();
        advisor.setId(2L);
        advisor.setName("Advisor");
        advisor.setManagementRole(ManagementRole.ADVISOR);
        advisor.setSchool(mockSchool);

        List<Management> managementList = List.of(manager1, advisor);

        List<ManagementDTO> result = managementService.mapManagementListToDTO(managementList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Manager 1", result.get(0).name());
        assertEquals("Advisor", result.get(1).name());
    }

    @Test
    @DisplayName("Map management list to DTO - Empty list")
    void mapManagementListToDTO_EmptyList() {
        List<Management> emptyList = new ArrayList<>();

        List<ManagementDTO> result = managementService.mapManagementListToDTO(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert management to DTO - With all fields")
    void convertManagementToDTO_WithAllFields() {
        Management completeManagement = getManagement();

        List<Management> managementList = List.of(completeManagement);

        List<ManagementDTO> result = managementService.mapManagementListToDTO(managementList);

        assertNotNull(result);
        assertEquals(1, result.size());
        ManagementDTO dto = result.getFirst();
        assertEquals(5L, dto.id());
        assertEquals("Complete Manager", dto.name());
        assertEquals(168.0, dto.height());
        assertEquals(17, dto.age());
        assertEquals(Year.SECOND, dto.year());
        assertEquals(Role.MANAGEMENT, dto.role());
        assertEquals(1L, dto.schoolId());
        assertEquals("Karasuno High", dto.schoolName());
        assertEquals("https://example.url/complete.png", dto.imgUrl());
        assertEquals(ManagementRole.MANAGER, dto.managementRole());
    }

    private Management getManagement() {
        Management completeManagement = new Management();
        completeManagement.setId(5L);
        completeManagement.setName("Complete Manager");
        completeManagement.setHeight(168.0);
        completeManagement.setAge(17);
        completeManagement.setYear(Year.SECOND);
        completeManagement.setRole(Role.MANAGEMENT);
        completeManagement.setSchool(mockSchool);
        completeManagement.setImgUrl("https://example.url/complete.png");
        completeManagement.setManagementRole(ManagementRole.MANAGER);
        return completeManagement;
    }

    @Test
    @DisplayName("Convert management to DTO - Without school")
    void convertManagementToDTO_WithoutSchool() {
        Management managementWithoutSchool = new Management();
        managementWithoutSchool.setId(6L);
        managementWithoutSchool.setName("No School Manager");
        managementWithoutSchool.setSchool(null);
        managementWithoutSchool.setManagementRole(ManagementRole.MANAGER);

        List<Management> managementList = List.of(managementWithoutSchool);

        List<ManagementDTO> result = managementService.mapManagementListToDTO(managementList);

        assertNotNull(result);
        assertEquals(1, result.size());
        ManagementDTO dto = result.getFirst();
        assertEquals(6L, dto.id());
        assertEquals("No School Manager", dto.name());
        assertNull(dto.schoolId());
        assertNull(dto.schoolName());
    }

    @Test
    @DisplayName("Create management - All year levels")
    void createManagement_AllYearLevels() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test FIRST
        ManagementDTO firstYearDTO = new ManagementDTO(null, "First Year Manager", 160.0, 16,
                Year.FIRST, Role.MANAGEMENT, 1L, "Karasuno High", "url", ManagementRole.ADVISOR);
        Management firstYear = new Management();
        firstYear.setName("First Year Manager");
        firstYear.setYear(Year.FIRST);
        firstYear.setSchool(mockSchool);
        when(managementRepository.save(any(Management.class))).thenReturn(firstYear);
        ManagementDTO result1 = managementService.createManagement(firstYearDTO);
        assertEquals(Year.FIRST, result1.year());

        // Test SECOND
        ManagementDTO secondYearDTO = new ManagementDTO(null, "Second Year Manager", 165.0, 17,
                Year.SECOND, Role.MANAGEMENT, 1L, "Karasuno High", "url", ManagementRole.MANAGER);
        Management secondYear = new Management();
        secondYear.setName("Second Year Manager");
        secondYear.setYear(Year.SECOND);
        secondYear.setSchool(mockSchool);
        when(managementRepository.save(any(Management.class))).thenReturn(secondYear);
        ManagementDTO result2 = managementService.createManagement(secondYearDTO);
        assertEquals(Year.SECOND, result2.year());

        // Test THIRD
        ManagementDTO thirdYearDTO = new ManagementDTO(null, "Third Year Manager", 168.0, 18,
                Year.THIRD, Role.MANAGEMENT, 1L, "Karasuno High", "url", ManagementRole.MANAGER);
        Management thirdYear = new Management();
        thirdYear.setName("Third Year Manager");
        thirdYear.setYear(Year.THIRD);
        thirdYear.setSchool(mockSchool);
        when(managementRepository.save(any(Management.class))).thenReturn(thirdYear);
        ManagementDTO result3 = managementService.createManagement(thirdYearDTO);
        assertEquals(Year.THIRD, result3.year());

        verify(managementRepository, times(3)).save(any(Management.class));
    }

    @Test
    @DisplayName("Create management - All management roles")
    void createManagement_AllManagementRoles() {
        when(characterRepository.existsByName(anyString())).thenReturn(false);
        when(schoolRepository.findById(anyLong())).thenReturn(Optional.of(mockSchool));

        // Test MANAGER
        ManagementDTO managerDTO = new ManagementDTO(null, "Manager", 165.0, 17,
                Year.THIRD, Role.MANAGEMENT, 1L, "Karasuno High", "url", ManagementRole.MANAGER);
        Management manager = new Management();
        manager.setName("Manager");
        manager.setManagementRole(ManagementRole.MANAGER);
        manager.setSchool(mockSchool);
        when(managementRepository.save(any(Management.class))).thenReturn(manager);
        ManagementDTO result1 = managementService.createManagement(managerDTO);
        assertEquals(ManagementRole.MANAGER, result1.managementRole());

        // Test ADVISOR
        ManagementDTO advisorDTO = new ManagementDTO(null, "Advisor", 160.0, 16,
                Year.SECOND, Role.MANAGEMENT, 1L, "Karasuno High", "url", ManagementRole.ADVISOR);
        Management advisor = new Management();
        advisor.setName("Advisor");
        advisor.setManagementRole(ManagementRole.ADVISOR);
        advisor.setSchool(mockSchool);
        when(managementRepository.save(any(Management.class))).thenReturn(advisor);
        ManagementDTO result2 = managementService.createManagement(advisorDTO);
        assertEquals(ManagementRole.ADVISOR, result2.managementRole());

        verify(managementRepository, times(2)).save(any(Management.class));
    }

    @Test
    @DisplayName("Find by school ID - Multiple schools")
    void findBySchoolId_MultipleSchools() {
        School school1 = new School();
        school1.setId(1L);
        school1.setName("Karasuno High");

        School school2 = new School();
        school2.setId(2L);
        school2.setName("Nekoma High");

        Management karasunoManager1 = new Management();
        karasunoManager1.setId(1L);
        karasunoManager1.setName("Karasuno Manager 1");
        karasunoManager1.setSchool(school1);

        Management nekomaManager = new Management();
        nekomaManager.setId(2L);
        nekomaManager.setName("Nekoma Manager");
        nekomaManager.setSchool(school2);

        Management karasunoManager2 = new Management();
        karasunoManager2.setId(3L);
        karasunoManager2.setName("Karasuno Manager 2");
        karasunoManager2.setSchool(school1);

        List<Management> allManagement = List.of(karasunoManager1, nekomaManager, karasunoManager2);

        when(managementRepository.findAll()).thenReturn(allManagement);

        List<ManagementDTO> karasunoResult = managementService.findBySchoolId(1L);
        assertEquals(2, karasunoResult.size());

        List<ManagementDTO> nekomaResult = managementService.findBySchoolId(2L);
        assertEquals(1, nekomaResult.size());

        verify(managementRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Create management - Verify all fields are set correctly")
    void createManagement_VerifyAllFieldsSet() {
        ManagementDTO completeDTO = new ManagementDTO(
                null,
                "Verified Manager",
                167.5,
                18,
                Year.THIRD,
                Role.MANAGEMENT,
                1L,
                "Karasuno High",
                "https://example.url/verified.png",
                ManagementRole.MANAGER
        );

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(managementRepository.save(any(Management.class))).thenAnswer(invocation -> {
            Management savedManagement = invocation.getArgument(0);
            // Verify all fields are set correctly
            assertEquals("Verified Manager", savedManagement.getName());
            assertEquals(167.5, savedManagement.getHeight());
            assertEquals(18, savedManagement.getAge());
            assertEquals(Year.THIRD, savedManagement.getYear());
            assertEquals(Role.MANAGEMENT, savedManagement.getRole());
            assertEquals("https://example.url/verified.png", savedManagement.getImgUrl());
            assertEquals(ManagementRole.MANAGER, savedManagement.getManagementRole());
            assertEquals(mockSchool, savedManagement.getSchool());
            return savedManagement;
        });

        ManagementDTO result = managementService.createManagement(completeDTO);

        assertNotNull(result);
        verify(managementRepository).save(any(Management.class));
    }
}