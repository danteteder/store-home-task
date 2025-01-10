package com.enefit.storetask.repository;

import com.enefit.storetask.model.ItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemAuditRepository extends JpaRepository<ItemAudit, Long> {
    List<ItemAudit> findByItemIdOrderByTimestampDesc(Long itemId);
    List<ItemAudit> findTop50ByOrderByTimestampDesc();
} 