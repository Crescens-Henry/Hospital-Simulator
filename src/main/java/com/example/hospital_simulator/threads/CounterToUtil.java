package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.Patient;
import com.example.hospital_simulator.models.ExitMonitor;
import com.example.hospital_simulator.models.enums.PatientState;

public class CounterToUtil implements Runnable {
    private ExitMonitor exitMonitor;
    private Patient patient;

    public CounterToUtil(ExitMonitor exitMonitor, Patient patient) {
        this.exitMonitor = exitMonitor;
        this.patient = patient;
    }

    @Override
    public void run() {
        while (patient.getTime()>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            patient.decrementTime();
        }
        patient.setState(PatientState.UTIL_FINISH);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exitMonitor.passToExitQueue();
    }
}
