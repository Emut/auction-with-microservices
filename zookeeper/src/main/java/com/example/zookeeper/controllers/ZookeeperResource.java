package com.example.zookeeper.controllers;

import com.example.zookeeper.ZookeeperService;
import com.example.zookeeper.dto.EventDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZookeeperResource {

    private final ZookeeperService zookeeperService;

    public ZookeeperResource(ZookeeperService zookeeperService) {
        this.zookeeperService = zookeeperService;
    }

    @GetMapping("/instances")
    List<EventDto> getEvents() {
        return zookeeperService.getEvents();
    }
}
