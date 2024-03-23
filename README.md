# firstjobapp
Spring Boot (Monolithic) Application containing Company, Job & Review related Endpoints.


## Spring boot with Docker 
    - Containerizing Spring Boot App without docker file
    - By using spring boot plugin - spring-boot:build-image

Docker Registry : https://hub.docker.com/

My Account : https://hub.docker.com/repository/docker/arjunb95

### creating docker image for firstjobapp via maven command

Reference : https://spring.io/guides/topicals/spring-boot-docker


Set Java home if mvn shows warning of java home :
    
    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ export JAVA_HOME=/home/arjunb/.jdks/azul-17.0.10

Now check maven version -
    
    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ ./mvnw --version
    Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
    Maven home: /home/arjunb/.m2/wrapper/dists/apache-maven-3.9.5-bin/32db9c34/apache-maven-3.9.5
    Java version: 17.0.10, vendor: Azul Systems, Inc., runtime: /home/arjunb/.jdks/azul-17.0.10
    Default locale: en_IN, platform encoding: UTF-8
    OS name: "linux", version: "6.5.0-26-generic", arch: "amd64", family: "unix"

Now also check docker is working - 
    
    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker images
    REPOSITORY                                       TAG       IMAGE ID       CREATED         SIZE
    image-db                                         latest    0999dac93576   3 weeks ago     16GB
    container-registry.oracle.com/database/express   latest    8da8cedb7fbf   7 months ago    11.4GB
    hello-world                                      latest    d2c94e258dcb   10 months ago   13.3kB

### Build image for firstjobapp
    
    ../firstjobapp$./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=arjunb95/jobappimage

sample output :
    
    [INFO] Successfully built image 'docker.io/arjunb95/jobappimage:latest'
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  03:45 min
    [INFO] Finished at: 2024-03-22T15:05:10+05:30
    [INFO] ------------------------------------------------------------------------


### Pushing this image to docker hub OR registry

    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker login
    
    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker push arjunb95/jobappimage

### Running docker image jobappimage

    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker run -p 8080:8080 arjunb95/jobappimage

Running image in detach mode :
    
    arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker run -d -p 8080:8080 arjunb95/jobappimage


### Setting Postgres DB & pgAdmin on docker [without docker-compose file]

1. create docker network : It helps to communicate across container's.
    
        $ docker network create my-network

2. Run postgres db image with my-network :

        $ docker run -d --name db --network my-network -e POSTGRES_PASSWORD=mysecretpassword postgres

3. Run pgadmin image with my-network :

       $ docker run -d --name pgadmin --network my-network -e PGADMIN_DEFAULT_EMAIL=user@admin.com -e PGADMIN_DEFAULT_PASSWORD=SuperSecret dpage/pgadmin4

       $ docker ps
       CONTAINER ID   IMAGE            COMMAND                  CREATED              STATUS              PORTS             NAMES
       c1e3673482eb   dpage/pgadmin4   "/entrypoint.sh"         About a minute ago   Up About a minute   80/tcp, 443/tcp   pgadmin
       faa328a58239   postgres         "docker-entrypoint.s…"   9 minutes ago        Up 9 minutes        5432/tcp          db

4. Check network between pgadmin & db :

       $ docker exec -it pgadmin ping db
        
       PING db (172.18.0.2): 56 data bytes
        64 bytes from 172.18.0.2: seq=0 ttl=42 time=0.250 ms
        64 bytes from 172.18.0.2: seq=1 ttl=42 time=0.179 ms
        64 bytes from 172.18.0.2: seq=2 ttl=42 time=0.238 ms

### Docker compose :
We can run multiple image via single docker file. 

1. Installing docker-compose on ubuntu via terminal :
   
         $ sudo apt install docker-compose

2. Under application root folder (i.e. /firstjobapp) create file 'docker-compose.yml' add containers details as below :

         services:
            postgres:
               container_name: postgres_container
               image: postgres
               environment:
                  POSTGRES_USER: parjunb
                  POSTGRES_PASSWORD: parjunb
                  PGDATA: /data/postgres
               volumes:
                  - postgres:/data/postgres
               ports:
                  - "5432:5432"
               networks:
                  - postgres
               restart: unless-stopped

            pgadmin:
               container_name: pgadmin_container
               image: dpage/pgadmin4
               environment:
                  PGADMIN_DEFAULT_EMAIL: ${PGADMIN_DEFAULT_EMAIL:-pgadmin4@pgadmin.org}
                  PGADMIN_DEFAULT_PASSWORD: ${PGADMIN_DEFAULT_PASSWORD:-admin}
                  PGADMIN_CONFIG_SERVER_MODE: 'False'
               volumes:
                  - pgadmin:/var/lib/pgadmin
               ports:
                 - "5050:80"
               networks:
                 - postgres
               restart: unless-stopped

         networks:
            postgres:
               driver: bridge

         volumes:
            postgres:
            pgadmin:

3. Run docker-compose command :

       arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker-compose up -d

4. Check containers running or not :

         arjunb@arjunb-Vostro-3480:~/git-repo/firstjobapp$ docker ps
         CONTAINER ID   IMAGE            COMMAND                  CREATED          STATUS          PORTS                                            NAMES
         df0e40503ee5   dpage/pgadmin4   "/entrypoint.sh"         10 minutes ago   Up 10 minutes   443/tcp, 0.0.0.0:5050->80/tcp, :::5050->80/tcp   pgadmin_container
         f7438ddac008   postgres         "docker-entrypoint.s…"   18 minutes ago   Up 10 minutes   0.0.0.0:5432->5432/tcp, :::5432->5432/tcp        postgres_container

