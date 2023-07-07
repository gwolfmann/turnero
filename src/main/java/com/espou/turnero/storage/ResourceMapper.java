package com.espou.turnero.storage;

import com.espou.turnero.model.Resource;

// Mapper class for Resource and ResourceDTO
public class ResourceMapper {
    public static ResourceDTO toDto(Resource resource) {
        return new ResourceDTO(resource.getId(), resource.getName(), resource.getTimeline());
    }

    public static Resource toEntity(ResourceDTO resourceDto) {
        return new Resource(resourceDto.getId(), resourceDto.getName(), resourceDto.getTimeline());
    }
}
