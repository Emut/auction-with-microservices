package com.example.zookeeper;

import com.example.zookeeper.dto.EventDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZookeeperService {

    private List<EventDto> events = new ArrayList<>();

    @KafkaListener(topics = "zookeeper")
    public void listenGroup(String message) {
        System.out.println("Received Message in group: " + message);
        String[] args = message.split(",");
        EventDto eventDto = new EventDto();
        eventDto.setInstance(args[0]);
        eventDto.setPort(Integer.valueOf(args[1]));
        boolean isOn = args[2].equals("ON");
        if (isOn) {
            events.add(eventDto);
        } else {
            events = events.stream().filter(eventDto1 -> !eventDto1.getInstance()
                    .equals(eventDto.getInstance())).collect(Collectors.toList());
        }
    }

    public List<EventDto> getEvents() {
        return events;
    }
}
