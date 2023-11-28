package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.PatientMonitor;

import java.util.Observable;

public class ConsumeQueueWait  extends Observable implements Runnable{
    private PatientMonitor patientMonitor;

    public ConsumeQueueWait(PatientMonitor patientMonitor){
        this.patientMonitor = patientMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.patientMonitor.extractPatientsWait();
            setChanged();
            notifyObservers("2");
        }
    }
}
