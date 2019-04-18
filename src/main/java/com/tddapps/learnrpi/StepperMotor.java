package com.tddapps.learnrpi;

public interface StepperMotor {
    void Init() throws InvalidOperationException;
    void Destroy();

    boolean MoveCW() throws InvalidOperationException;
    boolean MoveCCW() throws InvalidOperationException;
}
