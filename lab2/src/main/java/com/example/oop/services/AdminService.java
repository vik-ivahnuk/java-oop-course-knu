package com.example.oop.services;

import com.example.oop.dto.request.AddTourRequest;
import com.example.oop.dto.response.AdminResponse;
import com.example.oop.entity.Admin;
import com.example.oop.entity.Tour;
import com.example.oop.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AuthorizationService authorizationService;
    private final TourRepository tourRepository;

    public AdminResponse getAdmin() {
        Admin adminEntity = authorizationService.getCurrentAdmin();
        return AdminResponse.builder()
                .id(adminEntity.getId())
                .username(adminEntity.getUsername())
                .build();
    }

    @SneakyThrows
    public void refresh(AddTourRequest request) {
        Tour tourEntity = tourRepository.getById(request.getId());
        tourEntity.setCount(tourEntity.getCount() + 1);
        tourRepository.save(tourEntity);
    }
}
