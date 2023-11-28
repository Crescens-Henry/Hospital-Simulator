package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.PatientState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class ExitMonitor {
    private Deque<Patient> exitQueue;
    private Hospital hospital;
    private Patient exit;

    public ExitMonitor( Hospital hospital) {
        this.exitQueue = new LinkedList<Patient>();
        this.hospital = hospital;
    }
    public synchronized void extractPatientsExit(){
        while (exitQueue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.exit=this.exitQueue.getFirst();
        this.exit.setState(PatientState.EXIT);
        hospital.removeDinnerByTableId(exit.getTableId());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void passToExitQueue(){
        while (exitQueue.size()==20) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
            Patient patientOrder = hospital.getDinnerByState(PatientState.UTIL_FINISH);
            if(patientOrder !=null){
                patientOrder.setState(PatientState.WAIT_ORDER);
                this.exitQueue.add(patientOrder);
            }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Patient removeFromExitQueue(){
        return this.exitQueue.remove();
    }
}