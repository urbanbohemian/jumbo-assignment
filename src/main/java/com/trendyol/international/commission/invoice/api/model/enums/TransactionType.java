package com.trendyol.international.commission.invoice.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum TransactionType {

    SALE(1, BigDecimal.ONE),
    RETURN(51, BigDecimal.ONE.negate()),
    CANCEL(101, BigDecimal.ZERO);

    private static final Map<Integer, TransactionType> ENUM_MAP;
    private static final Map<String, TransactionType> ENUM_NAME_MAP = new HashMap<>();

    static {
        Arrays.asList(TransactionType.values())
                .forEach(enumValue -> ENUM_NAME_MAP.put(enumValue.name(), enumValue));
    }

    static {
        ENUM_MAP = new HashMap<>();
        Arrays.stream(TransactionType.values())
                .forEach(enumValue -> ENUM_MAP.put(enumValue.id, enumValue));
    }

    private final int id;
    private final BigDecimal multiplier;

    TransactionType(int id,  BigDecimal multiplier1) {
        this.multiplier = multiplier1;
        this.id = id;
    }

    @JsonCreator
    public static TransactionType from(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Illegal " + TransactionType.class.getSimpleName() + " name: " + name);
        }

        var value = ENUM_NAME_MAP.get(name);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Illegal " + TransactionType.class.getSimpleName() + " name: " + name);
        }
        return value;
    }

    public static TransactionType from(Integer id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Illegal " + TransactionType.class.getSimpleName() + " id: " + id);
        }

        var value = ENUM_MAP.get(id);
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Illegal " + TransactionType.class.getSimpleName() + " id: " + id);
        }
        return value;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    @JsonValue
    public String getName() {
        return name();
    }
}
