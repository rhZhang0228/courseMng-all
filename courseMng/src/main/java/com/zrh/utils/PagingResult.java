package com.zrh.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalCount;

    private List<T> items = Collections.emptyList();
}
