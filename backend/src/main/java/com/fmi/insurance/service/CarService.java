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

    Double getCarPrice(Car car) {

        return 300 * getAgeMultiplier(car.getRegistrationYear())
                * getVolumeMultiplier(car.getVolume())
                * getPowerMultiplier(car.getPower())
                * getSeatsMultiplier(car.getSeats())
                * getFuelMultiplier(car.getFuelType());        
    }

    private double getAgeMultiplier(int age) {
        if (age <= 5) return 0.95;
        else if (age <= 10) return 1.00;
        else if (age <= 20) return 1.10;
        else return 1.20;
    }

    private double getVolumeMultiplier(int volume) {
        if (volume < 2000) return 0.90;
        else if (volume <= 2400) return 1.00;
        else if (volume <= 2700) return 1.10;
        else return 1.20;
    }

    private double getPowerMultiplier(int power) {
        if (power < 100) return 0.95;
        else if (power <= 150) return 1.00;
        else return 1.15;
    }

    private double getSeatsMultiplier(int seats) {
        if (seats <= 5) return 1.00;
        else if (seats <= 9) return 1.10;
        else return 1.25;
    }

    private double getFuelMultiplier(FuelType fuel) {
        switch (fuel) {
            case GASOLINE:
                return 1.00;
            case DIESEL:
                return 1.10;
            case LPG:
                return 0.95;
            case HYBRID:
                return 0.90;
            case ELECTRIC:
                return 0.85;
            default:
                return 1.00;
        }
    }
}
