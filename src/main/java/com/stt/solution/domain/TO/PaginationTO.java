package com.stt.solution.domain.TO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PaginationTO {

    private int page;

    private int size;

    public PaginationTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    private Map<String, Object> params;

    private Date from;
    private Date to;

}
