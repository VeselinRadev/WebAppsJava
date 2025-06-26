package com.fmi.insurance.controller;

import java.util.List;

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
    public ResponseEntity<CarRequestDto> createCar(@Valid @RequestBody CarRequestDto request) {
        CarRequestDto createdCar = carService.createCar(request);
        return ResponseEntity.ok(createdCar);
    }

    @GetMapping
    public ResponseEntity<List<CarRequestDto>> getCars() {
        List<CarRequestDto> cars = carService.getCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{plate}")
    public ResponseEntity<CarRequestDto> getCarByPlate(@PathVariable String plate) {
        CarRequestDto car = carService.getCarByPlate(plate);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<CarRequestDto> getCarByVin(@PathVariable String vin) {
        CarRequestDto car = carService.getCarByVin(vin);
        return ResponseEntity.ok(car);
    }

     @DeleteMapping("/{plate}")
    public ResponseEntity<Void> deleteCarByPlate(@PathVariable String plate) {
        carService.deleteCarByPlate(plate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{vin}")
    public ResponseEntity<Void> deleteCarByVin(@PathVariable String vin) {
        carService.deleteCarByVin(vin);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{plate}")
    public ResponseEntity<CarRequestDto> updateCarByPlate(@PathVariable String plate, @Valid @RequestBody CarPatchDto request) {
        CarRequestDto updatedCar = carService.updateCarByPlate(plate, request);
        return ResponseEntity.ok(updatedCar);
    }

    @PatchMapping("/{vin}")
    public ResponseEntity<CarRequestDto> updateCarByVin(@PathVariable String vin, @Valid @RequestBody CarPatchDto request) {
        CarRequestDto updatedCar = carService.updateCarByVin(vin, request);
        return ResponseEntity.ok(updatedCar);
    }
}