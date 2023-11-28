package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.TableState;

public class Table {
    private Patient patient;
    private TableState state;

    public Table(Patient patient) {
        this.patient = patient;
        this.state=TableState.EMPTY;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public TableState getState() {
        return state;
    }

    public void setState(TableState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Table{" +
                "patient=" + patient +
                ", state=" + state +
                '}';
    }
}
