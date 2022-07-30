package com.persoff68.fatodo.model;

import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
public class PageableReadableList<T> {

    private List<T> data;

    private long count;

    private long unread;

    public <Z> PageableReadableList<Z> convert(Function<T, Z> converterFunc) {
        List<Z> convertedList = this.data.stream()
                .map(converterFunc)
                .toList();
        return PageableReadableList.of(convertedList, count, unread);
    }

    public static <T> PageableReadableList<T> of(List<T> data, long count, long unread) {
        PageableReadableList<T> pageableReadableList = new PageableReadableList<>();
        pageableReadableList.setData(data);
        pageableReadableList.setCount(count);
        pageableReadableList.setUnread(unread);
        return pageableReadableList;
    }

}
