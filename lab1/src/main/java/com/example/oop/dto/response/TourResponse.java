package com.example.oop.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TourResponse {
    private Long id;
    private String name;
    private Long price;
    private Long count;
}
