package ru.practicum.explorewithme.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.request.model.ItemRequest;
import ru.practicum.explorewithme.user.model.User;

@Component
public class RequestMapper {

    public static ItemRequest fromRequestDto(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .build();
    }
}
