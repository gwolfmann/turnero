package com.espou.turnero.service;

import com.espou.turnero.storage.UserDTO;
import com.espou.turnero.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserDTO> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserDTO> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found User by id " + id)));
    }

    public Mono<UserDTO> getUserByInternalId(String internalId) {
        return userRepository.findByInternalId(internalId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found User by internalId " + internalId)));
    }

    public Mono<UserDTO> writeUser(UserDTO userDTO) {
        return userRepository.save(userDTO);
    }

    public Mono<UserDTO> updateUser(UserDTO userDTO, String internalId) {
        return userRepository.findByInternalId(internalId)
                .flatMap(existingUser -> updateId(userDTO, existingUser.getId()))
                .flatMap(userRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update User by internalId " + internalId)));
    }

    public Mono<UserDTO> deleteUser(String internalId) {
      //  UserDTO userDTO = UserDTO.builder().internalId(internalId).build();
        return userRepository.findByInternalId(internalId)
             //   .flatMap(existingUser -> updateId(userDTO, existingUser.getId()))
                .flatMap(x -> {userRepository.delete(x);
                                return Mono.just(representsDeleted(x));})
                .switchIfEmpty(Mono.error(new RuntimeException("Not found to update User by internalId " + internalId)));
    }

    private Mono<UserDTO> updateId(UserDTO userDTO, String id) {
        userDTO.setId(id);
        return Mono.just(userDTO);
    }

    private UserDTO representsDeleted(UserDTO userDTO) {
        userDTO.setName("deleted value");
        return userDTO;
    }
}
