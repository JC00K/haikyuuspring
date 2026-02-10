package com.example.haikyuuspring.services;

import com.example.haikyuuspring.controller.dto.AlumniDTO;
import com.example.haikyuuspring.controller.dto.FanDTO;
import com.example.haikyuuspring.exception.ResourceDuplicateException;
import com.example.haikyuuspring.exception.ResourceNotFoundException;
import com.example.haikyuuspring.model.entity.Fan;
import com.example.haikyuuspring.model.entity.School;
import com.example.haikyuuspring.repository.CharacterRepository;
import com.example.haikyuuspring.repository.FanRepository;
import com.example.haikyuuspring.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FanService {
    private final FanRepository fanRepository;
    private final CharacterRepository characterRepository;
    private final SchoolRepository schoolRepository;

    @Transactional
    public FanDTO createFan(FanDTO fanInfo) {
        if (characterRepository.existsByName(fanInfo.name())) {
            throw new ResourceDuplicateException(fanInfo.name());
        }
        Fan fan = new Fan();

        fan.setName(fanInfo.name());
        fan.setAge(fanInfo.age());
        fan.setImgUrl(fanInfo.imgUrl());
        fan.setHeight(fanInfo.height());
        fan.setRole(fanInfo.role());


        if (fanInfo.schoolId() != null) {
            School school = schoolRepository.findById(fanInfo.schoolId()).orElseThrow(() -> new ResourceNotFoundException(fanInfo.schoolId()));
            fan.setSchool(school);
        }

        Fan newFan = fanRepository.save(fan);

        return convertFanToDTO(newFan);
    }

    public List<FanDTO> findAllFans() {
        return mapFanListToDTO(fanRepository.findAll());
    }

    public FanDTO findById(Long id) {
        return fanRepository.findById(id)
                .map(this::convertFanToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }


    public List<FanDTO> mapFanListToDTO(List<Fan> fan) {
        return fan.stream()
                .map(this::convertFanToDTO)
                .toList();
    }

    private FanDTO convertFanToDTO(Fan fan) {


        return new FanDTO(
                fan.getId(),
                fan.getName(),
                fan.getHeight(),
                fan.getAge(),
                fan.getRole(),
                Optional.ofNullable(fan.getSchool()).map(School::getId).orElse(null),
                Optional.ofNullable(fan.getSchool()).map(School::getName).orElse(null),
                fan.getImgUrl()
        );
    }
}