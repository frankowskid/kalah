### Kalah game

##Compile:
```
mvn package
```
##Test
```
mvn test
```
for mutation testing
```json
mvn org.pitest:pitest-maven:mutationCoverage
```

##Run:
java -jar ./target/kalahgame-1.0-SNAPSHOT.jar 

##Healthcheck
By default, app starts on: 8080 port

You can check readiness with:

```console
curl localhost:8080/actuator/health
```

```json
{"status":"UP"}
```


##To Play:
#Create new game:
```console
curl --header "Content-Type: application/json" \
--request POST \
http://<host>:<port>/games
```

#Make a move
```console
curl --header "Content-Type: application/json" \
--request PUT \
http://<host>:<port>/games/{gameId}/pits/{pitId}
```


##API
Application API can be found under:
```
http://localhost:8080/swagger-ui/
```