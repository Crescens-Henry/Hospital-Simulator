package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.PatientState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class PatientMonitor {
    private Deque<Patient> queue_wait;
    private Hospital hospital;
    private Patient enter;
    private int total;
    private int id;

    @Override
    public String toString() {
        return "PatientMonitor{" +
                "queue_wait=" + queue_wait +
                ", total=" + total +
                '}';
    }

    public PatientMonitor(int total, Hospital hospital){
        this.queue_wait= new LinkedList<Patient>();
        this.hospital = hospital;
        this.enter=null;
        this.total=total;
        this.id=20;
    }

    public synchronized void generatePatientsWait(){
        while (total == queue_wait.size()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient = new Patient(id);
        this.id++;
        queue_wait.add(patient);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void extractPatientsWait(){
        while (queue_wait.size() == 0 || hospital.isFull()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.enter=this.queue_wait.getFirst();
        this.enter.setState(PatientState.SIT_WITHOUT_ORDER);
        this.hospital.setData(this.getEnter());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.queue_wait.removeFirst();
        this.notifyAll();
    }

    public Patient getEnter() { return this.enter; }

    public Deque<Patient> getQueue_wait() {return queue_wait;}

}
