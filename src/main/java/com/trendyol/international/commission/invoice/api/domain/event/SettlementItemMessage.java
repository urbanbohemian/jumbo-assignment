package com.trendyol.international.commission.invoice.api.domain.event;

import java.math.BigDecimal;
import java.util.Date;

public class SettlementItemMessage {
    private Long oli_id;
    private String t_type;
    private BigDecimal price;
    private BigDecimal c_rate;
    private Date d_date;
    private Date p_date;
    private BigDecimal discount_price;
    private BigDecimal coupon_price;
    private BigDecimal total_price;
    private BigDecimal total_commision;
    private BigDecimal total_seller_revenue;
    private Long store_front_id;
    private String currency;
}
