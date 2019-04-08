package com.tddapps.learnrpi;

import com.pi4j.io.gpio.*;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");

        final var gpio = GpioFactory.getInstance();

        final var pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RedLED", PinState.LOW);
        pin.setShutdownOptions(true, PinState.LOW);
        pin.low();

        for (int i = 0; i < 10; i++) {
            System.out.println("Toggling pin");
            pin.toggle();
            Thread.sleep(1000);
        }

        gpio.shutdown();
    }
}
