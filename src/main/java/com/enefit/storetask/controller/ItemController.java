package com.enefit.storetask.controller;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.model.ItemAudit;
import com.enefit.storetask.repository.ItemAuditRepository;
import com.enefit.storetask.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> addItem(@Validated @RequestBody Item item) {
        if (item == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Item cannot be null"));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemService.addItem(item));
    }

    /**
     * Updates an existing item in the inventory.
     *
     * @param id   the ID of the item to update
     * @param item the updated item details
     * @return the updated item
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @Validated @RequestBody Item item) {
        if (item == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Item cannot be null"));
        }
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    /**
     * Deletes an item from the inventory.
     *
     * @param id the ID of the item to delete
     * @return a response entity with deletion confirmation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "ID cannot be null"));
        }
        itemService.deleteItem(id);
        return ResponseEntity.ok(Map.of(
            "message", "Item deleted successfully",
            "id", id
        ));
    }

    /**
     * Sells a specified quantity of an item.
     *
     * @param id       the ID of the item to sell
     * @param quantity the quantity to sell
     * @return the updated item
     */
    @PostMapping("/sell/{id}")
    public ResponseEntity<?> sellItem(@PathVariable Long id, @RequestParam Integer quantity) {
        if (id == null || quantity == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "ID and quantity cannot be null"));
        }
        if (quantity <= 0) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Quantity must be greater than 0"));
        }
        return ResponseEntity.ok(itemService.sellItem(id, quantity));
    }

    /**
     * Retrieves a report of current stock levels.
     *
     * @return a list of items in stock
     */
    @GetMapping("/report")
    public ResponseEntity<List<Item>> getStockReport() {
        List<Item> items = itemService.getStockReport();
        return ResponseEntity.ok(items); // Always return OK with the list (empty or not)
    }

    /**
     * Retrieves the audit trail of item transactions.
     *
     * @return a list of item audits
     */
    @GetMapping("/audit")
    public ResponseEntity<List<ItemAudit>> getAuditTrail() {
        List<ItemAudit> audits = auditRepository.findTop50ByOrderByTimestampDesc();
        return ResponseEntity.ok(audits); // Always return OK with the list (empty or not)
    }
}