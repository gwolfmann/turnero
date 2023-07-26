package com.espou.turnero.storage;

import com.espou.turnero.model.TimeLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "providers")
public class ProviderDTO {
    @Id
    private String id;
    private String name;
    private TimeLine timeline;
//    private Resource defaultResource;
//    private Task defaultTask;
    @Indexed(unique = true)
    private String internalId;
}
