package com.example.haikyuuspring.controller;



import com.example.haikyuuspring.controller.dto.CharacterDTO;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        return ResponseEntity.ok(characterService.findAllCharacters());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.findCharacterById(id));
    }

    @GetMapping("/age/{age}")
    public ResponseEntity <List<CharacterDTO>> getCharactersByAge(@PathVariable int age) {
        return ResponseEntity.ok(characterService.findByAge(age));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity <List<CharacterDTO>> getCharactersByYear(@PathVariable Year year) {
        return ResponseEntity.ok(characterService.findCharacterByYear(year));
    }

    @GetMapping("/roles/{role}")
    public ResponseEntity<List<CharacterDTO>> getCharactersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(characterService.findByRole(role));
    }

//    @GetMapping("/positions/{position}")
//    public ResponseEntity<List<CharacterDTO>> getCharactersByPosition(@PathVariable Position position) {
//        return ResponseEntity.ok(haikyuuCharacterService.findByPosition(position));
//    }

    @GetMapping("/greater_than_{height}")
    public ResponseEntity<List<CharacterDTO>> getCharactersByHeightGreaterThan(@PathVariable Double height) {
        return ResponseEntity.ok(characterService.findByHeightGreaterThan(height));
    }

    @GetMapping("/less_than_{height}")
    public ResponseEntity<List<CharacterDTO>> getCharactersByHeightLessThan(@PathVariable Double height) {
        return ResponseEntity.ok(characterService.findByHeightLessThan(height));
    }

//    @GetMapping("/search/position")
//    public ResponseEntity<List<CharacterDTO>> getCharactersByYearAndPosition(@RequestParam(required = false) Year year, @RequestParam(required = false) Position position) {
//        return ResponseEntity.ok(haikyuuCharacterService.findByYearAndPosition(year, position));
//    }

    @GetMapping("/search/role")
    public ResponseEntity<List<CharacterDTO>> getCharactersByYearAndRole(@RequestParam(required = false) Year year, @RequestParam(required = false) Role role) {
        return ResponseEntity.ok(characterService.findByYearAndRole(year, role));
    }

//    @PostMapping
//    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
//        return new ResponseEntity<>(characterService.createCharacter(characterDTO), HttpStatus.CREATED);
//    }

    @PatchMapping("/assign_year/{id}")
    public ResponseEntity<CharacterDTO> assignYearToCharacter(Long id, Year year) {
        return new ResponseEntity<>(characterService.assignYearToCharacter(id, year), HttpStatus.OK);
    }

    @PatchMapping("/assign_school/{id}")
    public ResponseEntity<CharacterDTO> assignSchoolToCharacter(Long id, String school) {
        return new ResponseEntity<>(characterService.assignSchoolToCharacter(id, school), HttpStatus.OK);
    }

    @PatchMapping("/assign_age/{id}")
    public ResponseEntity<CharacterDTO> assignAgeToCharacter(Long id, Integer age) {
        return new ResponseEntity<>(characterService.assignAgeToCharacter(id, age), HttpStatus.OK);
    }


}
