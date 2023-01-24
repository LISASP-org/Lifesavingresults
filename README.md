# Lifesavingresults

Stores results of livesaving sports competitions

# Keycloak

## Getting Started

Go to directory `backend` in the root of this project and do the following

- Run command `docker compose up -d` to spin up a Keycloak instance and automatically have the realm data imported.
- Use a browser to navigate to http://localhost:48080/admin.
- Log in with the admin username "keycloak" and password "keycloak" (as defined in file `.env`).
- Make sure the realm "lifesaving" exists, has a client "lifesaving-results-webapp" and the users were created.

## Export Realm

When you change settings in the realm, the client or add/update users you should export these settings, such that those settings are stored in the repository and can be applied by any other developer.

- Run command to attach a shell to the running docker container `docker exec -it keycloak bash`
- Inside the docker container export all realm data using this command: `/opt/keycloak/bin/kc.sh export --realm lifesaving --users realm_file --dir /opt/keycloak/data/import/`

# WebApp

## Getting Started

Go to directory `apps/lifesaving-results` in the root of this project and run the following commands in order to start a development server. Keep it running and see it automatically refreshing the application when you change the code.

- `yarn`
- `yarn dev`

Go to http://localhost:5174/ to see the app in action.
