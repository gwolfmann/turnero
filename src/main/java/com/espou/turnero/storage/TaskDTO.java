package com.espou.turnero.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "tasks")
public class TaskDTO {
    @Id
    private String id;
    private String name;
    private Integer duration;
    @Indexed(unique = true)
    private String internalId; // Internal ID
}
