package com.example.haikyuuspring.controller;


import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.controller.dto.ManagementDTO;
import com.example.haikyuuspring.model.enums.ManagementRole;
import com.example.haikyuuspring.services.ManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping
    public ResponseEntity<List<ManagementDTO>> getAllManagement() {
        return ResponseEntity.ok(managementService.findAllManagement());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(managementService.findById(id));
    }

    @GetMapping("/get_by_school_id/{schoolId}")
    public ResponseEntity<List<ManagementDTO>> getBySchoolId(@PathVariable Long schoolId) {
        return ResponseEntity.ok(managementService.findBySchoolId(schoolId));
    }

    @GetMapping("/get_by_management_role/{role}")
    public ResponseEntity<List<ManagementDTO>> getByManagementRole(@PathVariable ManagementRole role) {
        return ResponseEntity.ok(managementService.findByManagementRole(role));
    }

    @PostMapping
    public ResponseEntity<ManagementDTO> createManagement(@Valid @RequestBody ManagementDTO managementDTO) {
        return new ResponseEntity<>(managementService.createManagement(managementDTO), HttpStatus.CREATED);
    }
}
