package com.fmi.insurance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.fmi.insurance.model.Client;
import com.fmi.insurance.repository.ClientRepository;
import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.CarRequestDto;
import com.fmi.insurance.dto.CarResponseDto;
import com.fmi.insurance.dto.CarPatchDto;
import com.fmi.insurance.model.Car;
import com.fmi.insurance.repository.CarRepository;
import com.fmi.insurance.vo.EnumUtils;
import com.fmi.insurance.vo.FuelType;
import com.fmi.insurance.vo.InsuranceStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public CarResponseDto createCar(CarRequestDto request) {
        if (carRepository.existsByPlate(request.plate())) {
            throw new IllegalArgumentException("Car with this plate already exists");
        }
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with this ID does not exist"));


        Car car = Car.builder()
                .plate(request.plate())
                .vin(request.vin())
                .make(request.make())
                .model(request.model())
                .year(request.year())
                .volume(request.volume())
                .power(request.power())
                .seats(request.seats())
                .registrationYear(request.registrationYear())
                .fuelType(request.fuelType() != null ? FuelType.valueOf(request.fuelType()) : null)
                .client(client)
                .build();

        carRepository.save(car);
        return CarResponseDto.fromEntity(car);
    }

    public List<CarResponseDto> getCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(CarResponseDto::fromEntity)
                .toList();
    }


    public CarResponseDto getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with this ID does not exist"));
        return CarResponseDto.fromEntity(car);
    }

    public CarResponseDto getCarByInsuranceId(Long id) {
        Car car = carRepository.findByInsurances_Id(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with this ID does not exist"));
        return CarResponseDto.fromEntity(car);
    }

    public CarResponseDto getCarByPlate(String plate) {
        Car car = carRepository.findByPlate(plate)
                .orElseThrow(() -> new IllegalArgumentException("Car with this plate does not exist"));
        return CarResponseDto.fromEntity(car);
    }

    public CarResponseDto getCarByVin(String vin) {
        Car car = carRepository.findByVin(vin)
                .orElseThrow(() -> new IllegalArgumentException("Car with this VIN does not exist"));
        return CarResponseDto.fromEntity(car);
    }


    public void deleteCarById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new IllegalArgumentException("Car with this ID does not exist");
        }
        carRepository.deleteById(id);
    }

    public void deleteCarByPlate(String plate) {
        if (!carRepository.existsByPlate(plate)) {
            throw new IllegalArgumentException("Car with this plate does not exist");
        }
        carRepository.deleteByPlate(plate);
    }

    public void deleteCarByVin(String vin) {
        Car car = carRepository.findByVin(vin)
                .orElseThrow(() -> new IllegalArgumentException("Car with this VIN does not exist"));
        carRepository.delete(car);
    }


    private Car findCarBy(String identifier, Function<String, Optional<Car>> finder, String notFoundMsg) {
        return finder.apply(identifier)
                .orElseThrow(() -> new IllegalArgumentException(notFoundMsg));
    }

    private void validateAndUpdateCar(Car car, CarPatchDto request) {
        boolean hasActiveInsurance = car.getInsurances().stream()
                .anyMatch(insurance -> insurance.getStatus() == InsuranceStatus.ACTIVE
                        || insurance.getStatus() == InsuranceStatus.INCOMING);

        if (hasActiveInsurance) {
            throw new IllegalArgumentException("Cannot update car with active or incoming insurance");
        }

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
    }

    public CarResponseDto updateCarById(Long id, CarPatchDto request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with this ID does not exist"));
        validateAndUpdateCar(car, request);
        carRepository.save(car);
        return CarResponseDto.fromEntity(car);
    }

    public CarResponseDto updateCarByPlate(String plate, CarPatchDto request) {
        Car car = findCarBy(plate, carRepository::findByPlate, "Car with this plate does not exist");
        validateAndUpdateCar(car, request);
        carRepository.save(car);
        return CarResponseDto.fromEntity(car);
    }

    public CarResponseDto updateCarByVin(String vin, CarPatchDto request) {
        Car car = findCarBy(vin, carRepository::findByVin, "Car with this VIN does not exist");
        validateAndUpdateCar(car, request);
        carRepository.save(car);
        return CarResponseDto.fromEntity(car);
    }


    Car getCarByIdInternal(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with this id does not exist"));
    }

    Car getCarByPlateInternal(String plate) {
        return carRepository.findByPlate(plate)
                .orElseThrow(() -> new IllegalArgumentException("Car with this plate does not exist"));
    }

    Double getCarPrice(Car car) {

        return 300 * getAgeMultiplier(LocalDate.now().getYear() - car.getRegistrationYear())
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
