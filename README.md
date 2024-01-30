# Medical Equipment Location Simulator

## Description
This project entails the development of an application that simulates the real-time movement of delivery vehicles for medical equipment, utilizing RabbitMQ as the messaging platform. The application generates coordinates representing the vehicle's path from point A to point B and dispatches them for asynchronous processing through message queues. The processed coordinates are then relayed to the company's central system responsible for distributing medical equipment. The main system visualizes this information on a map. For a detailed understanding, refer to the example communication flow diagram provided bellow.

![image](https://github.com/anastasija1m/isa-med-equipment-location-simulator/assets/83400057/b3a062c9-bc22-409c-9981-fb37adbb52a2)

## Installation and Usage
* Download and Install [RabbitMQ.](https://www.rabbitmq.com/download.html)
* Start RabbitMQ Server.
* Open IntelliJ IDEA.
* Click on File -> Open and select your project folder. IntelliJ IDEA should automatically recognize it as a Maven project.
* Install Dependencies from pom.xml file.
* Add necesary queues in [RabbitMQ Management Interface.](http://localhost:15672/)
* Make sure you are running [Main application.](https://github.com/nina-bu/isa-med-equipment-be)
* Run the application.
