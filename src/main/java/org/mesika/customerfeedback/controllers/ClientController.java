package org.mesika.customerfeedback.controllers;

import java.util.List;
import java.util.UUID;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.DefaultDTO;
import org.mesika.customerfeedback.dto.client.ClientDTO;
import org.mesika.customerfeedback.dto.client.ClientIdentityProviderDTO;
import org.mesika.customerfeedback.dto.client.ModuleDTO;
import org.mesika.customerfeedback.services.client.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Clients")
public class ClientController {
    private final ClientService clientService;

    // TODO: Validate source to be url
    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientDTO>> listClients() {
        return ResponseEntity.ok(clientService.listClients());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DefaultDTO> deleteClient(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.deleteClient(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable UUID id, @RequestBody ClientDTO client) {
        return ResponseEntity.ok(clientService.updateClient(id, client));
    }

    @GetMapping("/identity-provider/list")
    public ResponseEntity<CustomPage<ClientIdentityProviderDTO>> listClientIDPs(
            @RequestParam(name = "client_id", required = false) UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(clientService
                .listClientIDPs(clientId, page, pageSize));

    }

    @PostMapping("/identity-provider/create/{client_id}")
    public ResponseEntity<ClientIdentityProviderDTO> createIDP(@PathVariable(name = "client_id") UUID clientId,
            @RequestBody ClientIdentityProviderDTO identityProvider) {
        return ResponseEntity.ok(clientService.createIDP(clientId, identityProvider));
    }

    @PutMapping("/identity-provider/update/{id}")
    public ResponseEntity<ClientIdentityProviderDTO> updateIDP(@PathVariable UUID id,
            @RequestBody ClientIdentityProviderDTO identityProvider) {
        return ResponseEntity.ok(clientService.updateIDP(id, identityProvider));
    }

    @DeleteMapping("/identity-provider/delete/{id}")
    public ResponseEntity<DefaultDTO> deleteIDP(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.deleteIDP(id));
    }

    @GetMapping("/modules/list")
    public ResponseEntity<List<ModuleDTO>> listClientModules(
            @RequestParam(name = "client_id") UUID clientId) {
        return ResponseEntity.ok(clientService.listClientModules(clientId, null));

    }

    @PostMapping("/modules/create/{client_id}")
    public ResponseEntity<List<ModuleDTO>> createModules(@PathVariable(name = "client_id") UUID clientId,
            @RequestBody List<ModuleDTO> modules) {
        return ResponseEntity.ok(clientService.createModules(clientId, modules));
    }

    @PutMapping("/modules/update/{id}")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long id,
            @RequestBody ModuleDTO module) {
        return ResponseEntity.ok(clientService.updateModule(id, module));
    }

    @DeleteMapping("/modules/delete/{id}")
    public ResponseEntity<DefaultDTO> deleteModule(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.deleteModule(id));
    }
}
