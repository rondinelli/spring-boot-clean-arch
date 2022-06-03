package com.stt.solution.domain.TO;

import java.util.List;

public class PageTO <T> {

    private List<T> content;

    private long total;

    public PageTO(List<T> content, long total) {
        this.content = content;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
