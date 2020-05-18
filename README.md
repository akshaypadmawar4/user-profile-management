# user-profile-management
Event driven micro services project

### 1-Authorization Service:
This micro service is responsible for Authorizing the User by Username and Password and serves as API Gateway.For authentication JWT is implemented.
### 2-User Profile Service:
This is a event driven microservice listen to events publish by Authorization service to perform operation on user profile data. 

### API Exposed:

###### POST /login 
provide username and password in the post body. User will be authenticated and jwt token is return

###### POST /profile 
Provide jwt token in request header and this service will authenticate user.
If user gets authorized with provided data, It will call User Profile Service using Open Feign to persist user details in database.

###### PUT /profile (Event based) 
Provide jwt token in request header and this service will authenticate user.
If user gets authorized with provided data, Update Event is posted on User-Profile-Service using kafka and user data get updated.


###### DELETE /profile (Event based)
Provide jwt token in request header and Authorization Service will authorize user.
If user gets authorized with provided data,Delete Event is posted on User-Profile-Service using kafka and user data get deleted.


***User Data is maintained in h2 in memory database.***

***data.sql file is used to store username and password in bcrypt encoded format.***


## Pre-requisites:

kafka must be installed in system.

Start kafka zookeeper and kafka broker using below commands ,

###### To start Zoo-keeper,
zookeeper-server-start.bat ..\..\config\zookeeper.properties

###### To start kafka broker,
kafka-server-start.bat ..\..\config\server.properties

###### Create kafka broker cluster using port 
localhost:9092,localhost:9093,localhost:9094

###### h2-database console is available on below url,
http://localhost:9091/h2-console

## How to access exposed API endpoints,

###### For Login:

http://localhost:9090/login POST

Body:
{
"username":"Akshay",
"password":"hello"
}

Response:JWT Token

###### For Profile Creation,

http://localhost:9090/profile POST

Header:
[{"key":"Authorization","value":"Bearer {JWT Token}"]

Body:
{
    "address": "Hinjewadi",
    "phoneNumber": 8888016421
}

###### For Profile Updation,

http://localhost:9090/profile PUT

Header:
[{"key":"Authorization","value":"Bearer {JWT Token}"]

Body:
{   "id": 1,
    "address": "Hinjewadi",
    "phoneNumber": 8888016421
}

###### For Profile Deletion:

http://localhost:9090/profile/{id} DELETE

Header:
[{"key":"Authorization","value":"Bearer {JWT Token}"]
