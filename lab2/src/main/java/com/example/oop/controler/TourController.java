package com.example.oop.controler;

import com.example.oop.dto.response.TourResponse;
import com.example.oop.services.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @GetMapping("/servlet_war_exploded/tour")
    public List<TourResponse> getCurrentAdmin() {
        return tourService.findAll();
    }
}
