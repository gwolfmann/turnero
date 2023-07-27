package com.espou.turnero.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "receivers")
public class ReceiverDTO {
    @Id
    private String id;
    private String name;
    private String affiliation;
    private String identification;
    private String email;
    private String telephone;
    @Indexed(unique = true)
    private String internalId; // Internal ID
}