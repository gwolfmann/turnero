package com.espou.turnero.storage;

import com.espou.turnero.model.Meet;

public class MeetMapper {
    public static MeetDTO toDto(Meet meet) {
        return new MeetDTO(
            meet.getId(),
            meet.getResource().getInternalId(),
            meet.getProvider().getInternalId(),
            meet.getReceiver().getInternalId(),
            meet.getTask(),
            meet.getDate(),
            meet.getHour(),
            meet.getDuration(),
            meet.getInternalId(),
            meet.getLastUser(),
            meet.getReceiverName()
        );
    }

    public static Meet toEntity(MeetDTO meetDto) {
        return new Meet(
            meetDto.getId(),
            meetDto.getResourceInternalId(),
            meetDto.getProviderInternalId(),
            meetDto.getReceiverInternalId(),
            meetDto.getTask(),
            meetDto.getDate(),
            meetDto.getHour(),
            meetDto.getDuration(),
            meetDto.getInternalId(),
            meetDto.getLastUser(),
            meetDto.getReceiverName()
        );
    }
    public static Meet toEntity(MeetDTOLookup meetDTOLookup) {
        return new Meet(
                meetDTOLookup.getId(),
                meetDTOLookup.getResource(),
                meetDTOLookup.getProvider(),
                meetDTOLookup.getReceiver(),
                meetDTOLookup.getTask(),
                meetDTOLookup.getDate(),
                meetDTOLookup.getHour(),
                meetDTOLookup.getDuration(),
                meetDTOLookup.getInternalId(),
                meetDTOLookup.getLastUser(),
                meetDTOLookup.getReceiver().getName()
        );
    }
}

