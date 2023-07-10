package com.espou.turnero.storage;

import com.espou.turnero.model.TimeLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "resources")
public class ResourceDTO {
    @Id
    private String id;
    private String name;
    private TimeLine timeline;
    private String internalId;
}