package com.espou.turnero.storage;

import com.espou.turnero.model.Resource;
import com.espou.turnero.model.Task;
import com.espou.turnero.model.TimeLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class ProviderDTO {
    @Id
    private String id;
    private String name;
    private TimeLine timeline;
    private Resource defaultResource;
    private Task defaultTask;
}
