package com.fmi.insurance.service;

import com.fmi.insurance.dto.TestDTO;
import com.fmi.insurance.model.TestModel;
import com.fmi.insurance.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public List<TestDTO> getAll() {
        return testRepository.findAll().stream().map(test -> new TestDTO(test.getValue())).toList();
    }

    public TestDTO save(TestDTO dto) {
        var testModel = testRepository.save(TestModel.builder().value(dto.getValue()).build());
        return new TestDTO(testModel.getValue());
    }
}