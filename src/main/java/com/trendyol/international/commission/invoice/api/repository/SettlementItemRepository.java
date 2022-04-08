package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SettlementItemRepository extends CrudRepository<SettlementItem, Long> {

    List<SettlementItem> findBySellerIdAndItemCreationDateBetween(Long sellerId, Date startDate, Date endDate);

    @Modifying
    @Query(value = "UPDATE settlement_items SET delivery_date = :deliveryDate, payment_date= :paymentDate WHERE id=:id ", nativeQuery = true)
    Integer updateDeliveryDateAndPaymentDate(Date deliveryDate, Date paymentDate, Long id);
}
