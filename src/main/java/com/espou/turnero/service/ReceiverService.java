package com.espou.turnero.service;

import com.espou.turnero.storage.ReceiverDTO;
import com.espou.turnero.storage.ReceiverRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
public class ReceiverService {
    private final ReceiverRepository ReceiverRepository;

    public ReceiverService(ReceiverRepository ReceiverRepository) {
        this.ReceiverRepository = ReceiverRepository;
    }

    public Flux<ReceiverDTO> getAllReceivers() {
        return ReceiverRepository.findAll();
    }

    public Mono<ReceiverDTO> getReceiverById(String id) {
        return ReceiverRepository.findById(id);
    }
    public Mono<ReceiverDTO> getReceiverByInternalId(String internalId) {
        return ReceiverRepository.findByInternalId(internalId);
    }

    public Mono<ReceiverDTO> createReceiver(ReceiverDTO ReceiverDTO) {
        return ReceiverRepository.save(ReceiverDTO);
    }

    public Mono<ReceiverDTO> updateReceiver(String id, ReceiverDTO ReceiverDTO) {
        return ReceiverRepository.updateById(id, ReceiverDTO);
    }

    public Mono<ReceiverDTO> updateReceiverByInternalId(String internalId, ReceiverDTO ReceiverDTO) {
        return ReceiverRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new NoSuchElementException(internalId+" Receiver not found")))
                .flatMap(existingReceiver -> {
                    ReceiverDTO.setId(existingReceiver.getId());
                    return ReceiverRepository.updateById(existingReceiver.getId(),ReceiverDTO);
                });
    }
    public Mono<Void> deleteReceiver(String id) {
        return ReceiverRepository.deleteById(id);
    }

    public Mono<Void> deleteReceiverByInternalId(String internalId) {
        return ReceiverRepository.deleteByInternalId(internalId);
    }
}
