package com.espou.turnero.storage;

import com.espou.turnero.model.Meet;

public class MeetMapper {
    public static MeetDTO toDto(Meet meet) {
        return new MeetDTO(
                meet.getId(),
                meet.getResource(),
                meet.getProvider(),
                meet.getReceiver(),
                meet.getTask(),
                meet.getDate(),
                meet.getHour(),
                meet.getDuration()
        );
    }

    public static Meet toEntity(MeetDTO meetDto) {
        return new Meet(
                meetDto.getId(),
                meetDto.getResource(),
                meetDto.getProvider(),
                meetDto.getReceiver(),
                meetDto.getTask(),
                meetDto.getDate(),
                meetDto.getHour(),
                meetDto.getDuration()
        );
    }
}

