package ru.practicum.explorewithme.model.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.location.Location;
import ru.practicum.explorewithme.model.location.dto.LocationDto;


@Component
public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocationFromDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

}