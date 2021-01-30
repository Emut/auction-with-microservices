package com.example.userservice.controllers;

import com.example.userservice.dto.ItemDto;
import com.example.userservice.services.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final InventoryService inventoryService;

    public InventoryResource(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/items")
    public List<ItemDto> getAllItems() {
        return inventoryService.getAllItems();
    }

    @PostMapping("/item")
    public void addItem(@RequestBody ItemDto itemDto) {
        inventoryService.addItem(itemDto);
    }

}
