start java -jar eureka.jar
start java -jar first-level-service.jar
start java -jar first-level-service.jar
start java -jar second-level-service.jar
start java -jar second-level-service.jar
start java -jar second-level-service.jar
start java -jar turbine.jar
start java -jar zuul.jar

start "" "http://127.0.0.1:5555/hystrix/monitor?stream=http%%3A%%2F%%2F127.0.0.1%%3A5555%%2Fturbine.stream%%3Fcluster%%3Ddefault"
start "" "http://localhost:8761/"
start "" "http://localhost:8080/first-level-service/getRandomHash?arg=fjsdklfjdskl"