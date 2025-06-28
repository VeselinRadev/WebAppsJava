package com.fmi.insurance.controller;

import java.util.List;

import com.fmi.insurance.dto.ClientResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmi.insurance.dto.CarRequestDto;
import com.fmi.insurance.dto.CarResponseDto;
import com.fmi.insurance.dto.CarPatchDto;
import com.fmi.insurance.service.CarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@Valid @RequestBody CarRequestDto request) {
        CarResponseDto createdCar = carService.createCar(request);
        return ResponseEntity.ok(createdCar);
    }

    @GetMapping
    public ResponseEntity<List<CarResponseDto>> getCars() {
        List<CarResponseDto> cars = carService.getCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        CarResponseDto car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/insurance/{insuranceId}")
    public ResponseEntity<CarResponseDto> getClientByInsuranceId(@PathVariable Long insuranceId) {
        CarResponseDto car = carService.getCarByInsuranceId(insuranceId);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/{plate}")
    public ResponseEntity<CarResponseDto> getCarByPlate(@PathVariable String plate) {
        CarResponseDto car = carService.getCarByPlate(plate);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<CarResponseDto> getCarByVin(@PathVariable String vin) {
        CarResponseDto car = carService.getCarByVin(vin);
        return ResponseEntity.ok(car);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCarById(@PathVariable Long id) {
//        carService.deleteCarById(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{plate}")
    public ResponseEntity<Void> deleteCarByPlate(@PathVariable String plate) {
        carService.deleteCarByPlate(plate);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{vin}")
//    public ResponseEntity<Void> deleteCarByVin(@PathVariable String vin) {
//        carService.deleteCarByVin(vin);
//        return ResponseEntity.noContent().build();
//    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<CarResponseDto> updateCarById(@PathVariable Long id, @Valid @RequestBody CarPatchDto request) {
//        CarResponseDto updatedCar = carService.updateCarById(id, request);
//        return ResponseEntity.ok(updatedCar);
//    }

    @PatchMapping("/{plate}")
    public ResponseEntity<CarResponseDto> updateCarByPlate(@PathVariable String plate, @Valid @RequestBody CarPatchDto request) {
        CarResponseDto updatedCar = carService.updateCarByPlate(plate, request);
        return ResponseEntity.ok(updatedCar);
    }

//    @PatchMapping("/{vin}")
//    public ResponseEntity<CarResponseDto> updateCarByVin(@PathVariable String vin, @Valid @RequestBody CarPatchDto request) {
//        CarResponseDto updatedCar = carService.updateCarByVin(vin, request);
//        return ResponseEntity.ok(updatedCar);
//    }
}