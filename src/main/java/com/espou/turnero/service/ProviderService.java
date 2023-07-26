package com.espou.turnero.service;

import com.espou.turnero.storage.ProviderDTO;
import com.espou.turnero.storage.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public Flux<ProviderDTO> getAllProviders() {
        return providerRepository.findAll();
    }

    public Mono<ProviderDTO> getProviderById(String id) {
        return providerRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Provider by id " + id)));
    }

    public Mono<ProviderDTO> getProviderByInternalId(String internalId) {
        return providerRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Provider by internalId " + internalId)));
    }

    public Mono<ProviderDTO> writeProvider(ProviderDTO providerDTO) {
        return providerRepository.save(providerDTO);
    }

    public Mono<ProviderDTO> updateProvider(ProviderDTO providerDTO, String internalId) {
        return providerRepository.findByInternalId(internalId)
                .flatMap(existingProvider -> updateId(providerDTO, existingProvider.getId()))
                .flatMap(providerRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Provider by internalId " + internalId)));
    }

    public Mono<ProviderDTO> deleteProvider(String internalId) {
        return providerRepository.deleteByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Provider by internalId " + internalId)));
    }
    private Mono<ProviderDTO> updateId(ProviderDTO providerDTO, String id) {
        providerDTO.setId(id);
        return Mono.just(providerDTO);
    }

    private ProviderDTO representsDeleted(ProviderDTO providerDTO) {
        providerDTO.setName("deleted value");
        return providerDTO;
    }
}