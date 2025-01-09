package com.enefit.storetask.repository;

import com.enefit.storetask.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Placeholder for custom queries if needed in the future
}