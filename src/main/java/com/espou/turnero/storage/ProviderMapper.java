package com.espou.turnero.storage;

import com.espou.turnero.model.Provider;

public class ProviderMapper {
    public static ProviderDTO toDto(Provider provider) {
        return new ProviderDTO(
                provider.getId(),
                provider.getName(),
                provider.getTimeline(),
                provider.getDefaultResource(),
                provider.getDefaultTask()
        );
    }

    public static Provider toEntity(ProviderDTO providerDto) {
        return new Provider(
                providerDto.getId(),
                providerDto.getName(),
                providerDto.getTimeline(),
                providerDto.getDefaultResource(),
                providerDto.getDefaultTask()
        );
    }
}
