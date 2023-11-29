package com.example.hospital_simulator.controller;

import com.example.hospital_simulator.models.*;
import com.example.hospital_simulator.threads.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Observable;
import java.util.Observer;


public class MainController implements Observer{
    private PatientMonitor patientMonitor;

    private ExitMonitor exitMonitor;

    private Hospital hospital;

    private DoctorMonitor doctorMonitor;

    private final Color EmptySpaceColor=Color.web("#232424");


    private final Color NurseColor=Color.web("#FFA500");

    private final Color DoctorColor=Color.web("#00FF00");

    @FXML
    private Button btn_start;

    @FXML
    private Rectangle enter_nurse;


    @FXML
    private Rectangle add_command;

    @FXML
    private Rectangle cooking_command;

    @FXML
    private Rectangle deliver_command;

    @FXML
    private Rectangle enter_patient;

    @FXML
    private Rectangle exit;

    @FXML
    private Rectangle exit_door;

    @FXML
    private HBox queue_wait;

    @FXML
    private Rectangle receive_command;

    @FXML
    private GridPane stretchers;

    @FXML
    private Rectangle wait_command;
    @FXML
    public void initialize() {
        this.hospital =new Hospital();
        this.patientMonitor =new PatientMonitor(10, this.hospital);
        this.doctorMonitor =new DoctorMonitor(this.hospital);
        this.exitMonitor=new ExitMonitor(this.hospital);
    }

    @FXML
    void onClickedStart(MouseEvent event) {
        btn_start.setDisable(true);
        initSimulation();
    }

    private void initSimulation(){
        ConsumeQueueWait consumeQueueWait= new ConsumeQueueWait(this.patientMonitor);
        ProduceQueueWait produceQueueWait = new ProduceQueueWait(this.patientMonitor);
        ConsumeCommands consumeCommands= new ConsumeCommands(this.doctorMonitor);
        ConsumeExitQueue ConsumeExitQueue = new ConsumeExitQueue(this.exitMonitor);

        consumeQueueWait.addObserver(this);
        produceQueueWait.addObserver(this);
        consumeCommands.addObserver(this);
        ConsumeExitQueue.addObserver(this);

        Thread consumeCommandsThread= new Thread(consumeCommands);
        consumeCommandsThread.setDaemon(true);
        Thread produceQueueWaitThread = new Thread(produceQueueWait);
        produceQueueWaitThread.setDaemon(true);
        Thread consumeQueueWaitThread = new Thread(consumeQueueWait);
        consumeQueueWaitThread.setDaemon(true);
        Thread ConsumeExitQueueThread= new Thread(ConsumeExitQueue);
        ConsumeExitQueueThread.setDaemon(true);

        consumeCommandsThread.start();
        ConsumeExitQueueThread.start();
        produceQueueWaitThread.start();
        consumeQueueWaitThread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        switch (Integer.valueOf(String.valueOf(o))) {
            case 1:
                addPatientToQueueWait();
                break;
            case 2:
                enterPatientToEntrace();
                waitSecond(2);
                sitPatientAtSomeTable();
                makeOrder();
                break;
            case 3:
                utilTimer();
                break;
            case 4:
                leavePatient();
                break;
        }
    }

    private void leavePatient(){
        Patient patient =exitMonitor.removeFromExitQueue();
        StackPane stackPane= getTable(patient.getTableId());
        Rectangle rectangle=(Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            text.setText("-");
            text.setFill(Color.WHITE);
            rectangle.setFill(EmptySpaceColor);
            exit_door.setFill(patient.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit_door.setFill(EmptySpaceColor);
            exit.setFill(patient.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit.setFill(EmptySpaceColor);
        });

    }

    private void utilTimer(){
        deliverCommand();
        waitSecond(1);
        Patient patient = doctorMonitor.getOrders().remove();
        StackPane stackPane=getTable(patient.getTableId());
        Text text = (Text) stackPane.getChildren().get(1);
        Timeline timeline = new Timeline();
        EventHandler<ActionEvent> eventHandler = event -> {
            Platform.runLater(()->{
                text.setText(String.valueOf(patient.getTime()));
            });
            if (patient.getTime() == 0) {
                Platform.runLater(()->{
                    text.setText("-F");
                });
                timeline.stop();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), eventHandler);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        countdown(patient);
        waitSecond(1);
        CheckCommands();
    }

    private void CheckCommands(){
        Platform.runLater(()->{
            wait_command.setFill(DoctorColor);
            cooking_command.setFill(DoctorColor);
            deliver_command.setFill(EmptySpaceColor);
        });
    }

    private void deliverCommand(){
        Platform.runLater(()->{
            deliver_command.setFill(DoctorColor);
            cooking_command.setFill(EmptySpaceColor);
            wait_command.setFill(EmptySpaceColor);
        });
    }

    private void countdown(Patient patient){
        CounterToUtil counterUtilPatient=new CounterToUtil(this.exitMonitor, patient);
        Thread CounterToUtilThread=new Thread(counterUtilPatient);
        CounterToUtilThread.setDaemon(true);
        CounterToUtilThread.start();
    }
    private void makeOrder(){
        ProduceCommand produceCommand=new ProduceCommand(this.doctorMonitor);
        Thread produceCommandThread=new Thread(produceCommand);
        produceCommandThread.setDaemon(true);
        produceCommandThread.start();
        workDoctor();
    }

    private void workDoctor(){
        Platform.runLater(()->{
            wait_command.setFill(EmptySpaceColor);
            deliver_command.setFill(EmptySpaceColor);
            cooking_command.setFill(DoctorColor);
        });
    }

    private void enterPatientToEntrace(){
        Rectangle popPatient= (Rectangle) queue_wait.getChildren().get(0);
        Platform.runLater(()->{
            enter_patient.setFill(popPatient.getFill());
            queue_wait.getChildren().remove(popPatient);
        });
    }
    private void sitPatientAtSomeTable(){
        for(Node hboxNode:stretchers.getChildren()){
            HBox hbox=(HBox) hboxNode;
            Rectangle nurse = (Rectangle) hbox.getChildren().get(0);
            StackPane stackPane= (StackPane) hbox.getChildren().get(1);
            Rectangle patient = (Rectangle) stackPane.getChildren().get(0);
            if(EmptySpaceColor.equals(patient.getFill())){
                sitPatient(nurse, stackPane);
                waitSecond(1);
                waitresReturnEntrace(nurse);
                break;
            }
        }
    }

    private void sitPatient( Rectangle nurse,StackPane stackPane){
        Rectangle patient = (Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            enter_nurse.setFill(EmptySpaceColor);
            nurse.setFill(NurseColor);
            patient.setFill(enter_patient.getFill());
            text.setFill(Color.BLACK);
            text.setText("-W");
            enter_patient.setFill(EmptySpaceColor);
        });
    }

    private void waitresReturnEntrace(Rectangle nurse){
        Platform.runLater(()->{
            enter_nurse.setFill(NurseColor);
            nurse.setFill(EmptySpaceColor);
        });
    }
    private void addPatientToQueueWait(){
        Patient newPatient =this.patientMonitor.getQueue_wait().getLast();
        Rectangle square = new Rectangle(50, 50, newPatient.getColor());
        square.setArcHeight(30);
        square.setArcWidth(30);
        Platform.runLater(()->{queue_wait.getChildren().add(square);});
    }

    private void waitSecond(int second){
        int milliseconds= second*1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private StackPane getTable(int id){
        Node hboxNode=stretchers.getChildren().get(id);
        HBox hbox=(HBox) hboxNode;
        return  (StackPane) hbox.getChildren().get(1);
    }
}
