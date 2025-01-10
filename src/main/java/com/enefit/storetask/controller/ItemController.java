package com.enefit.storetask.controller;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.model.ItemAudit;
import com.enefit.storetask.repository.ItemAuditRepository;
import com.enefit.storetask.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing store items.
 */
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemAuditRepository auditRepository;

    /**
     * Adds a new item to the inventory.
     *
     * @param item the item to be added
     * @return the added item
     */
    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.addItem(item));
    }

    /**
     * Updates an existing item in the inventory.
     *
     * @param id   the ID of the item to update
     * @param item the updated item details
     * @return the updated item
     */
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    /**
     * Deletes an item from the inventory.
     *
     * @param id the ID of the item to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Sells a specified quantity of an item.
     *
     * @param id       the ID of the item to sell
     * @param quantity the quantity to sell
     * @return the updated item
     */
    @PostMapping("/sell/{id}")
    public ResponseEntity<Item> sellItem(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(itemService.sellItem(id, quantity));
    }

    /**
     * Retrieves a report of current stock levels.
     *
     * @return a list of items in stock
     */
    @GetMapping("/report")
    public ResponseEntity<List<Item>> getStockReport() {
        return ResponseEntity.ok(itemService.getStockReport());
    }

    /**
     * Retrieves the audit trail of item transactions.
     *
     * @return a list of item audits
     */
    @GetMapping("/audit")
    public ResponseEntity<List<ItemAudit>> getAuditTrail() {
        return ResponseEntity.ok(auditRepository.findTop50ByOrderByTimestampDesc());
    }
}