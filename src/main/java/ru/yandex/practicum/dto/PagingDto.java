package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

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

    public static PagingDto fromPage(Page<?> page) {
        return new PagingDto(page.getNumber(), page.getSize(), (int) page.getTotalElements());
    }
}