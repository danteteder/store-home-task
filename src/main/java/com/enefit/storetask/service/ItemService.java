package com.enefit.storetask.service;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing store inventory and producing Kafka events.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreEventProducer storeEventProducer;

    /**
     * Adds a new item to the inventory.
     *
     * @param item the item to add
     * @return the added item
     */
    @Transactional
    public Item addItem(Item item) {
        item.setSoldQuantity(0);
        Item savedItem = itemRepository.save(item);
        
        storeEventProducer.sendEvent(
            "CREATED",
            savedItem.getId(),
            savedItem.getName(),
            savedItem.getPrice(),
            savedItem.getQuantity(),
            savedItem.getQuantity(),
            String.format("Added new item '%s' with stock: %d", 
                savedItem.getName(), savedItem.getQuantity())
        );
        
        return savedItem;
    }

    /**
     * Updates an existing item in the inventory.
     *
     * @param id   the ID of the item to update
     * @param item the updated item details
     * @return the updated item
     */
    @Transactional
    public Item updateItem(Long id, Item item) {
        Item existingItem = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        String changes = String.format("Updated '%s': Price: $%.2f → $%.2f, Stock: %d → %d",
            existingItem.getName(),
            existingItem.getPrice(), item.getPrice(),
            existingItem.getQuantity(), item.getQuantity());

        existingItem.setName(item.getName());
        existingItem.setPrice(item.getPrice());
        existingItem.setQuantity(item.getQuantity());

        Item updatedItem = itemRepository.save(existingItem);
        
        storeEventProducer.sendEvent(
            "UPDATED",
            updatedItem.getId(),
            updatedItem.getName(),
            updatedItem.getPrice(),
            updatedItem.getQuantity(),
            updatedItem.getQuantity(),
            changes
        );
        
        return updatedItem;
    }

    /**
     * Deletes an item from the inventory.
     *
     * @param id the ID of the item to delete
     */
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(id);
        storeEventProducer.sendEvent(
            "DELETED",
            item.getId(),
            item.getName(),
            item.getPrice(),
            item.getQuantity(),
            item.getQuantity(),
            String.format("Removed '%s' with remaining stock: %d", item.getName(), item.getQuantity())
        );
    }

    /**
     * Sells a specified quantity of an item.
     *
     * @param id           the ID of the item to sell
     * @param quantitySold the quantity to sell
     * @return the updated item
     */
    @Transactional
    public Item sellItem(Long id, int quantitySold) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (item.getQuantity() < quantitySold) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        int previousStock = item.getQuantity();
        item.setQuantity(previousStock - quantitySold);
        item.setSoldQuantity(item.getSoldQuantity() + quantitySold);
        
        Item updatedItem = itemRepository.save(item);
        storeEventProducer.sendEvent(
            "SOLD",
            updatedItem.getId(),
            updatedItem.getName(),
            updatedItem.getPrice(),
            quantitySold,
            updatedItem.getQuantity(),
            String.format("Sold %d units of '%s'. Stock: %d → %d", 
                quantitySold, item.getName(), previousStock, updatedItem.getQuantity())
        );
        
        return updatedItem;
    }

    /**
     * Retrieves a report of current stock levels.
     *
     * @return a list of items in stock
     */
    public List<Item> getStockReport() {
        return itemRepository.findAll();
    }
}