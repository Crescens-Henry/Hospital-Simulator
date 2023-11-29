package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.PatientState;
import com.example.hospital_simulator.models.enums.StretcherState;

import java.util.Arrays;

public class Hospital {
    private Stretcher[]stretchers;

    public Hospital() {
        this.stretchers = new Stretcher[20];
        for (int i = 0; i < this.stretchers.length; i++) {
            this.stretchers[i] = new Stretcher(null);
        }
    }

    public boolean isFull(){
        System.out.println(Arrays.toString(this.stretchers));
        for (Stretcher stretcher : this.stretchers) {
            if (StretcherState.EMPTY.equals(stretcher.getState()))
                return false;
        }
        return true;
    }

    public void setData(Patient patient){
        for (int i = 0; i < this.stretchers.length; i++) {
            if(StretcherState.EMPTY.equals(this.stretchers[i].getState())) {
                patient.setTableId(i);
                this.stretchers[i].setPatient(patient);
                this.stretchers[i].setState(StretcherState.BUSY);
                return;
            }
        }
    }

    public Patient getDinnerByState(PatientState state){
        for (int i = 0; i < this.stretchers.length; i++) {
            if(StretcherState.BUSY.equals(this.stretchers[i].getState())) {
                if(this.stretchers[i].getPatient().getState().equals(state)){
                    System.out.println("encontre1: "+this.stretchers[i].toString());
                    return  this.stretchers[i].getPatient();
                }
            }
        }
        return null;
    }

    public void removeDinnerByTableId(int id){
        this.stretchers[id].setPatient(null);
        this.stretchers[id].setState(StretcherState.EMPTY);
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "stretchers=" + Arrays.toString(stretchers) +
                '}';
    }

}
