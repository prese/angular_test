## Tasks

Implement from scratch an Angular 7+ application using the material theme, which contains the following use case:

1. As an anonymous user I can perform a user registration. The registration data are:

-   username
-   email
-   pass
    See an example here: http://localhost:8080/#/register

2. As an anonymous user I can perform a user login. After login based on the user role, I can be identified as regular user or admin. 

3. As an admin user(no need to be created, credential admin/admin), I can see all the partking places and I can:

-   add and edit inline the parking places(dirrectly on the table rows)
-   remove parking places
-   filter to see only the parking places without date out
-   sort by date in

4. As a regular user, I can checkin or checkout from the parking.

There is no need to implement any line of code on the backed.
All the needed REST API are already implemented and you can access them by:
http://localhost:8080/#/admin/docs

# The domain model of the application looks like:

![Alt text](parking.png?raw=true 'Domain Model')

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1.  [Node.js][]: We use Node to run a development web server and build the project.
    Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
