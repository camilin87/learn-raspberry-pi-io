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
        var gpio = GpioFactory.getInstance();

        Executors.newSingleThreadExecutor().execute(() -> {
            // These are BCM GPIO pins 17, 18, 27 and 22. Numbers are different because of wiringpi
            try {
                MoveSingleStepper(gpio, "X", new Pin[]{
                        RaspiPin.GPIO_00,
                        RaspiPin.GPIO_01,
                        RaspiPin.GPIO_02,
                        RaspiPin.GPIO_03
                }, true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            // These are BCM GPIO pins 23, 24, 25 and 04. Numbers are different because of wiringpi
            try {
                MoveSingleStepper(gpio, "Y", new Pin[]{
                        RaspiPin.GPIO_04,
                        RaspiPin.GPIO_05,
                        RaspiPin.GPIO_06,
                        RaspiPin.GPIO_07
                }, false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void MoveSingleStepper(GpioController gpio, String name, Pin[] pinsIds, boolean clockwise) throws InterruptedException {
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

        var stepDirection = clockwise ? 1 : -1;

        var pinCount = stepPins.length;
        var stepCount = movementSequence.length;
        var stepIndex = 0;

        while(true){
            var currentStep = movementSequence[stepIndex];

            System.out.println(String.format("[%s] Step; index: %d; pins: %s;",
                    name, stepIndex, Arrays.toString(currentStep)));

            for (int i = 0; i < pinCount; i++) {
                if (currentStep[i] == 0){
                    stepPins[i].low();
                }
                else {
                    System.out.println(String.format("[%s] Enable %s", name, stepPins[i].getName()));
                    stepPins[i].high();
                }
            }

            stepIndex += stepDirection;
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
