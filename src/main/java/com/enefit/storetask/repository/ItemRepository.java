package com.enefit.storetask.repository;

import com.enefit.storetask.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Item entity.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Checks if an item with the given name exists.
     *
     * @param name the name of the item
     * @return true if an item with the name exists, false otherwise
     */
    boolean existsByName(String name);
}