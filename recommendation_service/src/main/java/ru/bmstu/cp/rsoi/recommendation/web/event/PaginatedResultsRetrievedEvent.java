package ru.bmstu.cp.rsoi.recommendation.web.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public final class PaginatedResultsRetrievedEvent<T extends Serializable> extends ApplicationEvent {
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;
    private final int page;
    private final int totalPages;
    private final int pageSize;

    public PaginatedResultsRetrievedEvent(final Class<T> clazz,
                                          final UriComponentsBuilder uriBuilder,
                                          final HttpServletResponse response,
                                          final int page, final int totalPages, final int pageSize) {
        super(clazz);

        this.uriBuilder = uriBuilder;
        this.response = response;
        this.page = page;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }

    // API

    public final UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public final HttpServletResponse getResponse() {
        return response;
    }

    public final int getPage() {
        return page;
    }

    public final int getTotalPages() {
        return totalPages;
    }

    public final int getPageSize() {
        return pageSize;
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @SuppressWarnings("unchecked")
    public final Class<T> getClazz() {
        return (Class<T>) getSource();
    }

}

