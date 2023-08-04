package com.espou.turnero.storage;

import com.espou.turnero.model.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class UserDTO {
    @Id
    private String id;
    private String name;
    private String email;
    private String passw;
    private String telephone;
    @Indexed(unique = true)
    private String internalId;
    private Profile profile;
}
