package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.DoctorMonitor;

import java.util.Observable;

public class ConsumeCommands  extends Observable implements Runnable{
    private DoctorMonitor doctorMonitor;

    public ConsumeCommands(DoctorMonitor doctorMonitor) {
        this.doctorMonitor = doctorMonitor;
    }

    @Override
    public void run() {
        while(true){
            this.doctorMonitor.makeTheOrder();
            setChanged();
            notifyObservers("3");
        }
    }
}
