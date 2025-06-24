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

import com.fmi.insurance.dto.CarDto;
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
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CarDto request) {
        CarDto createdCar = carService.createCar(request);
        return ResponseEntity.ok(createdCar);
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getCars() {
        List<CarDto> cars = carService.getCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{plate}")
    public ResponseEntity<CarDto> getCarByPlate(@PathVariable String plate) {
        CarDto car = carService.getCarByPlate(plate);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/{vin}")
    public ResponseEntity<CarDto> getCarByVin(@PathVariable String vin) {
        CarDto car = carService.getCarByVin(vin);
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
    public ResponseEntity<CarDto> updateCarByPlate(@PathVariable String plate, @Valid @RequestBody CarPatchDto request) {
        CarDto updatedCar = carService.updateCarByPlate(plate, request);
        return ResponseEntity.ok(updatedCar);
    }

    @PatchMapping("/{vin}")
    public ResponseEntity<CarDto> updateCarByVin(@PathVariable String vin, @Valid @RequestBody CarPatchDto request) {
        CarDto updatedCar = carService.updateCarByVin(vin, request);
        return ResponseEntity.ok(updatedCar);
    }
}