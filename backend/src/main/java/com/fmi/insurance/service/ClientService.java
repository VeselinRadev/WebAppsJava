package com.fmi.insurance.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.ClientRequestDto;
import com.fmi.insurance.dto.ClientResponseDto;
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

    public ClientResponseDto createClient(ClientRequestDto request) {
        if (clientRepository.existsByUcn(request.ucn())) {
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
        return ClientResponseDto.fromEntity(client);
    }

    public List<ClientResponseDto> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(ClientResponseDto::fromEntity)
                .toList();
    }

    public ClientResponseDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with this ID does not exist"));

        return ClientResponseDto.fromEntity(client);
    }

    public ClientResponseDto getClientByUcn(String ucn) {
        Client client = clientRepository.findByUcn(ucn)
                .orElseThrow(() -> new IllegalArgumentException("Client with this UCN does not exist"));

        return ClientResponseDto.fromEntity(client);
    }

    public void deleteClientById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client with this ID does not exist");
        }

        clientRepository.deleteById(id);
    }

    public void deleteClientByUcn(String ucn) {
        if (!clientRepository.existsByUcn(ucn)) {
            throw new IllegalArgumentException("Client with this UCN does not exist");
        }

        clientRepository.deleteByUcn(ucn);
    }

    private Client findClientBy(String identifier, Function<String, Optional<Client>> finder, String notFoundMsg) {
        return finder.apply(identifier)
                .orElseThrow(() -> new IllegalArgumentException(notFoundMsg));
    }

    private void updateClient(Client client, ClientPatchDto request) {
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
    }

    public ClientResponseDto updateClientById(Long id, ClientPatchDto request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with this ID does not exist"));
        updateClient(client, request);
        clientRepository.save(client);
        return ClientResponseDto.fromEntity(client);
    }

    public ClientResponseDto updateClientByUcn(String ucn, ClientPatchDto request) {
        Client client = findClientBy(ucn, clientRepository::findByUcn, "Client with this UCN does not exist");
        updateClient(client, request);
        clientRepository.save(client);
        return ClientResponseDto.fromEntity(client);
    }

    Client getClientByIdInternal(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with this ID does not exist"));
    }

    Client getClientByUcnInternal(String ucn) {
        return clientRepository.findByUcn(ucn)
                .orElseThrow(() -> new IllegalArgumentException("Client with this UCN does not exist"));
    }
}
