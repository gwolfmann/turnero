package com.espou.turnero.service;

import com.espou.turnero.storage.ReceiverDTO;
import com.espou.turnero.storage.ReceiverRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReceiverService {

    private final ReceiverRepository receiverRepository;

    @Autowired
    public ReceiverService(ReceiverRepository receiverRepository) {
        this.receiverRepository = receiverRepository;
    }

    public Flux<ReceiverDTO> getAllReceivers() {
        return receiverRepository.findAll();
    }

    public Mono<ReceiverDTO> getReceiverById(String id) {
        return receiverRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Receiver by id " + id)));
    }

    public Mono<ReceiverDTO> getReceiverByInternalId(String internalId) {
        return receiverRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found Receiver by internalId " + internalId)));
    }

    public Mono<ReceiverDTO> writeReceiver(ReceiverDTO receiverDTO) {
        return receiverRepository.save(receiverDTO);
    }

    public Mono<ReceiverDTO> updateReceiver(ReceiverDTO receiverDTO, String internalId) {
        return receiverRepository.findByInternalId(internalId)
                .flatMap(existingReceiver -> updateId(receiverDTO, existingReceiver.getId()))
                .flatMap(receiverRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Receiver by internalId " + internalId)));
    }

    public Mono<ReceiverDTO> deleteReceiver(String internalId) {
        return receiverRepository.deleteByInternalId(internalId)
        .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Receiver by internalId " + internalId)));
    }

    private Mono<ReceiverDTO> updateId(ReceiverDTO receiverDTO, String id) {
        receiverDTO.setId(id);
        return Mono.just(receiverDTO);
    }

}
