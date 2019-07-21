# Mancala-Game
Mancala-Kalah Sample Game in Spring Boot/Angular.



How to run application
To run Tests mvn test To run application mvn spring-boot:run(or you can run via IDE)

At the startup, Spring boot will add Players into database. For the demo, you can add more players in commandline runner in KalahgameApplication class.

Talking about database, yes we need to configure your database as well. Setup your own database & configure the driver,url,credentials in application.properties. For the demo I have configured mysql db for the same.

The port set for the tomcat is 8123. so you can connect on localhost:8123. you can change the port from application.properties.

To understand rules of the game, Visit HowToPlay page after you login.
