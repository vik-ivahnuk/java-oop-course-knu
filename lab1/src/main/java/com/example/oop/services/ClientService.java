package com.example.oop.services;

import com.example.oop.entities.ClientEntity;
import com.example.oop.entities.TourEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.oop.dto.request.OrderTourRequest;
import com.example.oop.dto.response.CurrentClientResponse;
import com.example.oop.repositories.ClientRepository;
import com.example.oop.repositories.TourRepository;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class ClientService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final ClientRepository clientRepository = new ClientRepository();
    private final TourRepository tourRepository = new TourRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CurrentClientResponse getCurrentUser(HttpServletRequest request) {
        String username = authorizationService.getUsername(request);
        ClientEntity client = clientRepository.findByUsername(username);
        return CurrentClientResponse.builder()
                .id(client.getId())
                .username(client.getUsername())
                .amount(client.getAmount())
                .build();
    }

    @SneakyThrows
    public void order(HttpServletRequest request) {
        OrderTourRequest body = objectMapper.readValue(
                request.getReader().lines().collect(Collectors.joining(System.lineSeparator())),
                OrderTourRequest.class);
        String username = authorizationService.getUsername(request);
        ClientEntity client = clientRepository.findByUsername(username);
        TourEntity tourEntity = tourRepository.findById(body.getId());
        tourRepository.updateCount(tourEntity.getId(), tourEntity.getCount() - 1);
        clientRepository.updateAmount(client.getId(), client.getAmount() - tourEntity.getPrice());
    }
}
