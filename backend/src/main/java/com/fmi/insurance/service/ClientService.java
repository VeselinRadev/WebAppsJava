package com.fmi.insurance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.ClientDto;
import com.fmi.insurance.dto.ClientPatchDto;
import com.fmi.insurance.model.Address;
import com.fmi.insurance.model.Client;
import com.fmi.insurance.repository.ClientRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientDto createClient(ClientDto request) {
        if (clientRepository.existsById(request.ucn())) {
            throw new IllegalArgumentException("Client with this UCN already exists");
        }

        Address address = request.address() != null ? Address.builder()
                .city(request.address().city())
                .street(request.address().street())
                .postalCode(request.address().postalCode())
                .build() : null;

        Client client = Client.builder()
                .ucn(request.ucn())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .address(address)
                .experienceYears(request.experienceYears())
                .build();

        clientRepository.save(client);
        return ClientDto.fromEntity(client);
    }

    public List<ClientDto> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(ClientDto::fromEntity)
                .toList();
    }

    public ClientDto getClientByUcn(String ucn) {
        Client client = clientRepository.findById(ucn)
                .orElseThrow(() -> new IllegalArgumentException("Client with this UCN does not exist"));

        return ClientDto.fromEntity(client);
    }

    public void deleteClientByUcn(String ucn) {
        if (!clientRepository.existsById(ucn)) {
            throw new IllegalArgumentException("Client with this UCN does not exist");
        }

        clientRepository.deleteById(ucn);
    }

    public ClientDto updateClientByUcn(String ucn, ClientPatchDto request) {
        Client client = clientRepository.findById(ucn)
                .orElseThrow(() -> new IllegalArgumentException("Client with this UCN does not exist"));

        Optional.ofNullable(request.ucn()).ifPresent(client::setUcn);
        Optional.ofNullable(request.firstName()).ifPresent(client::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(client::setLastName);
        Optional.ofNullable(request.email()).ifPresent(client::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(client::setPhoneNumber);
        Optional.ofNullable(request.address())
                .ifPresent(address -> {
                    Address newAddress = Address.builder()
                        .city(address.city())
                        .street(address.street())
                        .postalCode(address.postalCode())
                        .build();
                        client.setAddress(newAddress);
                    });

        Optional.ofNullable(request.experienceYears()).ifPresent(client::setExperienceYears);

        clientRepository.save(client);
        return ClientDto.fromEntity(client);
    }
}
