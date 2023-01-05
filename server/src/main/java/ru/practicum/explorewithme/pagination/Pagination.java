package ru.practicum.explorewithme.pagination;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exceptions.BadRequestException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Pagination<T> {

    public List<T> setPagination(Integer from, Integer size, List<T> collection) {
        if (size != null && size <= 0) {
            String message = "Размер страницы должен быть больше нуля.";
            log.error(message);
            throw new BadRequestException(message);
        }
        if (from != null && from < 0) {
            String message = "Индекс первого элемента страницы не может быть меньше нуля.";
            log.error(message);
            throw new BadRequestException(message);
        }
        if (from == null) {
            if (size != null) {
                return collection.stream().limit(size).collect(Collectors.toList());
            } else
                return collection;
        }
        if (from > collection.size()) {
            return Collections.emptyList();
        } else
            return collection.stream().skip(from).limit(size).collect(Collectors.toList());
    }
}
