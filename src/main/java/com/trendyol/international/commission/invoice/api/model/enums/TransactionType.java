package com.trendyol.international.commission.invoice.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public enum TransactionType {

    Sale(1, BigDecimal.ONE),
    Return(51, BigDecimal.ONE.negate()),
    Cancel(101, BigDecimal.ZERO),
    Discount(151, BigDecimal.ONE.negate()),
    DiscountCancel(201, BigDecimal.ONE),
    Coupon(251, BigDecimal.ONE.negate()),
    CouponCancel(301, BigDecimal.ONE),
    ProvisionPositive(401, BigDecimal.ONE),
    ProvisionNegative(451, BigDecimal.ONE.negate());

    private static final EnumSet<TransactionType> ALL_PROMOTION_TYPES = EnumSet.of(Discount, Coupon, DiscountCancel, CouponCancel);
    private static final EnumSet<TransactionType> SALE_TYPES = EnumSet.of(Sale, Discount, Coupon);
    private static final EnumSet<TransactionType> CANCEL_TYPES = EnumSet.of(Cancel, DiscountCancel, CouponCancel);
    private static final EnumSet<TransactionType> CHARGE_TYPES = EnumSet.of(Discount, Coupon);
    private static final EnumSet<TransactionType> CHARGE_CANCEL_TYPES = EnumSet.of(DiscountCancel, CouponCancel);
    private static final EnumSet<TransactionType> RETURN_TYPES = EnumSet.of(Return, DiscountCancel, CouponCancel);
    private static final EnumSet<TransactionType> DISCOUNT_TYPES = EnumSet.of(Discount, DiscountCancel);
    private static final EnumSet<TransactionType> COUPON_TYPES = EnumSet.of(Coupon, CouponCancel);
    private static final EnumSet<TransactionType> PROVISION_TYPES = EnumSet.of(ProvisionPositive, ProvisionNegative);

    private static final Map<Integer, TransactionType> ENUM_MAP;
    private static final Map<TransactionType, TransactionType> CANCEL_MAP;
    private static final Map<TransactionType, TransactionType> RETURN_MAP;
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

    static {
        CANCEL_MAP = new HashMap<>();
        CANCEL_MAP.put(Sale, Cancel);
        CANCEL_MAP.put(Discount, DiscountCancel);
        CANCEL_MAP.put(Coupon, CouponCancel);
        CANCEL_MAP.put(ProvisionPositive, ProvisionNegative);
        CANCEL_MAP.put(ProvisionNegative, ProvisionPositive);
    }

    static {
        RETURN_MAP = new HashMap<>();
        RETURN_MAP.put(Sale, Return);
        RETURN_MAP.put(Discount, DiscountCancel);
        RETURN_MAP.put(Coupon, CouponCancel);
        RETURN_MAP.put(ProvisionPositive, ProvisionNegative);
        RETURN_MAP.put(ProvisionNegative, ProvisionPositive);
    }

    private final int id;
    private BigDecimal multiplier;

    TransactionType(int id, BigDecimal multiplier) {
        this.setMultiplier(multiplier);
        this.id = id;
    }

    @JsonCreator
    public static TransactionType from(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        return ENUM_NAME_MAP.get(name);
    }

    public static TransactionType from(Integer id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return ENUM_MAP.get(id);
    }

    public static EnumSet<TransactionType> getAllPromotionTypes() {
        return ALL_PROMOTION_TYPES;
    }

    public static EnumSet<TransactionType> getChargeTypes() {
        return CHARGE_TYPES;
    }

    public static EnumSet<TransactionType> getChargeCancelTypes() {
        return CHARGE_CANCEL_TYPES;
    }

    public static EnumSet<TransactionType> getCancelTypes() {
        return CANCEL_TYPES;
    }

    public static EnumSet<TransactionType> getAllProvisionTypes() {
        return PROVISION_TYPES;
    }

    public static boolean isSaleType(TransactionType typeName) {
        return SALE_TYPES.contains(typeName);
    }

    public static boolean isDiscountType(TransactionType typeName) {
        return DISCOUNT_TYPES.contains(typeName);
    }

    public static boolean isCouponType(TransactionType typeName) {
        return COUPON_TYPES.contains(typeName);
    }

    public static boolean isReturnType(TransactionType typeName) {
        return RETURN_TYPES.contains(typeName);
    }

    public static TransactionType getItsCancel(TransactionType transactionType) {
        return CANCEL_MAP.get(transactionType);
    }

    public static TransactionType getItsReturn(TransactionType transactionType) {
        return RETURN_MAP.get(transactionType);
    }

    public static Set<TransactionType> getCancellableTypeNames() {
        return CANCEL_MAP.keySet();
    }

    public static Set<TransactionType> getReturnableTypeNames() {
        return RETURN_MAP.keySet();
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    private void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public int getId() {
        return id;
    }

    @JsonValue
    public String getName() {
        return name();
    }
}