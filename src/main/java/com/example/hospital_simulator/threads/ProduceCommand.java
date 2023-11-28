package com.example.hospital_simulator.threads;

import com.example.hospital_simulator.models.DoctorMonitor;

import java.util.concurrent.ThreadLocalRandom;

public class ProduceCommand implements Runnable{
    private DoctorMonitor doctorMonitor;

    public ProduceCommand(DoctorMonitor doctorMonitor) {
        this.doctorMonitor = doctorMonitor;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            this.doctorMonitor.generatedCommand();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
