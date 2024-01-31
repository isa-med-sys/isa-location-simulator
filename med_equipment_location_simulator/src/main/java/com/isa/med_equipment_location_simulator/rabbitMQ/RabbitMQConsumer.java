package com.isa.med_equipment_location_simulator.rabbitMQ;

import com.isa.med_equipment_location_simulator.dto.StartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private RabbitMQProducer producer;

    public RabbitMQConsumer(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @RabbitListener(queues = {"${rabbitmq.starting.queue.name}"})
    public void consume(StartDto startDto){
        LOGGER.info(String.format("Received -> %s", startDto.toString()));
        producer.startSimulation(startDto);
    }
}