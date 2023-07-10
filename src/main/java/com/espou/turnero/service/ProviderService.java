package com.espou.turnero.service;

import com.espou.turnero.storage.ProviderDTO;
import com.espou.turnero.storage.ProviderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public Flux<ProviderDTO> getAllProviders() {
        return providerRepository.findAll();
    }

    public Mono<ProviderDTO> getProviderById(String id) {
        return providerRepository.findById(id);
    }

    public Mono<ProviderDTO> getProviderByInternalId(String internalId) {
        return providerRepository.findByInternalId(internalId);
    }

    public Mono<ProviderDTO> createProvider(ProviderDTO providerDTO) {
        return providerRepository.save(providerDTO);
    }

    public Mono<ProviderDTO> updateProvider(String id, ProviderDTO providerDTO) {
        return providerRepository.findById(id)
                .flatMap(existingProvider -> {
                    existingProvider.setName(providerDTO.getName());
                    existingProvider.setTimeline(providerDTO.getTimeline());
                    existingProvider.setDefaultResource(providerDTO.getDefaultResource());
                    existingProvider.setDefaultTask(providerDTO.getDefaultTask());
                    return providerRepository.save(existingProvider);
                });
    }

    public Mono<ProviderDTO> updateProviderByInternalId(String internalId, ProviderDTO providerDTO) {
        return providerRepository.findByInternalId(internalId)
                .flatMap(existingProvider -> {
                    providerDTO.setId(existingProvider.getId());
                    return providerRepository.save(providerDTO);
                });
    }

    public Mono<Void> deleteProvider(String id) {
        return providerRepository.deleteById(id);
    }

    public Mono<Void> deleteProviderByInternalId(String internalId) {
        return providerRepository.findByInternalId(internalId)
                .flatMap(provider -> providerRepository.deleteById(provider.getId()));
    }
}


