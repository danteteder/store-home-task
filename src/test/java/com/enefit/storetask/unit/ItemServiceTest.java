package com.enefit.storetask.unit;

import com.enefit.storetask.model.Item;
import com.enefit.storetask.repository.ItemRepository;
import com.enefit.storetask.service.ItemService;
import com.enefit.storetask.service.StoreEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private StoreEventProducer eventProducer;

    @InjectMocks
    private ItemService itemService;

    private Item testItem;

    @BeforeEach
    void setup() {
        testItem = Item.builder()
            .id(1L)
            .name("Test Item")
            .price(99.99)
            .quantity(10)
            .soldQuantity(0)
            .build();
    }

    @Test
    void shouldAddNewItem() {
        when(repository.save(any(Item.class))).thenReturn(testItem);

        Item result = itemService.addItem(testItem);

        assertNotNull(result);
        assertEquals(0, result.getSoldQuantity());
        verify(eventProducer).sendEvent(
            eq("CREATED"),
            eq(1L),
            eq("Test Item"),
            eq(99.99),
            eq(10),
            eq(10),
            anyString()
        );
    }

    @Test
    void shouldSellItemWhenEnoughStock() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));
        when(repository.save(any(Item.class))).thenReturn(testItem);

        Item result = itemService.sellItem(1L, 5);

        assertEquals(5, result.getQuantity());
        verify(eventProducer).sendEvent(
            eq("SOLD"),
            eq(1L),
            eq("Test Item"),
            eq(99.99),
            eq(5),
            eq(5),
            anyString()
        );
    }

    @Test
    void shouldFailWhenSellingMoreThanStock() {
        when(repository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThrows(IllegalArgumentException.class,
            () -> itemService.sellItem(1L, 20));
    }
} 