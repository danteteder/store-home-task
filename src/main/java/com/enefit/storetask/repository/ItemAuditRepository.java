package com.enefit.storetask.repository;

import com.enefit.storetask.model.ItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for ItemAudit entity.
 */
public interface ItemAuditRepository extends JpaRepository<ItemAudit, Long> {

    /**
     * Finds the audit records for a specific item, ordered by timestamp descending.
     *
     * @param itemId the ID of the item
     * @return a list of item audits
     */
    List<ItemAudit> findByItemIdOrderByTimestampDesc(Long itemId);

    /**
     * Finds the top 50 most recent audit records.
     *
     * @return a list of item audits
     */
    List<ItemAudit> findTop50ByOrderByTimestampDesc();
}