package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.StretcherState;

public class Stretcher {
    private Patient patient;
    private StretcherState state;

    public Strecher(Patient patient) {
        this.patient = patient;
        this.state= StretcherState.EMPTY;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public StretcherState getState() {
        return state;
    }

    public void setState(StretcherState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Stretcher{" +
                "patient=" + patient +
                ", state=" + state +
                '}';
    }
}