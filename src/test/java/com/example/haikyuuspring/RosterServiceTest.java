package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.*;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.*;
import com.example.haikyuuspring.model.entity.Character;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.repository.*;
import com.example.haikyuuspring.services.*;
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
class RosterServiceTest {

    @Mock
    private RosterRepository rosterRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private ManagementRepository managementRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private CoachService coachService;

    @Mock
    private ManagementService managementService;

    @InjectMocks
    private RosterService rosterService;

    private Roster mockRoster;
    private Player mockPlayer;
    private Coach mockCoach;
    private Management mockManagement;
    private Character mockCharacter;

    @BeforeEach
    void setUp() {
        // Roster ID matches its school ID (e.g., School 1 has Roster 1)
        mockRoster = new Roster();
        mockRoster.setPlayers(new ArrayList<>());
        mockRoster.setCoaches(new ArrayList<>());
        mockRoster.setManagement(new ArrayList<>());

        // Player extends Character (inheritance join architecture)
        mockPlayer = new Player();
        mockPlayer.setId(1L);
        mockPlayer.setName("Test Player A");

        // Coach extends Character (inheritance join architecture)
        mockCoach = new Coach();
        mockCoach.setId(2L);
        mockCoach.setName("Test Coach A");

        // Management extends Character (inheritance join architecture)
        mockManagement = new Management();
        mockManagement.setId(3L);
        mockManagement.setName("Test Management A");

        mockCharacter = mockPlayer;
    }

    @Test
    @DisplayName("Add player to roster - Success")
    void addPlayerToRoster_Success() {
        Long rosterId = 1L;
        Long playerId = 1L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(mockPlayer));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        RosterDTO result = rosterService.addPlayerToRoster(rosterId, playerId);

