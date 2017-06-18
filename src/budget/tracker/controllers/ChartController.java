/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget.tracker.controllers;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author OLYJOSH
 */
public class ChartController implements Initializable {

    @FXML
    LineChart lineChart;
    @FXML
    private AnchorPane parentAnchor;
    TableView t;
    private double initX;
    private double initY;
    private XYChart.Series<String, Number> series1, series2;//= new XYChart.Series<String,Number>();
    public double amtExp;
    public double amtInc;
    private Region veil;
    private PieChart.Data pieExp, pieInc;
    private HBox h;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //dc=new DataClass();
        t = HomeController.getInstance().table;
        ObservableList<Record> ol = t.getItems();
        series1 = new XYChart.Series<String, Number>();
        series1.setName("Income");
        series2 = new XYChart.Series<String, Number>();
        series2.setName("Expenditure");

        for (int i = 0; i < ol.size(); i++) {
            Record record = ol.get(i);
            try {
                String date = record.getDate();//.substring(0,9);
                double amount = Math.abs(record.getAmount());
                if (record.getTypee().equals("INCOME")) {
                    amtInc += amount;
                    series1.getData().add(new XYChart.Data<String, Number>(date, amount));
                } else {
                    amtExp += amount;
                    series2.getData().add(new XYChart.Data<String, Number>(date, amount));
                }
            } catch (ParseException ex) {
                Logger.getLogger(ChartController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);

    }

    @FXML
    public void createPieChatStage() {
        veil = Utility.veilScreen(parentAnchor);
        final Stage stage = new Stage(StageStyle.TRANSPARENT);
        Group rootGroup = new Group();
        Scene scene = new Scene(rootGroup, parentAnchor.getWidth(), parentAnchor.getHeight(), Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();

        stage.show();
        String drilldownCss = ChartController.class.getResource("/budget/tracker/resource/css/css.css").toExternalForm();
        // CREATION OF THE DRAGGER (CIRCLE)
        PieChart pie = new PieChart(
                FXCollections.observableArrayList(
                        pieExp = new PieChart.Data("Expenditure", amtExp),
                        pieInc = new PieChart.Data("Income", amtInc)
                )
        );

        ((Parent) pie).getStylesheets().add(drilldownCss);
        pie.setCenterShape(true);
        setDrilldownData(pie, pieInc, series1, "Inc");
        setDrilldownData(pie, pieExp, series2, "Exp");
        //when mouse button is pressed, save the initial position of screen
        rootGroup.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                initX = me.getScreenX() - stage.getX();
                initY = me.getScreenY() - stage.getY();
            }
        });

        //when screen is dragged, translate it accordingly
        rootGroup.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX() - initX);
                stage.setY(me.getScreenY() - initY);
            }
        });

        Label l = new Label("close");
        l.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                closePieChat(stage);
            }
        });
        l.setGraphic(new ImageView("/budget/tracker/resource/images/closebox.png"));
        h = new HBox(l);
        h.setAlignment(Pos.CENTER);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(h, pie);
        rootGroup.getStylesheets().add("/budget/tracker/resource/css/css.css");

//                final Label caption = new Label("");
//        caption.setTextFill(Color.DARKORANGE);
//        caption.setStyle("-fx-font: 24 arial;");
//        for (final PieChart.Data da : pie.getData()) {
//        da.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
//        new EventHandler<MouseEvent>() {
//            @Override public void handle(MouseEvent e) {
//                caption.setTranslateX(e.getSceneX());
//                caption.setTranslateY(e.getSceneY());
//                caption.setText(String.valueOf(da.getPieValue()) + "%");
//                 vBox.getChildren().add(caption);
//             }
//        });
//}
        //                for (Node node : pie.lookupAll("Text.chart-pie-label")){
//     if (node instanceof Text){
//     for (PieChart.Data pieInc :pie.getData()){
//     if (pieInc.getName().equals(((Text) node).getText()))
//     ((Text) node).setText(String.format("%,.0f", pieInc.getPieValue()));
//     }
//     }
//}
        rootGroup.getChildren().addAll(vBox);
        rootGroup.setFocusTraversable(true);
        Tooltip.install(rootGroup, new Tooltip("press ESC key to close"));
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent arg0) {
                if (arg0.getCode() == KeyCode.ESCAPE) {
                    closePieChat(stage);
                }
            }
        });
    }

    private void setDrilldownData(final PieChart pie, PieChart.Data data, final XYChart.Series<String, Number> series, final String labelPrefix) {
        data.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                ObservableList<PieChart.Data> drilledData = FXCollections.observableArrayList();
                ObservableList<XYChart.Data<String, Number>> data1 = series.getData();
                for (int i = 0; i < series.getData().size(); i++) {
                    PieChart.Data pd = new PieChart.Data(labelPrefix + "-" + data1.get(i).getXValue(), (double) data1.get(i).getYValue());
                    drilledData.add(pd);
                    //applyMouseEvents(pd);
                }
                pie.setLabelsVisible(false);
                //pie.setLegendSide(Side.LEFT);
                pie.setData(drilledData);
                h.getChildren().add(0, addBackButton((Stage) pie.getScene().getWindow()));

            }
        });

        applyMouseEvents(data);
    }

    private PieChart.Data selectedData;
    private Tooltip tooltip;

    private void applyMouseEvents(final PieChart.Data data) {
        tooltip = new Tooltip("");
        final Node node = data.getNode();

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                node.setEffect(new Glow());
                String styleString = "-fx-border-color: white; -fx-border-width: 3; -fx-border-style: dashed;";
                node.setStyle(styleString);
                tooltip.setText(String.valueOf(data.getName() + "\n" + (int) data.getPieValue()));
            }
        });

        node.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                node.setEffect(null);
                node.setStyle("");
            }
        });

        node.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedData = data;
            }
        });
    }

    private void closePieChat(Stage stageToreturnTo) {
        stageToreturnTo.close();
        parentAnchor.getChildren().removeAll(veil, Utility.p);
    }

    ;
    
    private Button addBackButton(Stage s) {
        Button bt = new Button();
        bt.setGraphic(new ImageView("/budget/tracker/resource/images/prev.png"));
        bt.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                closePieChat(s);
                createPieChatStage();
            }
        });
        return bt;
    }

}
