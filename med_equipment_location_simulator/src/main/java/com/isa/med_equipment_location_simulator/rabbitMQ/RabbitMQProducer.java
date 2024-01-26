package com.isa.med_equipment_location_simulator.rabbitMQ;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.isa.med_equipment_location_simulator.dto.DeliveryStartDto;
import com.isa.med_equipment_location_simulator.dto.LocationDto;
import com.isa.med_equipment_location_simulator.dto.StartDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;

    @Value("${rabbitmq.simulation.exchange.name}")
    private String exchange1;
    @Value("${rabbitmq.simulation.routing.key.name}")
    private String routingKey1;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
    private final RabbitTemplate rabbitTemplate;
    private final GeoApiContext geoApiContext;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate, @Value("${google.maps.api.key}") String apiKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.geoApiContext = new GeoApiContext.Builder().apiKey(apiKey).build();
    }

    public void sendMessage(LocationDto location) {
        LOGGER.info("Message -> {}", location);
        rabbitTemplate.convertAndSend(exchange, routingKey, location);
    }

    public void sendDeliveryStartMessage(DeliveryStartDto deliveryStart) {
        LOGGER.info("Delivery Started -> {}", deliveryStart);
        rabbitTemplate.convertAndSend(exchange1, routingKey1, deliveryStart);
    }

    public void startSimulation(StartDto start) {
        LOGGER.info("Starting -> {}", start);

        List<LatLng> coordinates = getCoordinates(start.getLatitudeStart(), start.getLongitudeStart(),
                start.getLatitudeEnd(), start.getLongitudeEnd());

        long totalDurationSeconds = calculateTotalDuration(start.getLatitudeStart(), start.getLongitudeStart(),
                start.getLatitudeEnd(), start.getLongitudeEnd());

        DeliveryStartDto deliveryStart = new DeliveryStartDto(totalDurationSeconds / 60);

        sendDeliveryStartMessage(deliveryStart);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final int[] index = {0};

        executorService.scheduleAtFixedRate(() -> {
            if (index[0] < coordinates.size()) {
                LatLng coordinate = coordinates.get(index[0]);
                LocationDto loc = new LocationDto(start.getUserId(), (float) coordinate.lat, (float) coordinate.lng);
                sendMessage(loc);

                index[0]++;
            } else {
                executorService.shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    private long calculateTotalDuration(float startLat, float startLng, float endLat, float endLng) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new LatLng(startLat, startLng))
                    .destination(new LatLng(endLat, endLng))
                    .await();

            DirectionsRoute route = result.routes[0];
            DirectionsLeg leg = route.legs[0];

            return leg.duration.inSeconds;
        } catch (Exception e) {
            LOGGER.error("Error fetching total duration from Google Maps API", e);
            return 0;
        }
    }

    private List<LatLng> getCoordinates(float startLat, float startLng, float endLat, float endLng) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new LatLng(startLat, startLng))
                    .destination(new LatLng(endLat, endLng))
                    .await();

            DirectionsRoute route = result.routes[0];
            DirectionsLeg leg = route.legs[0];

            return Arrays.stream(leg.steps)
                    .flatMap(step -> step.polyline.decodePath().stream())
                    .toList();
        } catch (Exception e) {
            LOGGER.error("Error fetching coordinates from Google Maps API", e);
            return List.of();
        }
    }
}