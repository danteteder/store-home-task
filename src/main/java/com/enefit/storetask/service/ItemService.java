package com.enefit.storetask.service;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item addItem(Item item) {
        item.setSoldQuantity(0); // Ensure sold quantity starts as 0
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item updatedItem) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        // Update fields
        existingItem.setName(updatedItem.getName());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setQuantity(updatedItem.getQuantity());

        return itemRepository.save(existingItem);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item not found with ID: " + id);
        }
        itemRepository.deleteById(id);
    }

    public Item sellItem(Long id, int quantitySold) {
        Item itemToSell = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with ID: " + id));

        if (itemToSell.getQuantity() < quantitySold) {
            throw new IllegalArgumentException("Insufficient stock to sell.");
        }

        itemToSell.setQuantity(itemToSell.getQuantity() - quantitySold);
        itemToSell.setSoldQuantity(itemToSell.getSoldQuantity() + quantitySold);

        return itemRepository.save(itemToSell);
    }

    public List<Item> getStockReport() {
        return itemRepository.findAll();
    }
}