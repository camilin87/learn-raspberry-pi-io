package com.tddapps.learnrpi;

import com.pi4j.io.gpio.*;

import java.util.Arrays;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting");

//        BlinkLed();

        MoveStepper();
    }

    private static void BlinkLed() throws InterruptedException {
        final var gpio = GpioFactory.getInstance();

        // This is BCM GPIO pin 18. Numbers are different because of wiringpi
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

    private static void MoveStepper() throws InterruptedException {
        Executors.newSingleThreadExecutor().execute(() -> {
            // These are BCM GPIO pins 17, 22, 23 and 24. Numbers are different because of wiringpi
            try {
                MoveSingleStepper(new Pin[]{
                        RaspiPin.GPIO_00,
                        RaspiPin.GPIO_03,
                        RaspiPin.GPIO_04,
                        RaspiPin.GPIO_05
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void MoveSingleStepper(Pin[] pinsIds) throws InterruptedException {
        final var gpio = GpioFactory.getInstance();

        var stepPins = Arrays.stream(pinsIds)
                .map(p -> {
                    var pin = gpio.provisionDigitalOutputPin(p, p.toString(), PinState.LOW);
                    pin.setShutdownOptions(true, PinState.LOW);
                    pin.low();
                    return pin;
                })
                .toArray(GpioPinDigitalOutput[]::new);

        final var movementSequence = new int[][]{
                new int []{1, 0, 0, 1},
                new int []{1, 0, 0, 0},
                new int []{1, 1, 0, 0},
                new int []{0, 1, 0, 0},
                new int []{0, 1, 1, 0},
                new int []{0, 0, 1, 0},
                new int []{0, 0, 1, 1},
                new int []{0, 0, 0, 1}
        };

        // use 1 for CW, -1 for CCW
        var stepDirection = 1;

        var pinCount = stepPins.length;
        var stepCount = movementSequence.length;
        var stepIndex = 0;

        while(true){
            var currentStep = movementSequence[stepIndex];

            System.out.println(String.format("Step; index: %d; pins: %s;", stepIndex, Arrays.toString(currentStep)));

            for (int i = 0; i < pinCount; i++) {
                if (currentStep[i] == 0){
                    stepPins[i].low();
                }
                else {
                    System.out.println(String.format("Enable %s", stepPins[i].getName()));
                    stepPins[i].high();
                }
            }

            stepIndex++;
            if (stepIndex >= stepCount){
                stepIndex = 0;
            }
            if (stepIndex < 0){
                stepIndex = stepCount - 1;
            }

            Thread.sleep(1);
        }
    }
}