        assertNotNull(result);
        verify(rosterRepository).findById(rosterId);
        verify(playerRepository).findById(playerId);
        verify(rosterRepository).save(mockRoster);
    }

    @Test
    @DisplayName("Add player to roster - Roster not found")
    void addPlayerToRoster_RosterNotFound() {
        Long rosterId = 999L;
        Long playerId = 1L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.addPlayerToRoster(rosterId, playerId)
        );

        verify(rosterRepository).findById(rosterId);
        verify(playerRepository, never()).findById(anyLong());
        verify(rosterRepository, never()).save(any());
    }

    @Test
    @DisplayName("Add player to roster - Player not found")
    void addPlayerToRoster_PlayerNotFound() {
        Long rosterId = 1L;
        Long playerId = 999L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.addPlayerToRoster(rosterId, playerId)
        );

        verify(rosterRepository).findById(rosterId);
        verify(playerRepository).findById(playerId);
        verify(rosterRepository, never()).save(any());
    }

    @Test
    @DisplayName("Add coach to roster - Success")
    void addCoachToRoster_Success() {
        Long rosterId = 1L;
        Long coachId = 2L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(coachRepository.findById(coachId)).thenReturn(Optional.of(mockCoach));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        RosterDTO result = rosterService.addCoachToRoster(rosterId, coachId);

        assertNotNull(result);
        verify(rosterRepository).findById(rosterId);
        verify(coachRepository).findById(coachId);
        verify(rosterRepository).save(mockRoster);
    }

    @Test
    @DisplayName("Add coach to roster - Coach not found")
    void addCoachToRoster_CoachNotFound() {
        Long rosterId = 1L;
        Long coachId = 999L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(coachRepository.findById(coachId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.addCoachToRoster(rosterId, coachId)
        );

        verify(rosterRepository).findById(rosterId);
        verify(coachRepository).findById(coachId);
        verify(rosterRepository, never()).save(any());
    }

    @Test
    @DisplayName("Add management to roster - Success")
    void addManagementToRoster_Success() {
        Long rosterId = 1L;
        Long managementId = 3L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(managementRepository.findById(managementId)).thenReturn(Optional.of(mockManagement));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        RosterDTO result = rosterService.addManagementToRoster(rosterId, managementId);

        assertNotNull(result);
        verify(rosterRepository).findById(rosterId);
        verify(managementRepository).findById(managementId);
        verify(rosterRepository).save(mockRoster);
    }

    @Test
    @DisplayName("Add management to roster - Management not found")
    void addManagementToRoster_ManagementNotFound() {
        Long rosterId = 1L;
        Long managementId = 999L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(managementRepository.findById(managementId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.addManagementToRoster(rosterId, managementId)
        );

        verify(rosterRepository).findById(rosterId);
        verify(managementRepository).findById(managementId);
        verify(rosterRepository, never()).save(any());
    }

    @Test
    @DisplayName("Remove character from roster - Success")
    void removeCharacterFromRoster_Success() {
        Long rosterId = 1L;
        Long characterId = 1L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(mockCharacter));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        RosterDTO result = rosterService.removeCharacterFromRoster(rosterId, characterId);

        assertNotNull(result);
        verify(rosterRepository).findById(rosterId);
        verify(characterRepository).findById(characterId);
        verify(rosterRepository).save(mockRoster);
    }

    @Test
    @DisplayName("Remove character from roster - Character not found")
    void removeCharacterFromRoster_CharacterNotFound() {
        Long rosterId = 1L;
        Long characterId = 999L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(characterRepository.findById(characterId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.removeCharacterFromRoster(rosterId, characterId)
        );

        verify(rosterRepository).findById(rosterId);
        verify(characterRepository).findById(characterId);
        verify(rosterRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get roster by ID - Success")
    void getRosterById_Success() {
        Long rosterId = 1L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        RosterDTO result = rosterService.getRosterById(rosterId);

        assertNotNull(result);
        verify(rosterRepository).findById(rosterId);
    }

    @Test
    @DisplayName("Get roster by ID - Roster not found")
    void getRosterById_NotFound() {
        Long rosterId = 999L;

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rosterService.getRosterById(rosterId)
        );

        verify(rosterRepository).findById(rosterId);
    }

    @Test
    @DisplayName("Get roster players - Success")
    void getRosterPlayers_Success() {
        Long rosterId = 1L;
        List<PlayerDTO> expectedPlayers = new ArrayList<>();

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(playerService.mapPlayerListToDTO(mockRoster.getPlayers())).thenReturn(expectedPlayers);

        List<PlayerDTO> result = rosterService.getRosterPlayers(rosterId);

        assertNotNull(result);
        assertEquals(expectedPlayers, result);
        verify(rosterRepository).findById(rosterId);
        verify(playerService).mapPlayerListToDTO(mockRoster.getPlayers());
    }

    @Test
    @DisplayName("Get roster coaches - Success")
    void getRosterCoaches_Success() {
        Long rosterId = 1L;
        List<CoachDTO> expectedCoaches = new ArrayList<>();

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(coachService.mapCoachListToDTO(mockRoster.getCoaches())).thenReturn(expectedCoaches);

        List<CoachDTO> result = rosterService.getRosterCoaches(rosterId);

        assertNotNull(result);
        assertEquals(expectedCoaches, result);
        verify(rosterRepository).findById(rosterId);
        verify(coachService).mapCoachListToDTO(mockRoster.getCoaches());
    }

    @Test
    @DisplayName("Get roster management - Success")
    void getRosterManagement_Success() {
        Long rosterId = 1L;
        List<ManagementDTO> expectedManagement = new ArrayList<>();

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(managementService.mapManagementListToDTO(mockRoster.getManagement())).thenReturn(expectedManagement);

        List<ManagementDTO> result = rosterService.getRosterManagement(rosterId);

        assertNotNull(result);
        assertEquals(expectedManagement, result);
        verify(rosterRepository).findById(rosterId);
        verify(managementService).mapManagementListToDTO(mockRoster.getManagement());
    }

    @Test
    @DisplayName("Find players by position - Success")
    void findPlayersByPosition_Success() {
        Long rosterId = 1L;
        Position position = Position.MIDDLE_BLOCKER;

        Player player1 = new Player();
        player1.setPosition(Position.MIDDLE_BLOCKER);
        Player player2 = new Player();
        player2.setPosition(Position.WING_SPIKER);
        Player player3 = new Player();
        player3.setPosition(Position.MIDDLE_BLOCKER);

        List<Player> players = List.of(player1, player2, player3);
        mockRoster.setPlayers(players);

        when(rosterRepository.findById(rosterId)).thenReturn(Optional.of(mockRoster));
        when(playerService.convertPlayerToDTO(any(Player.class))).thenReturn(new PlayerDTO(null, "John Doe", 150.3, 16, null, null, 20L, "School", "https://example.url/image.png", Position.LIBERO, 10));

        List<PlayerDTO> result = rosterService.findPlayersByPosition(rosterId, position);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rosterRepository).findById(rosterId);
        verify(playerService, times(2)).convertPlayerToDTO(any(Player.class));
    }

    @Test
    @DisplayName("Map roster list to DTO - Success")
    void mapRosterListToDTO_Success() {
        List<Roster> rosters = List.of(mockRoster, new Roster());

        when(playerService.mapPlayerListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(coachService.mapCoachListToDTO(anyList())).thenReturn(new ArrayList<>());
        when(managementService.mapManagementListToDTO(anyList())).thenReturn(new ArrayList<>());

        List<RosterDTO> result = rosterService.mapRosterListToDTO(rosters);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(playerService, times(2)).mapPlayerListToDTO(anyList());
        verify(coachService, times(2)).mapCoachListToDTO(anyList());
        verify(managementService, times(2)).mapManagementListToDTO(anyList());
    }
}