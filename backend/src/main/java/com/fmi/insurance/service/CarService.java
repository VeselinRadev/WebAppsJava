package com.fmi.insurance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.CarDto;
import com.fmi.insurance.dto.CarPatchDto;
import com.fmi.insurance.model.Car;
import com.fmi.insurance.repository.CarRepository;
import com.fmi.insurance.vo.EnumUtils;
import com.fmi.insurance.vo.FuelType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final CarRepository carRepository;

    public CarDto createCar(CarDto request) {
        if (carRepository.existsById(request.plate())) {
            throw new IllegalArgumentException("Car with this plate already exists");
        }

        Car car = Car.builder()
                .plate(request.plate())
                .vin(request.vin())
                .make(request.make())
                .model(request.model())
                .year(request.year())
                .volume(request.volume())
                .power(request.power())
                .seats(request.seats())
                .fuelType(request.fuelType() != null ? FuelType.valueOf(request.fuelType()) : null)
                .build();
        carRepository.save(car);
        return CarDto.fromEntity(car);
    }

    public List<CarDto> getCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    public CarDto getCarByPlate(String plate) {
        Car car = carRepository.findById(plate)
                .orElseThrow(() -> new IllegalArgumentException("Car with this plate does not exist"));
        return CarDto.fromEntity(car);
    }

    public CarDto getCarByVin(String vin) {
        Car car = carRepository.findByVin(vin)
                .orElseThrow(() -> new IllegalArgumentException("Car with this VIN does not exist"));
        return CarDto.fromEntity(car);
    }

    public void deleteCarByPlate(String plate) {
        if (!carRepository.existsById(plate)) {
            throw new IllegalArgumentException("Car with this plate does not exist");
        }
        carRepository.deleteById(plate);
    }

    public void deleteCarByVin(String vin) {
        Car car = carRepository.findByVin(vin)
                .orElseThrow(() -> new IllegalArgumentException("Car with this VIN does not exist"));
        carRepository.delete(car);
    }

    public CarDto updateCarByPlate(String plate, CarPatchDto request) {
        Car car = carRepository.findById(plate)
                .orElseThrow(() -> new IllegalArgumentException("Car with this plate does not exist"));

        // insuranceService.getActiveInsuranceByCarPlate(plate) - if insurance is active, we should not allow updates

        Optional.ofNullable(request.plate()).ifPresent(car::setPlate);
        Optional.ofNullable(request.vin()).ifPresent(car::setVin);
        Optional.ofNullable(request.make()).ifPresent(car::setMake);
        Optional.ofNullable(request.model()).ifPresent(car::setModel);
        Optional.ofNullable(request.year()).ifPresent(car::setYear);
        Optional.ofNullable(request.volume()).ifPresent(car::setVolume);
        Optional.ofNullable(request.power()).ifPresent(car::setPower);
        Optional.ofNullable(request.seats()).ifPresent(car::setSeats);
        Optional.ofNullable(request.fuelType())
                .ifPresent(fuelType -> car.setFuelType(EnumUtils.fromString(FuelType.class, fuelType)));

        carRepository.save(car);
        return CarDto.fromEntity(car);
    }

    public CarDto updateCarByVin(String vin, CarPatchDto request) {
        Car car = carRepository.findById(vin)
                .orElseThrow(() -> new IllegalArgumentException("Car with this vin does not exist"));

        // insuranceService.getActiveInsuranceByCarPlate(plate) - if insurance is active, we should not allow updates

        Optional.ofNullable(request.plate()).ifPresent(car::setPlate);
        Optional.ofNullable(request.vin()).ifPresent(car::setVin);
        Optional.ofNullable(request.make()).ifPresent(car::setMake);
        Optional.ofNullable(request.model()).ifPresent(car::setModel);
        Optional.ofNullable(request.year()).ifPresent(car::setYear);
        Optional.ofNullable(request.volume()).ifPresent(car::setVolume);
        Optional.ofNullable(request.power()).ifPresent(car::setPower);
        Optional.ofNullable(request.seats()).ifPresent(car::setSeats);
        Optional.ofNullable(request.fuelType())
                .ifPresent(fuelType -> car.setFuelType(EnumUtils.fromString(FuelType.class, fuelType)));

        carRepository.save(car);
        return CarDto.fromEntity(car);
    }
}
