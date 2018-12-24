# BookConferenceRestApp
Rest app for booking conference rooms, and user management.

Installing with Maven
```
mvn clean install
```

**Rest API**
---
All endpoints has prefix /api

Example: localhost:8080/api/user/available

**User**

Endpoint: `/user`
* `/available`
    * method: GET
    * url params: none
    * data params: none
    * Success response:
        * Code: 200 <br>
        Content: json array <br> example:
        `[{"name":"John","surname":"Smith","login":"jsmith"},{"name":"Jane","surname":"Doe","login":"jdoe"}]` <br>
* `/create`
    * method: POST
    * headers: x-api-key: admin password
    * url params: none
    * data params: UserCreateDTO object <br> example:
    `{
       "name": "Mark",
       "surname": "Bloom",
       "login": "mbloom",
       "password": "qazwsx"
     }`
     * Success response:
        * Code: 200 <br>
        Content: `{"success":true,"message":"User created successfully.","errors":null}`
     * Error response:
        * Code: 500 <br>
        Content: `{
                      "timestamp": "2018-12-24T12:46:53.826+0000",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "createUser.userCreateDTO.login: must not be blank",
                      "path": "/api/user/create"
                  }`
        * Code: 200 <br>
        Content: `{
                              "success": false,
                              "message": "Login is already taken.",
                              "errors": null
                          }`
* `/edit`
    * method: PUT
    * headers: x-api-key: admin password
    * url params: none
    * data params: UserEditDTO object <br>
    example: `{
                "name": "John",
                "surname": "Bloom",
                "login": "mbloom",
                "password": "123456"
              }`
    * Success response:
        * Code: 200 <br>
        Content: `{"success":true,"message":"User edited successfully.","errors":null}`
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "User not found login: msmith"
                      ]
                  }`
        <br> OR <br>
        * Code: 500 <br>
        Content: `{
                      "timestamp": "2018-12-24T12:57:07.103+0000",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "editUser.userEditDTO.login: must not be blank",
                      "path": "/api/user/edit"
                  }`
* `/delete`
    * method: DELETE
    * headers: x-api-key: admin password
    * url params: userLogin <br>
    example: `userLogin=jsmith`
    * data params: none
    * Success response: 
        * Code: 200 <br>
        Content: `{
                      "success": true,
                      "message": "User deleted successfully.",
                      "errors": null
                  }`
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "User not found login: json"
                      ]
                  }`

Endpoint: `/room`
* `/available`
    * method: GET
    * url params: none
    * data params: none
    * Success response:
        * Code: 200 <br>
        Content: json array <br> example:
        `[
             {
                 "name": "Large Room",
                 "locationDescription": "1st floor",
                 "numberOfSeats": 10,
                 "hasProjector": true,
                 "phone": "22-22-22-22"
             },
             {
                 "name": "Medium Room",
                 "locationDescription": "1st floor",
                 "numberOfSeats": 6,
                 "hasProjector": true,
                 "phone": ""
             }
         ]` <br>
* `/create`
    * method: POST
    * headers: x-api-key: admin password
    * url params: none
    * data params: RoomCreateDTO object <br> example:
    `{
     	"name": "Largest Room",
     	"locationDescription": "3rd floor",
     	"numberOfSeats": 14,
     	"hasProjector": false,
     	"phone": "123-456-789"
     }`
     * Success response:
        * Code: 200 <br>
        Content: `{
                      "success": true,
                      "message": "Room created successfully.",
                      "errors": null
                  }`
     * Error response:
        * Code: 500 <br>
        Content: `{
                      "timestamp": "2018-12-24T13:03:34.993+0000",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "createRoom.roomCreateDTO.numberOfSeats: must not be null",
                      "path": "/api/room/create"
                  }`
        * Code 200 <br>
        Content: `{
                      "success": false,
                      "message": "Name is already taken.",
                      "errors": null
                  }`
