package com.fmi.insurance.controller;

import com.fmi.insurance.dto.TestDTO;
import com.fmi.insurance.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService service;

    @GetMapping
    public List<TestDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public TestDTO create(@RequestBody TestDTO dto) {
        return service.save(dto);
    }
}