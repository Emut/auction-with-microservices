package com.example.userservice.controllers;

import com.example.userservice.dto.ItemDto;
import com.example.userservice.services.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
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
        inventoryService.getAllItems();
        return null;
    }

}
