package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.ExitMonitor;

import java.util.Observable;

public class ConsumeExitQueue extends Observable implements Runnable{
    private ExitMonitor exitMonitor;

    public ConsumeExitQueue(ExitMonitor exitMonitor) {
        this.exitMonitor = exitMonitor;
    }

    @Override
    public void run() {
        while (true){
            exitMonitor.extractPatientsExit();
            setChanged();
            notifyObservers("4");
        }

    }
}
