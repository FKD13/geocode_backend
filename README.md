# Backend
=======
# GeoCode Backend

## Running the Backend

### Before You Start

- Setup a `mariadb` server, this is the dbms we will use for this project.
    - Create an empty database
    - Create a user for the new database, for example I used `geocode-dev`. A localhost user should be enough.

- Create a `.env` file in the project root, this should be based upon `.env.template`. Edit the file where needed.

### Start the server

to start the webserver simply run `./runapp.sh bootrun`.