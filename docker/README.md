# Dockerized javaPS
## Building the image
For building a javaPS docker image run `docker build -t wacodis/wps:1.x`.
## Configurations
Be sure to overwrite the properties in `wacdodis.env` just to provide the credentials for the Copernicus Open Access Hub and to set a working directory.
## Run the container
You can simply run the container with Compose.  
If you prefer `docker run` you can also use `docker run -p 8080:8080 --env-file ./wacodis.env wacodis/wps:1.x ` or execute a customized command.
