package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SettlementItemRepository extends CrudRepository<SettlementItem, Long> {

    @Query(value = "SELECT * FROM settlement_items WHERE seller_id = ?1 AND " +
            "((transaction_type_id = 1 AND delivery_date BETWEEN ?2 AND ?3) OR " +
            "(transaction_type_id = 51 AND item_creation_date BETWEEN ?2 AND ?3))", nativeQuery = true)
    List<SettlementItem> getSettlementItems(Long sellerId, Date startDate, Date endDate);
}
