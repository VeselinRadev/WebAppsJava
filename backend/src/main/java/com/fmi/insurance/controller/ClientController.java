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
    public ResponseEntity<ClientRequestDto> createClient(@Valid @RequestBody ClientRequestDto request) {
        ClientRequestDto createdClient = clientService.createClient(request);
        return ResponseEntity.ok(createdClient);
    }

    @GetMapping
    public ResponseEntity<List<ClientRequestDto>> getClients() {
        List<ClientRequestDto> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{ucn}")
    public ResponseEntity<ClientRequestDto> getClientByUcn(@PathVariable String ucn) {
        ClientRequestDto client = clientService.getClientByUcn(ucn);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{ucn}")
    public ResponseEntity<Void> deleteClientByUcn(@PathVariable String ucn) {
        clientService.deleteClientByUcn(ucn);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{ucn}")
    public ResponseEntity<ClientRequestDto> updateClientByUcn(@PathVariable String ucn, @Valid @RequestBody ClientPatchDto request) {
        ClientRequestDto updatedClient = clientService.updateClientByUcn(ucn, request);
        return ResponseEntity.ok(updatedClient);
    }

}
