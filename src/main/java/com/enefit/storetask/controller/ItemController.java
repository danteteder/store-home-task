package com.enefit.storetask.controller;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

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
}