package com.example.hospital_simulator.models;
import com.example.hospital_simulator.models.enums.PatientState;
import javafx.scene.paint.Color;

public class Patient {
    private PatientState state;
    private int id;
    private  int stretcherId;
    private Color color;
    private int time;

    public Patient(int id){
        this.id=id;
        this.time=0;
        this.color=GenerateColorRandom();
        this.state= PatientState.WAIT;
        this.stretcherId=-1;
    }

    private Color GenerateColorRandom(){
        int rangR = (int)(Math.random() * 255);
        int rangG = (int)(Math.random() * 100);
        int rangB = 0;
        return Color.rgb(rangR, rangG, rangB);
    }

    public Color getColor() {
        return color;
    }

    public int getTime() {
        return time;
    }

    public PatientState getState() {
        return state;
    }

    public void setState(PatientState state) {
        this.state = state;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStretcherId() {
        return stretcherId;
    }

    public void setStretcherId(int stretcherId) {
        this.stretcherId = stretcherId;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "state=" + state +
                ", id=" + id +
                ", stretcherId=" + stretcherId +
                ", color=" + color +
                ", time=" + time +
                '}';
    }

    public void decrementTime(){
        this.time--;
    }
}