package com.example.haikyuuspring.controller;

import com.example.haikyuuspring.controller.dto.FanDTO;
import com.example.haikyuuspring.services.FanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fans")
@RequiredArgsConstructor
public class FanController {
    private final FanService fanService;

    @GetMapping
    public ResponseEntity<List<FanDTO>> getAlLFans() {
        return ResponseEntity.ok(fanService.findAllFans());
    }

    @PostMapping
    public ResponseEntity<FanDTO> createFan(@Valid @RequestBody FanDTO fanDTO) {
        return new ResponseEntity<>(fanService.createFan(fanDTO), HttpStatus.OK);
    }
}
