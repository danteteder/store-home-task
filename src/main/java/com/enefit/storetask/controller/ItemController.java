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
 * REST controller for managing store items
 */
@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemAuditRepository auditRepository;

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.addItem(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<Item> sellItem(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(itemService.sellItem(id, quantity));
    }

    @GetMapping("/report")
    public ResponseEntity<List<Item>> getStockReport() {
        return ResponseEntity.ok(itemService.getStockReport());
    }

    @GetMapping("/audit")
    public ResponseEntity<List<ItemAudit>> getAuditTrail() {
        return ResponseEntity.ok(auditRepository.findTop50ByOrderByTimestampDesc());
    }
}