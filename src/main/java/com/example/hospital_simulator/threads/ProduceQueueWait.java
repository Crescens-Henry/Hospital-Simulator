package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.PatientMonitor;

import java.util.Observable;

public class ProduceQueueWait  extends Observable implements Runnable{

    private final PatientMonitor patientMonitor;
    public ProduceQueueWait(PatientMonitor patientMonitor){
        this.patientMonitor = patientMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.patientMonitor.generatePatientsWait();
            setChanged();
            notifyObservers("1");
        }
    }
}
