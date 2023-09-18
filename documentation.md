# Documentation of my study project 

## About

For some years I'm a volunteer of the "Együtt a Kisállatokért Alapítvány" (Together For The Small Pets Foundation). We 
rescuee and rehome the pets, whom the owners don't want to take anymore. 
This project is build about the Hamster Resque.

---

## Structure

### `Hamster` entity

The `Hamster`entity has the following attributes: 

* `id` - the hamster's unique identifier (Long)
* `name` - arbitrarily name of the hamster (String)
* `hamsterSpecies` - species of the hamster (Enum)
* `color` - color of the hamster (String)
* `gender` - gender of the hamster (Enum)
* `dateOfBirth` - date of birth (can be estimated) (LocalDate)
* `hamsterStatus` - status of the adoptability (Enum)
* `host` - entity of the temporary host, who is taking care of the hamster (Host)
* `startOfFostering` - date of the hamster's arrive (LocalDate)
* `adopter` - entity of the new owner (Adopter)
* `dateOfAdoption` - date of the adoption (LocalDate)
* `pictures` - list of the pictures of the hamster (List<Pictures>)
* `weeklyReports` - reports of the hamster from the hosts (List<WeeklyReport>)

##### Endpoints in API:

| HTTP method | Endpoint                       | Description                                      |
|-------------|--------------------------------|--------------------------------------------------|
| GET         | `"/api/hamsters"`              | list of ALL hamster in the database              |
| GET         | `"/api/hamsters/fostering"`    | list of current, adoptable hamster               |
| GET         | `"/api/hamsters/{id}"`         | display a hamster by `id`                        |
| POST        | `"/api/hamsters"`              | create a new hamster and add to an existing host |
| PUT         | `"/api/hamsters/{id}"`         | update a hamster attributes                      |
| PUT         | `"/api/hamsters/{id}/adopted"` | set the hamster's status to adopted              |


##### Endpoints in Thymeleaf:

| HTTP method   | Endpoint                        | Description                                      |
|---------------|---------------------------------|--------------------------------------------------|
| GET           | `"/hamsters/current_hamsters"`  | list of current, adoptable hamster               |
| GET           | `"/hamsters/create_hamster"`    | create a new hamster and add to an existing host |
| GET           | `"/hamsters/hamster_page/{id}"` | display a hamster by `id`                        |


---

### Temporary `Host` entity

The `Host` entity has the following attributes:

* `id` - the temporary host's unique identifier (Long) 
* `name` - name of the temporary host (String)
* `address` - address of the temporary host (Address) 
* `contacts` - contact information of the temporary host (Contacts)
* `capacity` - numbers of the places where the temporary host can place a hamster (int)
* `hamsters` - list of the temporary host hamsters (List<Hamster>)
* `weeklyReports` - list of the reports about the hamsters (List<WeeklyReport>)
* `hostStatus` - status of that the temporary host can or can't take care a hamster (Enum)

The `Hamster` and the `Host` are in a two-way 1-n relationship.

##### Endpoints in API:

| HTTP method  | Endpoint                     | Description                                                        |
|--------------|------------------------------|--------------------------------------------------------------------|
| GET          | `"/api/hosts"`               | list of the hosts, filterable by name                              |
| GET          | `"/api/hosts/{id}"`          | display a host by `id`                                             |
| GET          | `"/api/hosts/{id}/bycity"`   | list of hosts filtered by location                                 |
| GET          | `"/api/hosts/{id}/hamsters"` | list of the host currently fostered hamsters                       |
| POST         | `"/api/hosts"`               | create a new temporary host and add to the database                |
| PUT          | `"/api/hosts/{id}"`          | update a temporary host's attributes                               |
| PUT          | `"/api/hosts/{id}/inactive"` | set the temporary host status to inactive                          |


##### Endpoints in Thymeleaf:

| HTTP method | Endpoint                                     | Description                                         |
|-------------|----------------------------------------------|-----------------------------------------------------|
| GET         | `"/hosts/add_new_host"`                      | create a new temporary host and add to the database |
| GET         | `"/hosts/host_current_hamsters/{id}"`        | list of the host's currently fostered hamsters      |
| GET         | `"/hosts/current_hosts"`                     | list of the hosts, filterable by name               |
| GET         | `"/hosts/current_hosts_free_capacity"`       | list of the hosts with free capacity                |
| POST        | `"/hosts/current_hosts_by_city/{city}"`      | list of hosts filtered by location                  |
| PUT         | `"/hosts/hosts_by_name_and_hamsters/{name}"` | list of hosts with hamsters, filtered by name       |


---

### `Adopter` entity 

The `Adopter` entity has a following attributes:

* `id` - the adopters host's unique identifier (Long)
* `name` - name of the adopter (String)
* `address` - address of the adopter (Address)
* `contacts` - contact information of the adopter (Contacts)
* `hamsters` - list of the adopter's adopted hamsters (List<Hamster>)

The `Hamster` and the `Adoptive` are in a two-way 1-n relationship.

##### Endpoints in API:

| HTTP method | Endpoint                          | Description                              |
|-------------|-----------------------------------|------------------------------------------|
| GET         | `"/api/adopters"`                 | list of all adopters, filterable by name |
| GET         | `"/api/adopters/adoptivesbycity"` | list of adopters filtered by location    |
| GET         | `"/api/adopters/{id}"`            | display an adopter by `id`               |
| GET         | `"/api/adopters/{id}/hamsters"`   | list of the adopter's hamsters           |
| POST        | `"/api/adopters"`                 | create a new adopter                     |
| PUT         | `"/api/adopters/{id}"`            | update the adopter's attributes          |
| DELETE      | `"/api/adopters/{id}"`            | delete the adopter*                      |


The delete of the adopter can be executed only if the adopter's hamster list is empty. 

---

## Technical details 

I created a three layer application with a MariaDb database, that can be start from a Docker container. The application and a test classes uses different database.

All of the entities have an own Controller, Service, Repository layers. The database schema is created by Liquibase with sql files. The inserted datas are validate with a built-in validation processes. Most of the exceptions are handled by the built-in handlers and personal exception classes.
The application has a Swagger UI, where simple functions can be try.

---

## Availability via the Internet 

I create HTML pages with CSS and JS and a Thymeleaf template. It's not ready yet.  