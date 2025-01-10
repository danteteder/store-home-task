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
 * Service for managing store inventory and producing Kafka events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final StoreEventProducer eventProducer;

    @Transactional
    public Item addItem(Item item) {
        item.setSoldQuantity(0);
        Item savedItem = itemRepository.save(item);
        
        eventProducer.sendEvent(
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
        
        eventProducer.sendEvent(
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

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(id);
        eventProducer.sendEvent(
            "DELETED",
            item.getId(),
            item.getName(),
            item.getPrice(),
            item.getQuantity(),
            item.getQuantity(),
            String.format("Removed '%s' with remaining stock: %d", item.getName(), item.getQuantity())
        );
    }

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
        eventProducer.sendEvent(
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

    public List<Item> getStockReport() {
        return itemRepository.findAll();
    }
}