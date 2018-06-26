# Welcome to the Generic Display Wiki

## Maven 

### Artifact

```xml
<groupId>org.openbase</groupId>
<artifactId>generic-display</artifactId>
<version>[0.6,0.7-SNAPSHOT)</version>
```

## How to install

Download source from github

`git clone https://github.com/openbase/generic-display.git`

and build the source via maven

```
mvn clean install
```

## How to start the server 

Start the server and open the view by executing
```
./target/appassembler/bin/generic-display
```

## How to remote control the server
```java
// Init remote instance
        DisplayRemote remote = new DisplayRemote();
        remote.init();
        remote.activate();
        remote.waitForData();

// Display text example
        remote.showText("Hello World");
```
For a more detail java example have a look at the [DisplayRemoteExample](https://github.com/openbase/generic-display/tree/master/src/main/java/org/openbase/display/DisplayRemoteExample.java) class.


The example code can executed with the following command
```
./target/appassembler/bin/generic-display-test
```
