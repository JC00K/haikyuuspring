package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Roster;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.CoachService;
import com.example.haikyuuspring.services.ManagementService;
import com.example.haikyuuspring.services.PlayerService;
import com.example.haikyuuspring.services.SchoolService;
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
class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private CoachService coachService;

    @Mock
    private ManagementService managementService;

    @InjectMocks
    private SchoolService schoolService;

    private School mockSchool;
    private SchoolDTO mockSchoolDTO;
    private Roster mockRoster;

    @BeforeEach
    void setUp() {
        mockRoster = new Roster();
        mockRoster.setId(1L);
        mockRoster.setPlayers(new ArrayList<>());
        mockRoster.setCoaches(new ArrayList<>());
        mockRoster.setManagement(new ArrayList<>());

        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");
        mockSchool.setPrefecture("Miyagi");
        mockSchool.setRoster(mockRoster);
        mockSchool.setMotto("Fly High");
        mockSchool.setColors("Black and Orange");
        mockSchool.setMascot("Crow");
        mockSchool.setImgUrl("https://example.url/karasuno.png");

        mockRoster.setSchool(mockSchool);

        RosterDTO rosterDTO = new RosterDTO(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        mockSchoolDTO = new SchoolDTO(
                null,
                "Karasuno High",
                "Miyagi",
                rosterDTO,
                "Fly High",
                "Black and Orange",
                "Crow",
                "https://example.url/karasuno.png"
        );
    }

    @Test
    @DisplayName("Create school - Success")
    void createSchool_Success() {
        when(schoolRepository.existsByName(mockSchoolDTO.name())).thenReturn(false);
        when(schoolRepository.save(any(School.class))).thenReturn(mockSchool);
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        SchoolDTO result = schoolService.createSchool(mockSchoolDTO);

        assertNotNull(result);
        assertEquals(mockSchoolDTO.name(), result.name());
        assertEquals(mockSchoolDTO.prefecture(), result.prefecture());
        assertEquals(mockSchoolDTO.motto(), result.motto());
        assertEquals(mockSchoolDTO.colors(), result.colors());
        assertEquals(mockSchoolDTO.mascot(), result.mascot());
        verify(schoolRepository).existsByName(mockSchoolDTO.name());
        verify(schoolRepository).save(any(School.class));
    }

    @Test
    @DisplayName("Create school - Duplicate name throws exception")
    void createSchool_DuplicateName() {
        when(schoolRepository.existsByName(mockSchoolDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                schoolService.createSchool(mockSchoolDTO)
        );

        verify(schoolRepository).existsByName(mockSchoolDTO.name());
        verify(schoolRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create school - With all fields")
    void createSchool_WithAllFields() {
        SchoolDTO completeDTO = new SchoolDTO(
                null,
                "Nekoma High",
                "Tokyo",
                new RosterDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                "Connect",
                "Red and Black",
                "Cat",
                "https://example.url/nekoma.png"
        );

        School completeSchool = new School();
        completeSchool.setId(2L);
        completeSchool.setName("Nekoma High");
        completeSchool.setPrefecture("Tokyo");
        completeSchool.setRoster(mockRoster);
        completeSchool.setMotto("Connect");
        completeSchool.setColors("Red and Black");
        completeSchool.setMascot("Cat");
        completeSchool.setImgUrl("https://example.url/nekoma.png");

        when(schoolRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.save(any(School.class))).thenReturn(completeSchool);
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        SchoolDTO result = schoolService.createSchool(completeDTO);

        assertNotNull(result);
        assertEquals(completeDTO.name(), result.name());
        assertEquals(completeDTO.prefecture(), result.prefecture());
        assertEquals(completeDTO.motto(), result.motto());
        assertEquals(completeDTO.colors(), result.colors());
        assertEquals(completeDTO.mascot(), result.mascot());
        verify(schoolRepository).existsByName(completeDTO.name());
        verify(schoolRepository).save(any(School.class));
    }

    @Test
    @DisplayName("Create school - Roster is automatically created")
    void createSchool_RosterAutoCreated() {
        when(schoolRepository.existsByName(mockSchoolDTO.name())).thenReturn(false);
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> {
            School savedSchool = invocation.getArgument(0);
            assertNotNull(savedSchool.getRoster());
            assertNotNull(savedSchool.getRoster().getSchool());
            return mockSchool;
        });
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        SchoolDTO result = schoolService.createSchool(mockSchoolDTO);

        assertNotNull(result);
        verify(schoolRepository).save(any(School.class));
    }

    @Test
    @DisplayName("Delete school - Success")
    void deleteSchool_Success() {
        Long schoolId = 1L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        doNothing().when(schoolRepository).delete(mockSchool);

        assertDoesNotThrow(() -> schoolService.deleteSchool(schoolId));

        verify(schoolRepository).findById(schoolId);
        verify(schoolRepository).delete(mockSchool);
    }

    @Test
    @DisplayName("Delete school - School not found")
    void deleteSchool_SchoolNotFound() {
        Long schoolId = 999L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                schoolService.deleteSchool(schoolId)
        );

        verify(schoolRepository).findById(schoolId);
        verify(schoolRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get school info - Success")
    void getSchoolInfo_Success() {
        Long schoolId = 1L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        SchoolDTO result = schoolService.getSchoolInfo(schoolId);

        assertNotNull(result);
        assertEquals(mockSchool.getId(), result.id());
        assertEquals(mockSchool.getName(), result.name());
        assertEquals(mockSchool.getPrefecture(), result.prefecture());
        assertEquals(mockSchool.getMotto(), result.motto());
        verify(schoolRepository).findById(schoolId);
    }

    @Test
    @DisplayName("Get school info - School not found")
    void getSchoolInfo_SchoolNotFound() {
        Long schoolId = 999L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                schoolService.getSchoolInfo(schoolId)
        );

        verify(schoolRepository).findById(schoolId);
    }

    @Test
    @DisplayName("Get school info - With roster data")
    void getSchoolInfo_WithRosterData() {
        Long schoolId = 1L;

        List<PlayerDTO> players = new ArrayList<>();
        List<CoachDTO> coaches = new ArrayList<>();
        List<ManagementDTO> management = new ArrayList<>();

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));
        when(playerService.mapPlayerListToDTO(mockRoster.getPlayers())).thenReturn(players);
        when(coachService.mapCoachListToDTO(mockRoster.getCoaches())).thenReturn(coaches);
        when(managementService.mapManagementListToDTO(mockRoster.getManagement())).thenReturn(management);

        SchoolDTO result = schoolService.getSchoolInfo(schoolId);

        assertNotNull(result);
        assertNotNull(result.roster());
        assertEquals(players, result.roster().players());
        assertEquals(coaches, result.roster().coaches());
        assertEquals(management, result.roster().management());
        verify(playerService).mapPlayerListToDTO(mockRoster.getPlayers());
        verify(coachService).mapCoachListToDTO(mockRoster.getCoaches());
        verify(managementService).mapManagementListToDTO(mockRoster.getManagement());
    }

    @Test
    @DisplayName("Get school info - Without roster")
    void getSchoolInfo_WithoutRoster() {
        Long schoolId = 1L;
        mockSchool.setRoster(null);

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(mockSchool));

        SchoolDTO result = schoolService.getSchoolInfo(schoolId);

        assertNotNull(result);
        assertEquals(" ", result.motto());
        verify(schoolRepository).findById(schoolId);
        verify(playerService, never()).mapPlayerListToDTO(anyList());
        verify(coachService, never()).mapCoachListToDTO(anyList());
        verify(managementService, never()).mapManagementListToDTO(anyList());
    }

    @Test
    @DisplayName("Find by prefecture - Success")
    void findByPrefecture_Success() {
        String prefecture = "Miyagi";

        School school1 = new School();
        school1.setId(1L);
        school1.setName("Karasuno High");
        school1.setPrefecture(prefecture);
        school1.setRoster(mockRoster);

        School school2 = new School();
        school2.setId(2L);
        school2.setName("Aoba Johsai High");
        school2.setPrefecture(prefecture);
        school2.setRoster(mockRoster);

        List<School> schools = List.of(school1, school2);

        when(schoolRepository.findByPrefectureIgnoreCase(prefecture)).thenReturn(schools);
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        List<SchoolDTO> result = schoolService.findByPrefecture(prefecture);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Karasuno High", result.get(0).name());
        assertEquals("Aoba Johsai High", result.get(1).name());
        verify(schoolRepository).findByPrefectureIgnoreCase(prefecture);
    }

    @Test
    @DisplayName("Find by prefecture - Case insensitive")
    void findByPrefecture_CaseInsensitive() {
        String prefecture = "MIYAGI";

        School school1 = new School();
        school1.setId(1L);
        school1.setName("Karasuno High");
        school1.setPrefecture("Miyagi");
        school1.setRoster(mockRoster);

        List<School> schools = List.of(school1);

        when(schoolRepository.findByPrefectureIgnoreCase(prefecture)).thenReturn(schools);
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        List<SchoolDTO> result = schoolService.findByPrefecture(prefecture);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(schoolRepository).findByPrefectureIgnoreCase(prefecture);
    }

    @Test
    @DisplayName("Find by prefecture - Not found throws exception")
    void findByPrefecture_NotFound() {
        String prefecture = "Nonexistent";

        when(schoolRepository.findByPrefectureIgnoreCase(prefecture)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () ->
                schoolService.findByPrefecture(prefecture)
        );

        verify(schoolRepository).findByPrefectureIgnoreCase(prefecture);
    }

    @Test
    @DisplayName("Find by prefecture - Empty list throws exception")
    void findByPrefecture_EmptyList() {
        String prefecture = "Unknown";

        when(schoolRepository.findByPrefectureIgnoreCase(prefecture)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () ->
                schoolService.findByPrefecture(prefecture)
        );

        verify(schoolRepository).findByPrefectureIgnoreCase(prefecture);
    }

    @Test
    @DisplayName("Lookup for dropdown - Success")
    void lookupForDropdown_Success() {
        List<SchoolLookupView> views = getSchoolLookupViews();

        when(schoolRepository.findAllProjectedBy()).thenReturn(views);

        List<SchoolLookupDTO> result = schoolService.lookupForDropdown();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Karasuno High", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("Nekoma High", result.get(1).name());
        verify(schoolRepository).findAllProjectedBy();
    }

    private static List<SchoolLookupView> getSchoolLookupViews() {
        SchoolLookupView view1 = new SchoolLookupView() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Karasuno High";
            }
        };

        SchoolLookupView view2 = new SchoolLookupView() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public String getName() {
                return "Nekoma High";
            }
        };

        return List.of(view1, view2);
    }

    @Test
    @DisplayName("Lookup for dropdown - Empty list")
    void lookupForDropdown_EmptyList() {
        when(schoolRepository.findAllProjectedBy()).thenReturn(new ArrayList<>());

        List<SchoolLookupDTO> result = schoolService.lookupForDropdown();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(schoolRepository).findAllProjectedBy();
    }

    @Test
    @DisplayName("Lookup for dropdown - Single school")
    void lookupForDropdown_SingleSchool() {
        SchoolLookupView view = new SchoolLookupView() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Karasuno High";
            }
        };

        List<SchoolLookupView> views = List.of(view);

        when(schoolRepository.findAllProjectedBy()).thenReturn(views);

        List<SchoolLookupDTO> result = schoolService.lookupForDropdown();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("Karasuno High", result.getFirst().name());
        verify(schoolRepository).findAllProjectedBy();
    }

    @Test
    @DisplayName("Convert school to DTO - Complete school")
    void convertSchoolToDto_CompleteSchool() {
        when(playerService.mapPlayerListToDTO(mockRoster.getPlayers())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(mockRoster.getCoaches())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(mockRoster.getManagement())).thenReturn(new ArrayList<>());
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(mockSchool));

        SchoolDTO result = schoolService.getSchoolInfo(1L);

        assertNotNull(result);
        assertEquals(mockSchool.getId(), result.id());
        assertEquals(mockSchool.getName(), result.name());
        assertEquals(mockSchool.getPrefecture(), result.prefecture());
        assertEquals(mockSchool.getMotto(), result.motto());
        assertEquals(mockSchool.getColors(), result.colors());
        assertEquals(mockSchool.getMascot(), result.mascot());
        assertEquals(mockSchool.getImgUrl(), result.imgUrl());
        assertNotNull(result.roster());
    }

    @Test
    @DisplayName("Find by prefecture - Multiple schools from same prefecture")
    void findByPrefecture_MultipleSchools() {
        String prefecture = "Tokyo";

        School school1 = new School();
        school1.setId(1L);
        school1.setName("Nekoma High");
        school1.setPrefecture(prefecture);
        school1.setRoster(mockRoster);

        School school2 = new School();
        school2.setId(2L);
        school2.setName("Fukurodani Academy");
        school2.setPrefecture(prefecture);
        school2.setRoster(mockRoster);

        School school3 = new School();
        school3.setId(3L);
        school3.setName("Itachiyama Institute");
        school3.setPrefecture(prefecture);
        school3.setRoster(mockRoster);

        List<School> schools = List.of(school1, school2, school3);

        when(schoolRepository.findByPrefectureIgnoreCase(prefecture)).thenReturn(schools);
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        List<SchoolDTO> result = schoolService.findByPrefecture(prefecture);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Nekoma High", result.get(0).name());
        assertEquals("Fukurodani Academy", result.get(1).name());
        assertEquals("Itachiyama Institute", result.get(2).name());
        verify(schoolRepository).findByPrefectureIgnoreCase(prefecture);
    }

    @Test
    @DisplayName("Create school - Verify bidirectional relationship")
    void createSchool_BidirectionalRelationship() {
        when(schoolRepository.existsByName(mockSchoolDTO.name())).thenReturn(false);
        when(schoolRepository.save(any(School.class))).thenAnswer(invocation -> {
            School savedSchool = invocation.getArgument(0);
            assertNotNull(savedSchool.getRoster());
            assertEquals(savedSchool, savedSchool.getRoster().getSchool());
            return mockSchool;
        });
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        SchoolDTO result = schoolService.createSchool(mockSchoolDTO);

        assertNotNull(result);
        verify(schoolRepository).save(any(School.class));
    }
}