package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagingDto {
    private final int pageNumber;
    private final int pageSize;
    private final int totalCount;

    public boolean hasNext() {
        return (pageNumber + 1) * pageSize < totalCount;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}