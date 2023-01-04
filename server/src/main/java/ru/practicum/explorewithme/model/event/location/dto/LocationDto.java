package ru.practicum.explorewithme.model.event.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LocationDto {

    private Float lat;
    private Float lon;
}
