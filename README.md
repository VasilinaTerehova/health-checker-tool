# health-checker-tool
Cluster health checker tool

to build project please use maven 
```
$ mvn clean install -Pall
```

after build please use controller\target\health-checker-tool-controller-0.0.1-SNAPSHOT-bin.zip

After unzipping the archive you will find health-tool-start.bat to start application

You will also need mysql database, class Dataload will generate schemas, only please update controller\src\main\resources\clusters.json with real credentials for cluster

All database properties and port for start are in application.properties file controller\src\main\resources\application.properties

Full technical documentation available at https://docs.google.com/document/d/1VmCzZYXfar-Z1Xn-EjCZJyu6Vdl1WogCUSQMw7nGHh4

```
$ mvn clean install - will rebuilt only server side
```