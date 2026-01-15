package com.example.haikyuuspring.controller;



import com.example.haikyuuspring.controller.dto.HaikyuuCharacterDTO;
import com.example.haikyuuspring.model.enums.Position;
import com.example.haikyuuspring.model.enums.Role;
import com.example.haikyuuspring.model.enums.Year;
import com.example.haikyuuspring.services.HaikyuuCharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/characters")
@RequiredArgsConstructor
public class HaikyuuCharacterController {
    private final HaikyuuCharacterService haikyuuCharacterService;

    @GetMapping
    public ResponseEntity<List<HaikyuuCharacterDTO>> getAllCharacters() {
        return ResponseEntity.ok(haikyuuCharacterService.findAllCharacters());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<HaikyuuCharacterDTO> getCharacterById(@PathVariable Long id) {
        return ResponseEntity.ok(haikyuuCharacterService.findCharacterById(id));
    }

    @GetMapping("/age/{age}")
    public ResponseEntity <List<HaikyuuCharacterDTO>> getCharactersByAge(@PathVariable int age) {
        return ResponseEntity.ok(haikyuuCharacterService.findByAge(age));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity <List<HaikyuuCharacterDTO>> getCharactersByYear(@PathVariable Year year) {
        return ResponseEntity.ok(haikyuuCharacterService.findCharacterByYear(year));
    }

    @GetMapping("/roles/{role}")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(haikyuuCharacterService.findByRole(role));
    }

    @GetMapping("/positions/{position}")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByPosition(@PathVariable Position position) {
        return ResponseEntity.ok(haikyuuCharacterService.findByPosition(position));
    }

    @GetMapping("/greater_than_{height}")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByHeightGreaterThan(@PathVariable Double height) {
        return ResponseEntity.ok(haikyuuCharacterService.findByHeightGreaterThan(height));
    }

    @GetMapping("/less_than_{height}")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByHeightLessThan(@PathVariable Double height) {
        return ResponseEntity.ok(haikyuuCharacterService.findByHeightLessThan(height));
    }

    @GetMapping("/search/position")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByYearAndPosition(@RequestParam(required = false) Year year, @RequestParam(required = false) Position position) {
        return ResponseEntity.ok(haikyuuCharacterService.findByYearAndPosition(year, position));
    }

    @GetMapping("/search/role")
    public ResponseEntity<List<HaikyuuCharacterDTO>> getCharactersByYearAndRole(@RequestParam(required = false) Year year, @RequestParam(required = false) Role role) {
        return ResponseEntity.ok(haikyuuCharacterService.findByYearAndRole(year, role));
    }

    @PostMapping
    public ResponseEntity<HaikyuuCharacterDTO> createCharacter(@Valid @RequestBody HaikyuuCharacterDTO characterDTO) {
        return new ResponseEntity<>(haikyuuCharacterService.createCharacter(characterDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/assign_year/{id}")
    public ResponseEntity<HaikyuuCharacterDTO> assignYearToCharacter(Long id, Year year) {
        return new ResponseEntity<>(haikyuuCharacterService.assignYearToCharacter(id, year), HttpStatus.OK);
    }

    @PatchMapping("/assign_school/{id}")
    public ResponseEntity<HaikyuuCharacterDTO> assignSchoolToCharacter(Long id, String school) {
        return new ResponseEntity<>(haikyuuCharacterService.assignSchoolToCharacter(id, school), HttpStatus.OK);
    }

    @PatchMapping("/assign_age/{id}")
    public ResponseEntity<HaikyuuCharacterDTO> assignAgeToCharacter(Long id, Integer age) {
        return new ResponseEntity<>(haikyuuCharacterService.assignAgeToCharacter(id, age), HttpStatus.OK);
    }


}
