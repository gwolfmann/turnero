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

    public Mono<ReceiverDTO> deleteReceiver(ReceiverDTO receiverDTO, String internalId) {
        return receiverRepository.findByInternalId(internalId)
                .flatMap(existingReceiver -> updateId(receiverDTO, existingReceiver.getId()))
                .flatMap(receiverRepository::delete)
                .thenReturn(representsDeleted(receiverDTO))
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update Receiver by internalId " + internalId)));
    }

    private Mono<ReceiverDTO> updateId(ReceiverDTO receiverDTO, String id) {
        receiverDTO.setId(id);
        return Mono.just(receiverDTO);
    }

    private ReceiverDTO representsDeleted(ReceiverDTO receiverDTO) {
        receiverDTO.setName("deleted value");
        return receiverDTO;
    }
}

/*
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
*/

