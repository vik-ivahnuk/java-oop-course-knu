package com.example.oop.services;

import com.example.oop.entities.AdminEntity;
import com.example.oop.entities.TourEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.oop.dto.request.AddTourRequest;
import com.example.oop.dto.response.CurrentAdminResponse;
import com.example.oop.repositories.AdminRepository;
import com.example.oop.repositories.ClientRepository;
import com.example.oop.repositories.TourRepository;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class AdminService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final AdminRepository adminRepository = new AdminRepository();
    private final TourRepository tourRepository = new TourRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CurrentAdminResponse getCurrentAdmin(HttpServletRequest request) {
        String username = authorizationService.getUsername(request);
        AdminEntity adminEntity = adminRepository.findByUsername(username);
        return CurrentAdminResponse.builder()
                .id(adminEntity.getId())
                .username(adminEntity.getUsername())
                .build();
    }

    @SneakyThrows
    public void refresh(HttpServletRequest request) {
        AddTourRequest body = objectMapper.readValue(
                request.getReader().lines().collect(Collectors.joining(System.lineSeparator())),
                AddTourRequest.class);
        TourEntity tourEntity = tourRepository.findById(body.getId());
        tourRepository.updateCount(tourEntity.getId(), tourEntity.getCount() + 1);
    }
}
