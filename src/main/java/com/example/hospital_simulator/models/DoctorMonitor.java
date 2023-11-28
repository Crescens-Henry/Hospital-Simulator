package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.PatientState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class DoctorMonitor {
    private Queue<Patient> commands;

    private Deque<Patient> orders;

    private Hospital hospital;

    private int TOTAL;

    public DoctorMonitor(Hospital hospital){
        this.commands=new LinkedList<Patient>();
        this.orders=new LinkedList<Patient>();
        this.hospital = hospital;
        this.TOTAL=20;

    }
    public synchronized void makeTheOrder() {
        while (commands.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient =commands.remove();
        patient.setState(PatientState.UTIL);
        patient.setTime(getRandomUtilTime());
        orders.add(patient);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();

    }

    public int getRandomUtilTime() {
        return  (int)(Math.random() * (10 - 5)) + 5;
    }

    public synchronized void generatedCommand(){
        while (commands.size()==TOTAL) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patientOrder = hospital.getDinnerByState(PatientState.SIT_WITHOUT_ORDER);
        if(patientOrder !=null){
            patientOrder.setState(PatientState.WAIT_ORDER);
            commands.add(patientOrder);
        }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Deque<Patient> getOrders() {
        return orders;
    }
}
