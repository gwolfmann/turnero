package com.espou.turnero.storage;
public class ReceiverMapper {
    public static ReceiverDTO toDto(com.espou.turnero.model.Receiver receiver) {
        return new ReceiverDTO(
                receiver.getId(),
                receiver.getName(),
                receiver.getAffiliation(),
                receiver.getIdentification()
        );
    }

    public static com.espou.turnero.model.Receiver toEntity(ReceiverDTO receiverDto) {
        return new com.espou.turnero.model.Receiver(
                receiverDto.getId(),
                receiverDto.getName(),
                receiverDto.getAffiliation(),
                receiverDto.getIdentification()
        );
    }
}