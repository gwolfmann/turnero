package com.espou.turnero.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "users")
public class UserDTO {
    @Id
    private String id;
    private String name;
    private String email;
    private String passw;
    @Indexed(unique = true)
    private String internalId;
}
