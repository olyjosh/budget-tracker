/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package budget.tracker.controllers;

import budget.tracker.database.DataClass;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author OLYJOSH
 */
public class ConfirmController implements Initializable {
    @FXML AnchorPane parentPane;
    @FXML HBox hbox;
    @FXML Button closeButton;
    @FXML PasswordField password;
    @FXML Label errLab,close;
    HomeController ins;
    private static ConfirmController instance;
    private double initX;
    private double initY;
    private boolean mState = false;
    Stage stage;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        instance=this;
        ins = HomeController.getInstance();
        strokeTransitionEffect(parentPane);

//        String value = ins.dp.getValue().toString();
//        System.out.println("value"+value);
        close.setGraphic(new ImageView(new Image("/budget/tracker/resource/images/close.png")));
        close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                closeWindow(null);
            }
        });
        hbox.setCursor(Cursor.HAND);
        parentPane.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent arg0) {
                if(arg0.getCode()==KeyCode.ESCAPE){
                    closeWindow(null);
                }
            }
        });
        
        hbox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(stage==null){
                    stage=(Stage) hbox.getScene().getWindow();
                }
                initX = event.getScreenX() - stage.getX();
                initY = event.getScreenY() - stage.getY();
            }
        });
        
        
            
        hbox.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mState) {
                    mState = !mState;
                } else {
                    stage.setX(event.getScreenX() - initX);
                    stage.setY(event.getScreenY() - initY);
                }
            }
        });
    }
    
    @FXML
    private void closeWindow(ActionEvent e){
        stage = (Stage) parentPane.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void okHandler(ActionEvent e){
        if(password.getText()!=null){
            DataClass dc =new DataClass();
            if(dc.confirmPass(HomeController.getInstance().lv.getSelectionModel().getSelectedItem().toString(), password.getText())){
                HomeController.getInstance().deleteEntryCall();
                closeWindow(e);
                System.out.println(false);
            }else errLab.setText("invalid password");
        }else errLab.setText("Please enter a your password");
    }
    

    
    private void strokeTransitionEffect(AnchorPane root){
        Rectangle rect = new Rectangle(0, 0, root.getPrefWidth()+2, root.getPrefHeight()+2);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setFill(null);
        rect.setStroke(Color.DODGERBLUE);
        rect.setStrokeWidth(10);
        root.getChildren().add(rect);
         
        StrokeTransition strokeTransition = StrokeTransitionBuilder.create()
            .duration(Duration.seconds(3))
            .shape(rect)
            .fromValue(Color.RED)
            .toValue(Color.DODGERBLUE)
            .cycleCount(Timeline.INDEFINITE)
            .autoReverse(true)
            .build();
        strokeTransition.play();
    }
    
    public static ConfirmController getInstance() {
       return instance;
    }
}
