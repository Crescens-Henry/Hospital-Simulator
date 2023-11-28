package com.example.hospital_simulator.models;

import com.example.hospital_simulator.models.enums.PatientState;
import com.example.hospital_simulator.models.enums.TableState;

import java.util.Arrays;

public class Hospital {
    private Table[]tables;

    public Hospital() {
        this.tables = new Table[20];
        for (int i = 0; i < this.tables.length; i++) {
            this.tables[i] = new Table(null);
        }
    }

    public boolean isFull(){
        System.out.println(Arrays.toString(this.tables));
        for (Table table : this.tables) {
            if (TableState.EMPTY.equals(table.getState()))
                return false;
        }
        return true;
    }

    public void setData(Patient patient){
        for (int i = 0; i < this.tables.length; i++) {
            if(TableState.EMPTY.equals(this.tables[i].getState())) {
                patient.setTableId(i);
                this.tables[i].setPatient(patient);
                this.tables[i].setState(TableState.BUSY);
                return;
            }
        }
    }

    public Patient getDinnerByState(PatientState state){
        for (int i = 0; i < this.tables.length; i++) {
            if(TableState.BUSY.equals(this.tables[i].getState())) {
                if(this.tables[i].getPatient().getState().equals(state)){
                    System.out.println("encontre1: "+this.tables[i].toString());
                    return  this.tables[i].getPatient();
                }
            }
        }
        return null;
    }

    public void removeDinnerByTableId(int id){
        this.tables[id].setPatient(null);
        this.tables[id].setState(TableState.EMPTY);
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "tables=" + Arrays.toString(tables) +
                '}';
    }

}
