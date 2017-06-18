package budget.tracker.controllers;

import budget.tracker.database.DataClass;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author OLYJOSH
 */
public class NewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField name, desc, starting;
    @FXML
    private PasswordField pass, confPass;
//    @FXML private ComboBox recExp,recInc;
//    @FXML private ListView recIncList,recExpList;
    @FXML
    private ProgressBar prog;
    @FXML
    private Label outLabel;
    @FXML 
    private ChoiceBox curr;
    private DataClass dc;
    private static NewController instance;

    public static NewController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        instance = this;
        //strokeTransitionEffect(anchorPane);
        //System.out.println("The date from instance "+HomeController.getInstance().getDp().getValue() );

    }
//
//    public HomeController showCustomerDialog() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/budget/tracker/resource/Home.fxml"));
//        loader.load();
//        HomeController hc = loader.<HomeController>getController();
//        return hc;
//    }

    @FXML
    private void createNew(ActionEvent event) throws IOException {
        System.out.println(name.getText());
        dc = new DataClass();
        String nameText = name.getText();
        String descText = desc.getText();
        String[] ar= new String[]{"ha NG","hu Hu","en US","en GB","fr FR"};
        dc.createNewRec(nameText, pass.getText(), descText, Double.parseDouble(starting.getText()),ar[curr.getSelectionModel().getSelectedIndex()]);
        HomeController.getInstance().updateList();
        HomeController.getInstance().lv.getSelectionModel().selectLast();
        ((Stage) anchorPane.getScene().getWindow()).close();
    }

    @FXML
    private void dataChange() throws IOException {
        prog.setVisible(true);
        Utility.passwordValidator(pass.getText(), confPass.getText(), outLabel, prog);
    }

    private void strokeTransitionEffect(AnchorPane root) {
        Rectangle rect = new Rectangle(0, 0, root.getPrefWidth() + 2, root.getPrefHeight() + 2);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setFill(null);
        rect.setStroke(Color.GREEN);
        rect.setStrokeWidth(10);
        root.getChildren().add(rect);

        StrokeTransition strokeTransition = StrokeTransitionBuilder.create()
                .duration(Duration.seconds(3))
                .shape(rect)
                .fromValue(Color.GREEN)
                .toValue(Color.DODGERBLUE)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        strokeTransition.play();
    }

}
