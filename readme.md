# Learn Raspberry Pi IO  

Some sample code as I learned to play with the GPIO and other things in the Raspberry Pi.  

## Development  

### Select Java 11  

```bash
sdk use java 11.0.1-open
```

### Building the Code  

```bash
mvn clean package
```

### Running the Tests  

```bash
mvn clean test
```

## Running the application  

```bash
java -cp target/learnrpi-*.jar com.tddapps.learnrpi.Program
```
