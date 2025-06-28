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

import com.fmi.insurance.dto.ClientRequestDto;
import com.fmi.insurance.dto.ClientResponseDto;
import com.fmi.insurance.dto.ClientPatchDto;
import com.fmi.insurance.service.ClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody ClientRequestDto request) {
        ClientResponseDto createdClient = clientService.createClient(request);
        return ResponseEntity.ok(createdClient);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getClients() {
        List<ClientResponseDto> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<ClientResponseDto> getClientById(@PathVariable Long id) {
    //     ClientResponseDto client = clientService.getClientById(id);
    //     return ResponseEntity.ok(client);
    // }

    @GetMapping("/insurance/{insuranceId}")
    public ResponseEntity<ClientResponseDto> getClientByInsuranceId(@PathVariable Long insuranceId) {
        ClientResponseDto client = clientService.getClientByInsuranceId(insuranceId);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{ucn}")
    public ResponseEntity<ClientResponseDto> getClientByUcn(@PathVariable String ucn) {
        ClientResponseDto client = clientService.getClientByUcn(ucn);
        return ResponseEntity.ok(client);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteClientById(@PathVariable Long id) {
//        clientService.deleteClientById(id);
//        return ResponseEntity.noContent().build();
//    }

    @DeleteMapping("/{ucn}")
    public ResponseEntity<Void> deleteClientByUcn(@PathVariable String ucn) {
        clientService.deleteClientByUcn(ucn);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<ClientResponseDto> updateClientById(@PathVariable Long id, @Valid @RequestBody ClientPatchDto request) {
//        ClientResponseDto updatedClient = clientService.updateClientById(id, request);
//        return ResponseEntity.ok(updatedClient);
//    }

    @PatchMapping("/{ucn}")
    public ResponseEntity<ClientResponseDto> updateClientByUcn(@PathVariable String ucn, @Valid @RequestBody ClientPatchDto request) {
        ClientResponseDto updatedClient = clientService.updateClientByUcn(ucn, request);
        return ResponseEntity.ok(updatedClient);
    }

}
