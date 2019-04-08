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

## Install Dependencies on the Raspberry Pi  

[How to use the Debian Backports](https://github.com/superjamie/lazyweb/wiki/Raspberry-Pi-Debian-Backports)  
[Fix missing dinmgr](https://blog.sleeplessbeastie.eu/2017/11/02/how-to-fix-missing-dirmngr/)  
[Wiring Pi](http://wiringpi.com/)  

```bash
sudo apt-get update
sudo apt-get install dirmngr -y --install-recommends

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys E0B11894F66AEC98
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 7638D0442B90D010
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 8B48AD6246925553

echo 'deb http://httpredir.debian.org/debian stretch-backports main contrib non-free' | sudo tee -a /etc/apt/sources.list.d/debian-backports.list
sudo apt-get update

sudo apt-get install -t stretch-backports -y openjdk-11-jre
sudo apt-get install -y wiringpi
```

## Deployment  

```bash
sh build-deploy-run.sh 192.168.1.2
```