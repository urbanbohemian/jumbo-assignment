package com.trendyol.international.commission.invoice.api.util;


import java.util.Optional;

public interface FilterExtension<T> {

    default boolean applyFilter(T model) {
        return true;
    }

    default void process(T model) {
        Optional.ofNullable(model)
                .filter(this::applyFilter)
                .ifPresentOrElse(this::execute,
                        () -> handleError(model));
    }

    void execute(T model);

    void handleError(T model);
}
