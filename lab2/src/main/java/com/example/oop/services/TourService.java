package com.example.oop.services;

import com.example.oop.dto.response.TourResponse;
import com.example.oop.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;

    public List<TourResponse> findAll() {
        return tourRepository.findByOrderById().stream()
                .map(entity ->
                        TourResponse.builder()
                                .id(entity.getId())
                                .name(entity.getName())
                                .price(entity.getPrice())
                                .count(entity.getCount())
                                .build())
                .toList();
    }
}
