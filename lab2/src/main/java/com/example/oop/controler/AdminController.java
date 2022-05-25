package com.example.oop.controler;

import com.example.oop.dto.request.AddTourRequest;
import com.example.oop.dto.response.AdminResponse;
import com.example.oop.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/servlet_war_exploded/current-admin")
    public AdminResponse getCurrentAdmin() {
        return adminService.getAdmin();
    }

    @PutMapping("/servlet_war_exploded/refresh")
    public void refresh(@RequestBody AddTourRequest request) {
        adminService.refresh(request);
    }
}
