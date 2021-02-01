package com.example.userservice.controllers;

import com.example.userservice.dto.ItemDto;
import com.example.userservice.services.AuctionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuctionResource {

    private final AuctionService auctionService;

    public AuctionResource(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/auction-status")
    public void auctionStatus(@RequestParam Boolean isStart) {
        auctionService.startAuction(isStart);
    }

    @PostMapping("/place-bid")
    public ItemDto placeBid(@RequestBody ItemDto itemDto) {
        return auctionService.placeBid(itemDto);
    }
}

