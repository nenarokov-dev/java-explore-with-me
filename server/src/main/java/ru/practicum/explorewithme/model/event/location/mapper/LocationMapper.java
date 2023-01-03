package ru.practicum.explorewithme.model.event.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.event.location.Location;
import ru.practicum.explorewithme.model.event.location.dto.LocationDto;
import ru.practicum.explorewithme.model.user.User;
import ru.practicum.explorewithme.model.user.dto.UserDto;
import ru.practicum.explorewithme.model.user.dto.UserShortDto;


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