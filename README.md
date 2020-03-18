# GeoCode Backend

## Running the Backend

### Before You Start

- Setup a `mariadb` server, this is the dbms we will use for this project.
    - Create an empty database
    - Create a user for the new database, for example I used `geocode-dev`. A localhost user should be enough.

- Create a `.env` file in the project root, this should be based upon `.env.template`. Edit the file where needed.
- If you are on windows create a `.env.bat` file in the project root, this should be based upon `.env.bat.template`. Edit the file where needed

### Start the server

To start the webserver simply run `./runapp.sh bootrun`.  
Or if you are on windows, you need to run this command `cmd.exe /c runapp.bat bootrun` 

To visit the swagger documentation, surf to [http://localhost:8080/swagger/swagger-ui.html](http://localhost:8080/swagger/swagger-ui.html)

## URL's
https://geocode.ga/

https://dev.geocode.ga/
