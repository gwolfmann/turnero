package com.espou.turnero.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class ReceiverDTO {
    @Id
    private String id;
    private String name;
    private String affiliation;
    private Integer identification;
}