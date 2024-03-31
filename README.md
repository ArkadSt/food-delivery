# food-delivery

Calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weatherconditions.

## Usage

The program is not case-sensitive in respect to the parameters that you give it.

You can calculate a fee using a GET-request:

`localhost:8080/get-fee/{City}/{Vehicle}`</br>
such as:</br>
`localhost:8080/get-fee/Tallinn/Bike`

which would calculate the courier delivery fee for cyclists in Tallinn in current weather conditions.

If you need fees from paticular time, you can use either:

`http://localhost:8080/get-fee/Tartu/Car/timestamp/1711908900`</br>
or</br>
`http://localhost:8080/get-fee/Tartu/Car/datetime/2024-03-31T21:15:00`

You can add, edit and delete business rules using REST interface.</br>
Some examples:

`curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "city=Narva&vehicle=Bike&fee=3.0" http://localhost:8080/base-fee` - adds new RBF

`mcurl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "city=Narva&vehicle=Bike&fee=3.5" http://localhost:8080/base-fee` - modifies it

`curl -X DELETE http://localhost:8080/delete-wpef/Bike/Rain` - deletes a business rule associated with bike couriers in rainy weather


The database is being updated every 15 minutes by default, but you can also set your own schedule by updating the cron expression in Spring's application.properties (food-delivery/src/main/resources/application.properties).
