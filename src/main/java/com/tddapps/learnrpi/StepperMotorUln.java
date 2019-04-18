package com.tddapps.learnrpi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.Pin;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StepperMotorUln implements StepperMotor {
    private static final int[][] movementSequence = new int[][]{
            new int []{1, 0, 0, 1},
            new int []{1, 0, 0, 0},
            new int []{1, 1, 0, 0},
            new int []{0, 1, 0, 0},
            new int []{0, 1, 1, 0},
            new int []{0, 0, 1, 0},
            new int []{0, 0, 1, 1},
            new int []{0, 0, 0, 1}
    };

    private final Object statusCriticalSection = new Object();
    private boolean isInitialized = false;
    private boolean isDestroyed = false;

    private final String name;
    private final GpioController gpio;
    private final Pin[] pinIds;

    public StepperMotorUln(String name, GpioController gpio, Pin[] pinIds){
        this.name = name;
        this.gpio = gpio;
        this.pinIds = pinIds;
    }

    private String ToLog(String msg){
        return String.format("%s; stepper: %s;", msg, name);
    }

    @Override
    public void Init() {
        synchronized (statusCriticalSection){
            if (isInitialized){
                log.warn(ToLog("Already Initialized"));
            }

            //TODO finish this
        }
    }

    @Override
    public void Destroy() {
        synchronized (statusCriticalSection){
            if (isDestroyed){
                log.warn(ToLog("Already Destroyed"));
            }

            //TODO finish this
        }
    }

    @Override
    public boolean MoveCW() throws InvalidOperationException {
        synchronized (statusCriticalSection){
            ValidateStatusSupportsMovement();

            //TODO finish this
        }
        return true;
    }

    @Override
    public boolean MoveCCW() throws InvalidOperationException {
        synchronized (statusCriticalSection){
            ValidateStatusSupportsMovement();

            //TODO finish this
        }
        return true;
    }

    private void ValidateStatusSupportsMovement() throws InvalidOperationException {
        if (isDestroyed) {
            throw new InvalidOperationException(ToLog("Cannot Move a Destroyed Motor"));
        }

        if (!isInitialized) {
            throw new InvalidOperationException(ToLog("Cannot Move a Motor that has not been initialized"));
        }
    }
}
