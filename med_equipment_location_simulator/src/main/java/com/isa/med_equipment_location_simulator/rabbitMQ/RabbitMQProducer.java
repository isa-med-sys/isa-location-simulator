package com.isa.med_equipment_location_simulator.rabbitMQ;

import com.isa.med_equipment_location_simulator.LocationDto;
import com.isa.med_equipment_location_simulator.StartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
    private RabbitTemplate rabbitTemplate;
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(LocationDto location){
        LOGGER.info(String.format("Message -> %s", location.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, location);
    }

    public void startSimulation(StartDto start){
        LOGGER.info(String.format("Starting -> %s", start.toString()));
        Float sLatitude = start.getLatitudeStart();
        Float sLongitude = start.getLongitudeStart();
        Long uId = start.getUserId();
        for(int i = 0; i < 19; i++) {
            LocationDto loc = new LocationDto(uId, sLatitude, sLongitude);
            sendMessage(loc);
            sLatitude+=1;
            sLongitude+=1;
        }
        LocationDto loc = new LocationDto(uId, start.getLatitudeEnd(), start.getLatitudeEnd());
        sendMessage(loc);
    }
}
