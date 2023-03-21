# food-delivery

You can calculate a fee using GET-requests, such as

`localhost:8080/get-fee/Tallinn/Bike`

Which would calculate the courier delivery fee for cyclists in Tallinn in current weather conditions.

The database is being updated every 15 minutes by default, but you can also set your own schedule by updating the cron expression in Spring's application.properties.
