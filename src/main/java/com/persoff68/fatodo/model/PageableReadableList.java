package com.persoff68.fatodo.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageableReadableList<T> {

    private List<T> data;

    private long count;

    private long unread;

    public static <T> PageableReadableList<T> of(List<T> data, long count, long unread) {
        PageableReadableList<T> pageableReadableList = new PageableReadableList<>();
        pageableReadableList.setData(data);
        pageableReadableList.setCount(count);
        pageableReadableList.setUnread(unread);
        return pageableReadableList;
    }

    public static <T> PageableReadableList<T> empty() {
        PageableReadableList<T> pageableReadableList = new PageableReadableList<>();
        pageableReadableList.setData(Collections.emptyList());
        pageableReadableList.setCount(0L);
        pageableReadableList.setUnread(0L);
        return pageableReadableList;
    }
}
