package com.tddapps.learnrpi;

import com.pi4j.io.gpio.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Executors;

@Log4j2
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
            var config = new MotorMoveConfig();
            config.setName("X");
            config.setPinIds(new Pin[]{
                    RaspiPin.GPIO_00,
                    RaspiPin.GPIO_01,
                    RaspiPin.GPIO_02,
                    RaspiPin.GPIO_03
            });
            config.setClockwise(true);
            config.setRevertMovementAfterSteps(-1);
            MoveSingleStepper(gpio, config);
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            // These are BCM GPIO pins 23, 24, 25 and 04. Numbers are different because of wiringpi
            var config = new MotorMoveConfig();
            config.setName("Y");
            config.setPinIds(new Pin[]{
                    RaspiPin.GPIO_04,
                    RaspiPin.GPIO_05,
                    RaspiPin.GPIO_06,
                    RaspiPin.GPIO_07
            });
            config.setClockwise(false);
            config.setRevertMovementAfterSteps(7500);
            MoveSingleStepper(gpio, config);
        });
    }

    @Data
    private static class MotorMoveConfig {
        private String name;
        private Pin[] pinIds;
        private boolean clockwise;
        private int revertMovementAfterSteps;

        public void ReverseDirection() {
            clockwise = !clockwise;
        }
    }

    private static void MoveSingleStepper(GpioController gpio, MotorMoveConfig config) {
        var motor = new StepperMotorUln(config.getName(), gpio, config.getPinIds());
        motor.Init();

        var stepNumber = 0L;

        try {
            while (true) {
                if (config.isClockwise()) {
                    motor.MoveCW();
                } else {
                    motor.MoveCCW();
                }

                stepNumber++;
                if (config.getRevertMovementAfterSteps() > 0 && (stepNumber % config.getRevertMovementAfterSteps() == 0)) {
                    config.ReverseDirection();
                }
            }
        } catch (InvalidOperationException e) {
            log.error("Invalid Motor Operation", e);
        } finally {
            motor.Destroy();
        }
    }
}
