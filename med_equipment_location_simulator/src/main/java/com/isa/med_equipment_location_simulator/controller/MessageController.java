package com.isa.med_equipment_location_simulator.controller;

import com.isa.med_equipment_location_simulator.dto.LocationDto;
import com.isa.med_equipment_location_simulator.dto.StartDto;
import com.isa.med_equipment_location_simulator.rabbitMQ.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mq")
public class MessageController {

    private RabbitMQProducer producer;

    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

//    @PostMapping("/publish")
//    public ResponseEntity<String> sendMessage(@RequestBody LocationDto location){
//        producer.sendMessage(location);
//        return ResponseEntity.ok("Message sent!");
//    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestBody StartDto start){
        producer.startSimulation(start);
        return ResponseEntity.ok("Started!");
    }
}
