package com.espou.turnero.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Meet {
    private String id;
    private Resource resource;
    private Provider provider;
    private Receiver receiver;
    private Task task;
    private LocalDate date;
    private LocalTime hour;
    private Integer duration;
    private String internalId;
    private User lastUSer;

    @Builder
    public Meet(String id,
            String resourceInternalId,
            String providerInternalId,
            String receiverInternalId,
            Task task,
            LocalDate date,
            LocalTime hour,
            Integer duration,
            String internalId,
            User lastUSer){
       this.id=id;
       this.resource=Resource.builder().internalId(resourceInternalId).build();
       this.provider=Provider.builder().internalId(providerInternalId).build();
       this.receiver=Receiver.builder().internalId(receiverInternalId).build();
       this.task=task;
       this.date=date;
       this.hour=hour;
       this.duration=duration;
       this.internalId=internalId;
       this.lastUSer=lastUSer;
    }
    public LocalTime endingTime() {
        return hour.plusMinutes(duration);
    }

}
