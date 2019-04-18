package com.tddapps.learnrpi;

public interface StepperMotor {
    void Init();
    void Destroy();

    boolean MoveCW();
    boolean MoveCCW();
}
