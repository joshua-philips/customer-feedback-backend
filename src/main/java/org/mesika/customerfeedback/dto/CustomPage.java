package org.mesika.customerfeedback.dto;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomPage<T> implements Page<T> {

    private final Page<T> delegate;

    public CustomPage(@JsonProperty("data") final List<T> content,
            @JsonProperty("page") final int page,
            @JsonProperty("size") final int size,
            @JsonProperty("total_elements") final long totalElements) {
        delegate = new PageImpl<>(content, PageRequest.of(page, size), totalElements);
    }

    public CustomPage(final List<T> content,
            final Pageable pageable,
            final long total) {
        delegate = new PageImpl<>(content, pageable, total);
    }

    public CustomPage(Page<T> page) {
        delegate = page;
    }

    @JsonProperty("total_pages")
    @Override
    public int getTotalPages() {
        return delegate.getTotalPages();
    }

    @JsonProperty("total_elements")
    @Override
    public long getTotalElements() {
        return delegate.getTotalElements();
    }

    @JsonProperty("page")
    @Override
    public int getNumber() {
        return delegate.getNumber();
    }

    @JsonProperty("size")
    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @JsonProperty("number_of_elements")
    @Override
    public int getNumberOfElements() {
        return delegate.getNumberOfElements();
    }

    @JsonProperty("data")
    @Override
    public List<T> getContent() {
        return delegate.getContent();
    }

    @JsonIgnore
    @JsonProperty("has_content")
    @Override
    public boolean hasContent() {
        return delegate.hasContent();
    }

    @JsonIgnore
    @JsonProperty("sort")
    @Override
    public Sort getSort() {
        return delegate.getSort();
    }

    @JsonProperty("is_first")
    @Override
    public boolean isFirst() {
        return delegate.isFirst();
    }

    @JsonProperty("is_last")
    @Override
    public boolean isLast() {
        return delegate.isLast();
    }

    @JsonProperty("has_next")
    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @JsonProperty("has_previous")
    @Override
    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    @JsonIgnore
    @Override
    public Pageable nextPageable() {
        return delegate.nextPageable();
    }

    @JsonIgnore
    @Override
    public Pageable previousPageable() {
        return delegate.previousPageable();
    }

    @Override
    public <U> Page<U> map(final Function<? super T, ? extends U> function) {
        return delegate.map(function);
    }

    @JsonIgnore
    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @JsonIgnore
    @Override
    public Pageable getPageable() {
        return delegate.getPageable();
    }
}