* `/edit`
    * method: PUT
    * headers: x-api-key: admin password
    * url params: none
    * data params: RoomEditDTO object <br>
    example: `{
              	"name": "Largest Room",
              	"locationDescription": "3rd floor",
              	"numberOfSeats": 14,
              	"hasProjector": true,
              	"phone": "987-654-321"
              }`
    * Success response:
        * Code: 200 <br>
        Content: `{"success":true,"message":"Room edited successfully.","errors":null}`
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "Room not found name: Largesst Room"
                      ]
                  }`
        * Code: 500 <br>
        Content: `{
                      "timestamp": "2018-12-24T13:07:31.246+0000",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "editRoom.roomEditDTO.name: must not be blank",
                      "path": "/api/room/edit"
                  }`
* `/delete`
    * method: DELETE
    * headers: x-api-key: admin password
    * url params: roomName <br>
    example: `roomName=Large Room`
    * data params: none
    * Success response: 
        * Code: 200 <br>
        Content: `{
                      "success": true,
                      "message": "Room deleted successfully.",
                      "errors": null
                  }`
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "Room not found name: room small"
                      ]
                  }`
                  
Endpoint: `/book`
* `/`
    * method: POST
    * url params: none
    * data params: BookingCreateDTO object <br>
    example: `{
              	"login": "jdoe",
              	"password": "mySecret",
              	"room": "Large Room",
              	"dateFrom": "2018-12-24 16:30:00",
              	"dateTo": "2018-12-24 18:30:00"
              }`
    * Success response:
        * Code: 200 <br>
        Content: `{
                      "success": true,
                      "message": "Successfully booked room.",
                      "errors": null
                  }`
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Bad login credentials.",
                      "errors": null
                  }`
        *Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "Room not found name: Large Rsoom"
                      ]
                  }`
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Date to must be later than date from.",
                      "errors": null
                  }`
        * Code: 500 <br>
        Content: `{
                      "timestamp": "2018-12-24T13:19:33.777+0000",
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "bookRoom.bookingCreateDTO.dateTo: must not be null",
                      "path": "/api/book"
                  }`
                  

* `/all`
    * method: GET
    * url params: dateFrom (optional), dateTo (optional)
    * data params: none
    * Success response:
        * Code: 200 <br>
        Content: json array <br> example:
        `[
             {
                 "user": {
                     "name": "Jane",
                     "surname": "Doe"
                 },
                 "room": {
                     "name": "Large Room"
                 },
                 "dateFrom": "2018-12-24 16:30:00",
                 "dateTo": "2018-12-24 18:30:00"
             },
             {
                 "user": {
                     "name": "John",
                     "surname": "Smith"
                 },
                 "room": {
                     "name": "Medium Room"
                 },
                 "dateFrom": "2018-12-24 16:30:00",
                 "dateTo": "2018-12-24 18:30:00"
             }
         ]` <br>
* `/user`
    * method: GET
    * url params: userName (required), dateFrom (optional), dateTo (optional)
    * data params: none
    * Success response:
        * Code: 200 <br>
        Content: json array <br> example:
        `[
             {
                 "user": {
                     "name": "John",
                     "surname": "Smith"
                 },
                 "room": {
                     "name": "Medium Room"
                 },
                 "dateFrom": "2018-12-24 16:30:00",
                 "dateTo": "2018-12-24 18:30:00"
             }
         ]` <br>
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "User not found name: Jake"
                      ]
                  }`
* `/room`
    * method: GET
    * url params: roomName (required), dateFrom (optional), dateTo (optional)
    * data params: none
    * Success response:
        * Code: 200 <br>
        Content: json array <br> example:
        `[
             {
                 "user": {
                     "name": "Jane",
                     "surname": "Doe"
                 },
                 "room": {
                     "name": "Large Room"
                 },
                 "dateFrom": "2018-12-24 16:30:00",
                 "dateTo": "2018-12-24 18:30:00"
             }
         ]` <br>
    * Error response:
        * Code: 200 <br>
        Content: `{
                      "success": false,
                      "message": "Error occurred",
                      "errors": [
                          "Room not found name: Largse Room"
                      ]
                  }`     


---
**Models**
* UserCreateDTO
    * name: string
    * surname: string
    * login: string
    * password: string
    
* UserEditDTO
    * name: string
    * surname: string
    * login: string
    * password: string
    
* RoomCreateDTO
    * name: string
    * lcationDescription: string
    * numberOfSeats: number
    * hasProjector: boolean
    * phone: string
    
* RoomEditDTO
    * name: string
    * lcationDescription: string
    * numberOfSeats: number
    * hasProjector: boolean
    * phone: string
    
* BookingCreateDTO
    * login: string
    * password: string
    * room: string
    * dateFrom: LocalDateTime
    * dateTo: LocalDateTime
