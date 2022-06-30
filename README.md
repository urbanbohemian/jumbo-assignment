# Jumbo Store Locator UI

URL below can be used for displaying the Store Locator UI, any location (city name, district name or neighbourhood etc.) can be written in the search location text box for locating nearby stores on the map)
http://localhost:8080/storelocator


# Jumbo Store Locator Rest API
Simple REST based application for querying closest Jumbo Stores.

Application has an endpoint that returns closest stores with given latitude, longitude, number_of_stores and calculation_type:

`GET /stores/closest?latitude={latitude}&longitude={longitude}&number_of_stores={number_of_stores}&distance_calculation_type={distance_calculation_type}`

Application also has endpoints enabling crud operations , all the services exposed can be found under swagger GUI
## Usage

To run the application, just run this command under the base directory:

`mvn spring-boot:run`

## Swagger

You can try the closest store endpoint using SwaggerUI:

http://localhost:8080/swagger-ui.html