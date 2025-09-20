package org.mesika.customerfeedback.services.client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mesika.customerfeedback.dto.CustomPage;
import org.mesika.customerfeedback.dto.DefaultDTO;
import org.mesika.customerfeedback.dto.client.ClientDTO;
import org.mesika.customerfeedback.dto.client.ClientIdentityProviderDTO;
import org.mesika.customerfeedback.dto.client.ModuleDTO;
import org.mesika.customerfeedback.models.clients.Client;
import org.mesika.customerfeedback.models.clients.ClientIdentityProvider;
import org.mesika.customerfeedback.models.clients.Module;
import org.mesika.customerfeedback.repo.ClientIdentityProviderRepository;
import org.mesika.customerfeedback.repo.ClientRepository;
import org.mesika.customerfeedback.repo.ModuleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final ClientIdentityProviderRepository idpRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public ClientDTO createClient(ClientDTO request) {
        Client client = modelMapper.map(request, Client.class);
        client = clientRepository.save(client);
        return modelMapper.map(client, ClientDTO.class);
    }

    public List<ClientDTO> listClients() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public DefaultDTO deleteClient(UUID id) {
        clientRepository.deleteById(id);
        return new DefaultDTO("Client deleted successfully");
    }

    @Transactional
    public ClientDTO updateClient(UUID id, ClientDTO request) {
        ClientDTO response = new ClientDTO();
        Optional<Client> clientOpt = clientRepository.findById(id);

        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client not found");
        }

        Client client = clientOpt.get();
        client.setName(request.getName());
        client.setSource(request.getSource());
        client.setDescription(request.getDescription());
        response = modelMapper.map(clientRepository.save(client),
                ClientDTO.class);

        return response;
    }

    public CustomPage<ClientIdentityProviderDTO> listClientIDPs(UUID clientId, int page, int pageSize) {
        CustomPage<ClientIdentityProviderDTO> response;

        PageRequest pageRequest = PageRequest.of(page, pageSize,
                Sort.by("createdDate").descending());

        if (clientId != null) {
            response = new CustomPage<>(idpRepository.findAllByClient_Id(clientId, pageRequest)
                    .map(idp -> modelMapper
                            .map(idp, ClientIdentityProviderDTO.class)));
        } else {
            response = new CustomPage<>(idpRepository.findAll(pageRequest).map(idp -> modelMapper
                    .map(idp, ClientIdentityProviderDTO.class)));
        }

        return response;
    }

    @Transactional
    public ClientIdentityProviderDTO createIDP(UUID clientId, ClientIdentityProviderDTO request) {
        ClientIdentityProviderDTO response = new ClientIdentityProviderDTO();
        Optional<Client> client = clientRepository.findById(clientId);

        if (client.isEmpty()) {
            throw new EntityNotFoundException("Client not found");
        }

        ClientIdentityProvider idp = modelMapper.map(request, ClientIdentityProvider.class);
        idp.setClient(client.get());

        ClientIdentityProvider savedClientProvider = idpRepository.save(idp);
        response = modelMapper.map(savedClientProvider,
                ClientIdentityProviderDTO.class);

        Client updatedClient = client.get();
        updatedClient.setIdentityProvider(savedClientProvider);

        clientRepository.save(updatedClient);

        return response;
    }

    @Transactional
    public ClientIdentityProviderDTO updateIDP(UUID id, ClientIdentityProviderDTO request) {
        Optional<ClientIdentityProvider> idp = idpRepository.findById(id);

        if (idp.isEmpty()) {
            throw new EntityNotFoundException("Identity Provider not found");
        }

        ClientIdentityProvider newIdp = idp.get();
        newIdp.setAdminRole(request.getAdminRole());
        newIdp.setVerificationStrategy(request.getVerificationStrategy());
        newIdp.setVerifyHeader(request.getVerifyHeader());
        newIdp.setVerifyParameter(request.getVerifyParameter());
        newIdp.setVerifyRequestField(request.getVerifyRequestField());
        newIdp.setVerifyResponseCustomer(request.getVerifyResponseCustomer());
        newIdp.setVerifyResponseRole(request.getVerifyResponseRole());
        newIdp.setVerifyUrl(request.getVerifyUrl());

        return modelMapper.map(idpRepository
                .save(newIdp), ClientIdentityProviderDTO.class);
    }

    @Transactional
    public DefaultDTO deleteIDP(UUID id) {
        idpRepository.deleteById(id);
        return new DefaultDTO("Identity Provider deleted successfully");
    }

    public List<ModuleDTO> listClientModules(UUID clientId, HttpServletRequest servletRequest) {

        UUID useClientId = clientId;

        if (clientId == null) {
            Client client = clientRepository.findBySource(servletRequest.getRemoteHost()).orElseThrow();
            useClientId = client.getId();
        }

        return moduleRepository.findByClient_Id(useClientId).stream()
                .map(module -> {
                    ModuleDTO dto = modelMapper.map(module, ModuleDTO.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ModuleDTO> createModules(UUID clientId, List<ModuleDTO> request) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new EntityNotFoundException("Client not found");
        }

        List<Module> newModules = moduleRepository.saveAll(
                request.stream().map(module -> {
                    Module mdl = modelMapper.map(module, Module.class);
                    mdl.setClient(client.get());
                    return mdl;
                })
                        .collect(Collectors.toList()));

        return newModules.stream().map(mdl -> modelMapper.map(mdl, ModuleDTO.class))
                .collect(Collectors.toList());

    }

    public ModuleDTO updateModule(Long id, ModuleDTO request) {
        Optional<Module> module = moduleRepository.findById(id);

        if (module.isEmpty()) {
            throw new EntityNotFoundException("Module not found");
        }

        Module newModule = module.get();
        newModule.setName(request.getName());
        newModule.setDescription(request.getDescription());

        return modelMapper.map(moduleRepository
                .save(newModule), ModuleDTO.class);
    }

    public DefaultDTO deleteModule(Long id) {
        moduleRepository.deleteById(id);
        return new DefaultDTO("Module deleted successfully");
    }
}