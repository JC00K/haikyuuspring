package com.example.haikyuuspring;

import com.example.haikyuuspring.controller.dto.PlayerDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Player;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.PlayerRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import com.example.haikyuuspring.services.PlayerService;
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
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player mockPlayer;
    private PlayerDTO mockPlayerDTO;
    private School mockSchool;

    @BeforeEach
    void setUp() {
        // Mock School
        mockSchool = new School();
        mockSchool.setId(1L);
        mockSchool.setName("Karasuno High");

        // Mock Player entity
        mockPlayer = new Player();
        mockPlayer.setId(1L);
        mockPlayer.setName("Shoyo Hinata");
        mockPlayer.setHeight(162.8);
        mockPlayer.setAge(16);
        mockPlayer.setYear(Year.FIRST);
        mockPlayer.setRole(Role.PLAYER);
        mockPlayer.setSchool(mockSchool);
        mockPlayer.setImgUrl("https://example.url/hinata.png");
        mockPlayer.setPosition(Position.MIDDLE_BLOCKER);
        mockPlayer.setJerseyNumber(10);

        mockPlayerDTO = new PlayerDTO(
                null,
                "Shoyo Hinata",
                162.8,
                16,
                Year.FIRST,
                Role.PLAYER,
                1L,
                "Karasuno High",
                "https://example.url/hinata.png",
                Position.MIDDLE_BLOCKER,
                10
        );
    }

    @Test
    @DisplayName("Create player - Success")
    void createPlayer_Success() {
        when(characterRepository.existsByName(mockPlayerDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockPlayerDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        PlayerDTO result = playerService.createPlayer(mockPlayerDTO);

        assertNotNull(result);
        assertEquals(mockPlayerDTO.name(), result.name());
        assertEquals(mockPlayerDTO.position(), result.position());
        assertEquals(mockPlayerDTO.jerseyNumber(), result.jerseyNumber());
        verify(characterRepository).existsByName(mockPlayerDTO.name());
        verify(schoolRepository).findById(mockPlayerDTO.schoolId());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Create player - Duplicate name throws exception")
    void createPlayer_DuplicateName() {
        when(characterRepository.existsByName(mockPlayerDTO.name())).thenReturn(true);

        assertThrows(ResourceDuplicateException.class, () ->
                playerService.createPlayer(mockPlayerDTO)
        );

        verify(characterRepository).existsByName(mockPlayerDTO.name());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create player - School not found")
    void createPlayer_SchoolNotFound() {
        when(characterRepository.existsByName(mockPlayerDTO.name())).thenReturn(false);
        when(schoolRepository.findById(mockPlayerDTO.schoolId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                playerService.createPlayer(mockPlayerDTO)
        );

        verify(characterRepository).existsByName(mockPlayerDTO.name());
        verify(schoolRepository).findById(mockPlayerDTO.schoolId());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create player - Without school ID success")
    void createPlayer_WithoutSchoolId() {
        PlayerDTO noSchoolDTO = new PlayerDTO(
                null,
                "Independent Player",
                175.0,
                17,
                Year.SECOND,
                Role.PLAYER,
                null,
                null,
                "https://example.url/player.png",
                Position.SETTER,
                5
        );

        Player noSchoolPlayer = new Player();
        noSchoolPlayer.setId(2L);
        noSchoolPlayer.setName("Independent Player");
        noSchoolPlayer.setPosition(Position.SETTER);
        noSchoolPlayer.setJerseyNumber(5);

        when(characterRepository.existsByName(noSchoolDTO.name())).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(noSchoolPlayer);

        PlayerDTO result = playerService.createPlayer(noSchoolDTO);

        assertNotNull(result);
        assertEquals(noSchoolDTO.name(), result.name());
        verify(characterRepository).existsByName(noSchoolDTO.name());
        verify(schoolRepository, never()).findById(anyLong());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Create player - With all fields")
    void createPlayer_WithAllFields() {
        PlayerDTO completeDTO = new PlayerDTO(
                null,
                "Complete Player",
                180.5,
                18,
                Year.THIRD,
                Role.PLAYER,
                1L,
                "Karasuno High",
                "https://example.url/complete.png",
                Position.WING_SPIKER,
                4
        );

        Player completePlayer = getPlayer();

        when(characterRepository.existsByName(completeDTO.name())).thenReturn(false);
        when(schoolRepository.findById(completeDTO.schoolId())).thenReturn(Optional.of(mockSchool));
        when(playerRepository.save(any(Player.class))).thenReturn(completePlayer);

        PlayerDTO result = playerService.createPlayer(completeDTO);

        assertNotNull(result);
        assertEquals(completeDTO.name(), result.name());
        assertEquals(completeDTO.height(), result.height());
        assertEquals(completeDTO.age(), result.age());
        assertEquals(completeDTO.year(), result.year());
        assertEquals(completeDTO.role(), result.role());
        assertEquals(completeDTO.position(), result.position());
        assertEquals(completeDTO.jerseyNumber(), result.jerseyNumber());
        verify(characterRepository).existsByName(completeDTO.name());
        verify(playerRepository).save(any(Player.class));
    }

    private Player getPlayer() {
        Player completePlayer = new Player();
        completePlayer.setId(3L);
        completePlayer.setName("Complete Player");
        completePlayer.setHeight(180.5);
        completePlayer.setAge(18);
        completePlayer.setYear(Year.THIRD);
        completePlayer.setRole(Role.PLAYER);
        completePlayer.setSchool(mockSchool);
        completePlayer.setImgUrl("https://example.url/complete.png");
        completePlayer.setPosition(Position.WING_SPIKER);
        completePlayer.setJerseyNumber(4);
        return completePlayer;
    }

    @Test
    @DisplayName("Find all players - Success")
    void findAllPlayers_Success() {
        List<Player> playerList = List.of(mockPlayer);

        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDTO> result = playerService.findAllPlayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockPlayer.getName(), result.getFirst().name());
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find all players - Empty list")
    void findAllPlayers_EmptyList() {
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());

        List<PlayerDTO> result = playerService.findAllPlayers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by position - Success")
    void findByPosition_Success() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player 1");
        player1.setPosition(Position.SETTER);
        player1.setSchool(mockSchool);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player 2");
        player2.setPosition(Position.WING_SPIKER);
        player2.setSchool(mockSchool);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setName("Player 3");
        player3.setPosition(Position.SETTER);
        player3.setSchool(mockSchool);

        List<Player> allPlayers = List.of(player1, player2, player3);

        when(playerRepository.findAll()).thenReturn(allPlayers);

        List<PlayerDTO> result = playerService.findByPosition(Position.SETTER);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.position() == Position.SETTER));
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by position - None found")
    void findByPosition_NoneFound() {
        List<Player> playerList = List.of(mockPlayer);

        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDTO> result = playerService.findByPosition(Position.LIBERO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by year and position - Success")
    void findByYearAndPosition_Success() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("First Year Setter");
        player1.setYear(Year.FIRST);
        player1.setPosition(Position.SETTER);
        player1.setSchool(mockSchool);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Second Year Setter");
        player2.setYear(Year.SECOND);
        player2.setPosition(Position.SETTER);
        player2.setSchool(mockSchool);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setName("First Year Wing Spiker");
        player3.setYear(Year.FIRST);
        player3.setPosition(Position.WING_SPIKER);
        player3.setSchool(mockSchool);

        Player player4 = new Player();
        player4.setId(4L);
        player4.setName("First Year Setter 2");
        player4.setYear(Year.FIRST);
        player4.setPosition(Position.SETTER);
        player4.setSchool(mockSchool);

        List<Player> allPlayers = List.of(player1, player2, player3, player4);

        when(playerRepository.findAll()).thenReturn(allPlayers);

        List<PlayerDTO> result = playerService.findByYearAndPosition(Year.FIRST, Position.SETTER);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p ->
                p.year() == Year.FIRST && p.position() == Position.SETTER
        ));
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by year and position - None found")
    void findByYearAndPosition_NoneFound() {
        List<Player> playerList = List.of(mockPlayer);

        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDTO> result = playerService.findByYearAndPosition(Year.THIRD, Position.LIBERO);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by jersey number - Success")
    void findByJerseyNumber_Success() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player 1");
        player1.setJerseyNumber(10);
        player1.setSchool(mockSchool);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player 2");
        player2.setJerseyNumber(5);
        player2.setSchool(mockSchool);

        Player player3 = new Player();
        player3.setId(3L);
        player3.setName("Player 3");
        player3.setJerseyNumber(10);
        player3.setSchool(mockSchool);

        List<Player> allPlayers = List.of(player1, player2, player3);

        when(playerRepository.findAll()).thenReturn(allPlayers);

        List<PlayerDTO> result = playerService.findByJerseyNumber(10);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.jerseyNumber().equals(10)));
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Find by jersey number - None found")
    void findByJerseyNumber_NoneFound() {
        List<Player> playerList = List.of(mockPlayer);

        when(playerRepository.findAll()).thenReturn(playerList);

        List<PlayerDTO> result = playerService.findByJerseyNumber(99);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findAll();
    }

    @Test
    @DisplayName("Map player list to DTO - Success")
    void mapPlayerListToDTO_Success() {
        Player player1 = new Player();
        player1.setId(1L);
        player1.setName("Player 1");
        player1.setPosition(Position.SETTER);
        player1.setSchool(mockSchool);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setName("Player 2");
        player2.setPosition(Position.LIBERO);
        player2.setSchool(mockSchool);

        List<Player> playerList = List.of(player1, player2);

        List<PlayerDTO> result = playerService.mapPlayerListToDTO(playerList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Player 1", result.get(0).name());
        assertEquals("Player 2", result.get(1).name());
    }

    @Test
    @DisplayName("Map player list to DTO - Empty list")
    void mapPlayerListToDTO_EmptyList() {
        List<Player> emptyList = new ArrayList<>();

        List<PlayerDTO> result = playerService.mapPlayerListToDTO(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert player to DTO - With all fields")
    void convertPlayerToDTO_WithAllFields() {
        Player completePlayer = getCompletePlayer();

        PlayerDTO result = playerService.convertPlayerToDTO(completePlayer);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals("Complete Player", result.name());
        assertEquals(185.0, result.height());
        assertEquals(17, result.age());
        assertEquals(Year.SECOND, result.year());
        assertEquals(Role.PLAYER, result.role());
        assertEquals(1L, result.schoolId());
        assertEquals("Karasuno High", result.schoolName());
        assertEquals("https://example.url/complete.png", result.imgUrl());
        assertEquals(Position.WING_SPIKER, result.position());
        assertEquals(4, result.jerseyNumber());
    }

    private Player getCompletePlayer() {
        Player completePlayer = new Player();
        completePlayer.setId(5L);
        completePlayer.setName("Complete Player");
        completePlayer.setHeight(185.0);
        completePlayer.setAge(17);
        completePlayer.setYear(Year.SECOND);
        completePlayer.setRole(Role.PLAYER);
        completePlayer.setSchool(mockSchool);
        completePlayer.setImgUrl("https://example.url/complete.png");
        completePlayer.setPosition(Position.WING_SPIKER);
        completePlayer.setJerseyNumber(4);
        return completePlayer;
    }

    @Test
    @DisplayName("Convert player to DTO - Without school")
    void convertPlayerToDTO_WithoutSchool() {
        Player playerWithoutSchool = new Player();
        playerWithoutSchool.setId(6L);
        playerWithoutSchool.setName("No School Player");
        playerWithoutSchool.setPosition(Position.MIDDLE_BLOCKER);
        playerWithoutSchool.setJerseyNumber(7);
        playerWithoutSchool.setSchool(null);

        PlayerDTO result = playerService.convertPlayerToDTO(playerWithoutSchool);

        assertNotNull(result);
        assertEquals(6L, result.id());
        assertEquals("No School Player", result.name());
        assertNull(result.schoolId());
        assertNull(result.schoolName());
        assertEquals(Position.MIDDLE_BLOCKER, result.position());
        assertEquals(7, result.jerseyNumber());
    }

    @Test
    @DisplayName("Find by position - Multiple positions")
    void findByPosition_MultiplePositions() {
        Player setter1 = new Player();
        setter1.setId(1L);
        setter1.setName("Setter 1");
        setter1.setPosition(Position.SETTER);
        setter1.setSchool(mockSchool);

        Player setter2 = new Player();
        setter2.setId(2L);
        setter2.setName("Setter 2");
        setter2.setPosition(Position.SETTER);
        setter2.setSchool(mockSchool);

        Player libero = new Player();
        libero.setId(3L);
        libero.setName("Libero 1");
        libero.setPosition(Position.LIBERO);
        libero.setSchool(mockSchool);

        Player middleBlocker = new Player();
        middleBlocker.setId(4L);
        middleBlocker.setName("Middle Blocker 1");
        middleBlocker.setPosition(Position.MIDDLE_BLOCKER);
        middleBlocker.setSchool(mockSchool);

        List<Player> allPlayers = List.of(setter1, setter2, libero, middleBlocker);

        when(playerRepository.findAll()).thenReturn(allPlayers);
        
        List<PlayerDTO> setters = playerService.findByPosition(Position.SETTER);
        assertEquals(2, setters.size());
        
        List<PlayerDTO> liberos = playerService.findByPosition(Position.LIBERO);
        assertEquals(1, liberos.size());
        
        List<PlayerDTO> middleBlockers = playerService.findByPosition(Position.MIDDLE_BLOCKER);
        assertEquals(1, middleBlockers.size());

        verify(playerRepository, times(3)).findAll();
    }

    @Test
    @DisplayName("Find by year and position - All years")
    void findByYearAndPosition_AllYears() {
        Player firstYearSetter = new Player();
        firstYearSetter.setId(1L);
        firstYearSetter.setName("1st Year Setter");
        firstYearSetter.setYear(Year.FIRST);
        firstYearSetter.setPosition(Position.SETTER);
        firstYearSetter.setSchool(mockSchool);

        Player secondYearSetter = new Player();
        secondYearSetter.setId(2L);
        secondYearSetter.setName("2nd Year Setter");
        secondYearSetter.setYear(Year.SECOND);
        secondYearSetter.setPosition(Position.SETTER);
        secondYearSetter.setSchool(mockSchool);

        Player thirdYearSetter = new Player();
        thirdYearSetter.setId(3L);
        thirdYearSetter.setName("3rd Year Setter");
        thirdYearSetter.setYear(Year.THIRD);
        thirdYearSetter.setPosition(Position.SETTER);
        thirdYearSetter.setSchool(mockSchool);

        List<Player> allPlayers = List.of(firstYearSetter, secondYearSetter, thirdYearSetter);

        when(playerRepository.findAll()).thenReturn(allPlayers);
        
        List<PlayerDTO> firstYears = playerService.findByYearAndPosition(Year.FIRST, Position.SETTER);
        assertEquals(1, firstYears.size());

        List<PlayerDTO> secondYears = playerService.findByYearAndPosition(Year.SECOND, Position.SETTER);
        assertEquals(1, secondYears.size());

        List<PlayerDTO> thirdYears = playerService.findByYearAndPosition(Year.THIRD, Position.SETTER);
        assertEquals(1, thirdYears.size());

        verify(playerRepository, times(3)).findAll();
    }

    @Test
    @DisplayName("Find by height greater than - Success")
    void findByHeightGreaterThan_Success() {
        Player shortPlayer = new Player();
        shortPlayer.setId(1L);
        shortPlayer.setName("Short Player");
        shortPlayer.setHeight(165.0);
        shortPlayer.setSchool(mockSchool);

        Player tallPlayer1 = new Player();
        tallPlayer1.setId(2L);
        tallPlayer1.setName("Tall Player 1");
        tallPlayer1.setHeight(185.5);
        tallPlayer1.setSchool(mockSchool);

        Player tallPlayer2 = new Player();
        tallPlayer2.setId(3L);
        tallPlayer2.setName("Tall Player 2");
        tallPlayer2.setHeight(190.0);
        tallPlayer2.setSchool(mockSchool);

        List<Player> tallPlayers = List.of(tallPlayer1, tallPlayer2);

        when(playerRepository.findByHeightGreaterThan(180.0)).thenReturn(tallPlayers);

        List<PlayerDTO> result = playerService.findByHeightGreaterThan(180.0);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.height() > 180.0));
        verify(playerRepository).findByHeightGreaterThan(180.0);
    }

    @Test
    @DisplayName("Find by height greater than - None found")
    void findByHeightGreaterThan_NoneFound() {
        when(playerRepository.findByHeightGreaterThan(200.0)).thenReturn(new ArrayList<>());

        List<PlayerDTO> result = playerService.findByHeightGreaterThan(200.0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findByHeightGreaterThan(200.0);
    }

    @Test
    @DisplayName("Find by height greater than - Empty list")
    void findByHeightGreaterThan_EmptyList() {
        when(playerRepository.findByHeightGreaterThan(250.0)).thenReturn(new ArrayList<>());

        List<PlayerDTO> result = playerService.findByHeightGreaterThan(250.0);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(playerRepository).findByHeightGreaterThan(250.0);
    }

    @Test
    @DisplayName("Find by height less than - Success")
    void findByHeightLessThan_Success() {
        Player shortPlayer1 = new Player();
        shortPlayer1.setId(1L);
        shortPlayer1.setName("Short Player 1");
        shortPlayer1.setHeight(160.0);
        shortPlayer1.setSchool(mockSchool);

        Player shortPlayer2 = new Player();
        shortPlayer2.setId(2L);
        shortPlayer2.setName("Short Player 2");
        shortPlayer2.setHeight(165.5);
        shortPlayer2.setSchool(mockSchool);

        Player tallPlayer = new Player();
        tallPlayer.setId(3L);
        tallPlayer.setName("Tall Player");
        tallPlayer.setHeight(185.0);
        tallPlayer.setSchool(mockSchool);

        List<Player> shortPlayers = List.of(shortPlayer1, shortPlayer2);

        when(playerRepository.findByHeightLessThan(170.0)).thenReturn(shortPlayers);

        List<PlayerDTO> result = playerService.findByHeightLessThan(170.0);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.height() < 170.0));
        verify(playerRepository).findByHeightLessThan(170.0);
    }

    @Test
    @DisplayName("Find by height less than - None found")
    void findByHeightLessThan_NoneFound() {
        when(playerRepository.findByHeightLessThan(150.0)).thenReturn(new ArrayList<>());

        List<PlayerDTO> result = playerService.findByHeightLessThan(150.0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository).findByHeightLessThan(150.0);
    }

    @Test
    @DisplayName("Find by height less than - Empty list")
    void findByHeightLessThan_EmptyList() {
        when(playerRepository.findByHeightLessThan(100.0)).thenReturn(new ArrayList<>());

        List<PlayerDTO> result = playerService.findByHeightLessThan(100.0);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(playerRepository).findByHeightLessThan(100.0);
    }

    @Test
    @DisplayName("Find by height greater than - Boundary test")
    void findByHeightGreaterThan_BoundaryTest() {
        Player exactHeight = new Player();
        exactHeight.setId(1L);
        exactHeight.setName("Exact Height Player");
        exactHeight.setHeight(180.0);
        exactHeight.setSchool(mockSchool);

        Player slightlyTaller = new Player();
        slightlyTaller.setId(2L);
        slightlyTaller.setName("Slightly Taller Player");
        slightlyTaller.setHeight(180.1);
        slightlyTaller.setSchool(mockSchool);
        
        List<Player> tallerPlayers = List.of(slightlyTaller);

        when(playerRepository.findByHeightGreaterThan(180.0)).thenReturn(tallerPlayers);

        List<PlayerDTO> result = playerService.findByHeightGreaterThan(180.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Slightly Taller Player", result.getFirst().name());
        verify(playerRepository).findByHeightGreaterThan(180.0);
    }

    @Test
    @DisplayName("Find by height less than - Boundary test")
    void findByHeightLessThan_BoundaryTest() {
        Player exactHeight = new Player();
        exactHeight.setId(1L);
        exactHeight.setName("Exact Height Player");
        exactHeight.setHeight(180.0);
        exactHeight.setSchool(mockSchool);

        Player slightlyShorter = new Player();
        slightlyShorter.setId(2L);
        slightlyShorter.setName("Slightly Shorter Player");
        slightlyShorter.setHeight(179.9);
        slightlyShorter.setSchool(mockSchool);
        
        List<Player> shorterPlayers = List.of(slightlyShorter);

        when(playerRepository.findByHeightLessThan(180.0)).thenReturn(shorterPlayers);

        List<PlayerDTO> result = playerService.findByHeightLessThan(180.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Slightly Shorter Player", result.getFirst().name());
        verify(playerRepository).findByHeightLessThan(180.0);
    }
}