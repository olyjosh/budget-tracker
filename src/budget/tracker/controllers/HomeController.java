package budget.tracker.controllers;

import budget.tracker.database.DataClass;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 *
 * @author OLYJOSH
 */
public class HomeController implements Initializable {

    @FXML
    public TableView table;
    @FXML
    public ListView lv;
    @FXML
    private Button fetch, addEntButton, prev, next, loginButton;
    @FXML
    public TableColumn refId, amountCol, balanceCol;
    @FXML
    private ComboBox cb;
    @FXML
    private VBox dateHolder;
    @FXML
    private HBox userBox, entryHolder;
    @FXML
    private ImageView newButtonIcon, nextIcon, prevIcon;
    @FXML
    private ChoiceBox rows, type, user;
    @FXML
    private AnchorPane parentAnchor, tableAnchor, listAnchor, topAnchor;
    @FXML
    private TextField amt, ddd, ref;
    @FXML
    private PasswordField pass;
    @FXML
    private Label addEntOut, timeLabel, L1, L2, L3, passLabel;
    @FXML
    private Hyperlink f1, chart, balanceText;
    //@FXML private TextField desc;
    //@FXML private Rectangle rect;

    private Stage primaryStage;
    public DataClass dc;
    DatePicker dp, dp2;
    public int id;
    private FillTransition fillTransition;
    ObservableList<Record> recordEntryModel;

    private static HomeController instance;//= new HomeController();
//    private BudgetTracker application;
    private Stage stage;
    private double initX;
    private double initY;
    private boolean mState = false, maximized = false, loginState = false;
    private Rectangle2D backupWindowBounds = null;
    private NumberFormat currency;
    public String outParseValue;
    public Image outParseIcon;
    private Region veil;
    public final String WARNING_IMG_LINK = "/budget/tracker/resource/images/warning.png",
            INFO_IMG_LINK = "/budget/tracker/resource/images/point.png",
            STOP_IMG_LINK = "/budget/tracker/resource/images/Stop.png";

    public DatePicker getDp() {
        return dp;
    }

    public DatePicker getDp2() {
        return dp2;
    }

    public String getOutParseValue() {
        return outParseValue;
    }

    public void setOutParseValue(String s) {
        outParseValue = s;
    }

    public Image getOutParseIcon() {
        return outParseIcon;
    }

    public void setOutParseIcon(Image outParseIcon) {
        this.outParseIcon = outParseIcon;
    }

    //private HomeController() {};
//    public static HomeController getInstance() {
//        if (HomeController.instance == null) {
//            synchronized (HomeController.class) {
//                if (HomeController.instance == null) {
//                    HomeController.instance = new HomeController();
//                }
//            }
//        }
//        return HomeController.instance;
//    }
//    
//    
//    public void setApp(BudgetTracker application, Stage stage) {
//        this.application = application;
//        this.stage = stage;
//}    
    public static HomeController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        someDefaultSet();

    }

    void someDefaultSet() {

        //balanceText.setText("NGN 380,000");
        currency = NumberFormat.getCurrencyInstance(new Locale("ha", "NG"));

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                double doBalance = doBalance();
                if (doBalance == 0) {
                    balanceText.setText("Click to refresh");
                }
                // System.out.println("this is called every 5 seconds on UI thread");
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        digitalClock();

        L1.setGraphic(new ImageView(new Image("/budget/tracker/resource/images/min.png")));
        L2.setGraphic(new ImageView(new Image("/budget/tracker/resource/images/max.png")));
        L3.setGraphic(new ImageView(new Image("/budget/tracker/resource/images/close_1.png")));

        L1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                stage.setIconified(true);
            }
        });
        L2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                toogleMaximized();
            }
        });
        L3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Platform.exit();

//                System.exit(0);
//                //createPopup("Are you sure you want to close this application?");
//                dialog.show();
//                dialog.setX((stage.getWidth() - dialog.getWidth()) / 2 + stage.getX());
//                dialog.setY((stage.getHeight() - dialog.getHeight()) / 2 + stage.getY());
            }
        });

        topAnchor.setCursor(Cursor.HAND);
        topAnchor.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (stage == null) {
                    stage = (Stage) topAnchor.getScene().getWindow();
                }
                initX = event.getScreenX() - stage.getX();
                initY = event.getScreenY() - stage.getY();
            }
        });

        topAnchor.setOnMouseDragged(new EventHandler<MouseEvent>() {
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

        f1.setGraphic(new ImageView(new Image("/budget/tracker/resource/images/Info.png")));
        parentAnchor.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent arg0) {
                if (arg0.getCode() == KeyCode.F1) {
                    createF1CircledStage();
                }
            }
        });
        addEntOut.setVisible(false);
        type.getSelectionModel().selectFirst();
        rows.getSelectionModel().selectLast();
        numOfRows = Integer.parseInt("" + rows.getSelectionModel().getSelectedItem());
        rows.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                String a = arg2.toString();
                service.setRowsLim(a);
                numOfRows = Integer.parseInt(a);
                service.restart();
            }
        });
//        rows.addEventHandler(ActionEvent.ACTION, new EventHandler<Event>(){
//
//            @Override
//            public void handle(Event arg0) {
//                service.restart();
//            }
//        
//        });

        lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
//                try {
//                    if(!loginState){
//                        showDilogStage(null, "LogOutConfirm.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);
//                    }else {
                //Just calling logout to render default statt
                logOutUser();
                //}
//                pass.setVisible(true);
//                loginButton.setVisible(true);
//                passLabel.setVisible(true);
                //fadeEffect(userBox);
//                } catch (IOException ex) {
//                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        });

        user.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                lv.getSelectionModel().select(user.getSelectionModel().getSelectedItem());
                setUnLogInState();
            }
        });

        dc = new DataClass();
        updateList();
        doTablesDefault();
        addDatePickers();
        setUnLogInState();
        nextIcon.setImage(new Image("/budget/tracker/resource/images/next.png"));
        prevIcon.setImage(new Image("/budget/tracker/resource/images/prev.png"));
        service.start();
    }

    private void doTablesDefault() {

        table.setPlaceholder(new ImageView(new Image("/budget/tracker/resource/images/login.png")));
//        ObservableList<Record> populateTableData = dc.populateTableData("1", null, null, null);
//        table.setItems(populateTableData);
        setMoneyCells(amountCol);
        setMoneyCells(balanceCol);

    }

    public void updateList() {
        recordEntryModel = dc.recordEntryModel();
        ObservableList observableList = FXCollections.observableList(dc.recordEntryList);
        lv.setItems(observableList);
        user.setItems(observableList);

    }

    private FadeTransition fadeTransition;
    SequentialTransition animation;

    private void fadeEffect(HBox h) {
        fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.seconds(5))
                .node(h)
                .fromValue(1)
                .toValue(0.5)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        fadeTransition.play();
    }

    private void seqencetialEffect(HBox x) {
        animation = SequentialTransitionBuilder.create()
                .node(x)
                .children(
                        TranslateTransitionBuilder.create()
                        .duration(Duration.seconds(0.5))
                        .fromY(-10)
                        .toY(10)
                        .build()
                )
                .build();
        animation.play();
    }

    private void setMoneyCells(TableColumn tc) {
        tc.setCellFactory(new Callback<TableColumn<Record, Integer>, TableCell<Record, Integer>>() {
            @Override
            public TableCell<Record, Integer> call(TableColumn<Record, Integer> text) {
                return new TableCell<Record, Integer>() {
                    @Override
                    protected void updateItem(Integer text, boolean empty) {
                        super.updateItem(text, empty);
                        if (!empty) {
                            int value = text.intValue();
                            if (value > 0) {
                                setTextFill(Color.GREEN);
                            } else if (value < 0) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                            setAlignment(Pos.BASELINE_RIGHT);
                            setText(currency.format(Math.abs(value)));
                        } else {
                            setText(null);  // for debugging purposes
                        }
//                        
                    }

                };
            }

        });
    }

//    private void transi(Stage s){
//        
//        //Rectangle rect = new Rectangle(0, 0, s.getWidth(), s.getHeight());
//         
//        
//        fillTransition = FillTransitionBuilder.create()
//            .duration(Duration.seconds(3))
//            .shape(rect)
//            .fromValue(Color.RED)
//            .toValue(Color.DODGERBLUE)
//            .cycleCount(Timeline.INDEFINITE)
//            .autoReverse(true)
//            .build();
//    
//    }
//    
    public void play() {
        fillTransition.play();
    }

    public void stop() {
        fillTransition.stop();
    }

    public void logOutUser() {
        user.getSelectionModel().select(lv.getSelectionModel().getSelectedItem());
        setUnLogInState();
        seqencetialEffect(userBox);
        userBox.setVisible(true);
        loginState = false;
    }

    private void showDilogStage(String title, String fxml, Modality m, StageStyle s, boolean resizable) throws IOException {
        // Load the fxml file and create a new stage for the popup dialog.

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/budget/tracker/resource/" + fxml));
        AnchorPane page = (AnchorPane) loader.load();
        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(m);
        dialogStage.initStyle(s);
        dialogStage.setResizable(resizable);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

    }

    @FXML
    public void showNewDialog() throws IOException {
        showDilogStage("New Account", "NewTrack.fxml", Modality.NONE, StageStyle.UTILITY, false);
    }

    public void ConfirmDialog() throws IOException {
        showDilogStage(null, "Confirm.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);
    }

    @FXML
    public void ChangePassword() throws IOException {
        showDilogStage("Change Password", "ChangePass.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);
    }

    @FXML
    private void showHelp() throws IOException {
        createF1CircledStage();
        //showDilogStage(null, "f1.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT);
    }

    public void setStage(Stage temp) {
        primaryStage = temp;
    }

    int rowOffset = 0;
    int numOfRows;
    //int currentRow;
    boolean isNext = false;

    @FXML
    private void handleNext(ActionEvent e) {
        rowOffset += numOfRows;
        service.setRowOffseeet(String.valueOf(rowOffset));
        service.setRowsLim("" + numOfRows);
        service.restart();
    }

    @FXML
    private void handlePrev(ActionEvent e) {
        rowOffset -= numOfRows;
        if (rowOffset < 0) {
            rowOffset = 0;
        }
        service.setRowOffseeet(String.valueOf(rowOffset));
        service.setRowsLim("" + numOfRows);
        service.restart();

    }

    @FXML
    private void handleNewButton(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/budget/tracker/resource/NewTrack.fxml"));
    }

    @FXML
    private void handleChatButton(ActionEvent e) throws IOException {

        showDilogStage("Chart", "Chart.fxml", Modality.NONE, StageStyle.DECORATED, true);
    }

    @FXML
    private double doBalance() {
        double balance = dc.balance();
        balanceText.setText(currency.format(balance));
        System.out.println(currency.format(balance));
        return balance;
    }

    @FXML
    private void addRecordEntry(ActionEvent e) throws ParseException {
        String amount = amt.getText();
        double doubleAmount = NumberFormat.getInstance(Locale.ENGLISH).parse(amount).doubleValue();
        String describe = ddd.getText();
        int sel = type.getSelectionModel().getSelectedIndex();
        dc = new DataClass();
        boolean addEntry = dc.addEntry(id, doubleAmount, describe, sel, ref.getText());
        if (!addEntry) {
            addEntOut.setVisible(true);
            //addEntOut.setEffect(new Glow(0.5));
            service.restart();
        }

        doBalance();
    }

    @FXML
    private void handleFetchAction(ActionEvent event) {
        dc = new DataClass();
        ObservableList<Record> populateTableData = dc.populateTableData("1", null, null, null);
        table.setItems(populateTableData);
//        table.setItems(populateTableData);

    }

    public Record selectEd;
    public int seleccc;

    @FXML
    public void editEntryCall() throws IOException {
        if (table.getItems().size() > 0 && !table.getSelectionModel().isEmpty()) {
            int selectedIndex = table.getSelectionModel().getSelectedIndex();
            selectEd = (Record) table.getItems().get(selectedIndex);
            seleccc = selectedIndex;
            showDilogStage("Edit", "Edit.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);

        }

//...
//        if(!dc.deleteRecord(lv.getSelectionModel().getSelectedItem().toString())){
//            lv.getItems().remove(lv.getSelectionModel().getSelectedIndex());
//        }
    }

    public void deleteEntryCall() {

        if (!dc.deleteRecord(lv.getSelectionModel().getSelectedItem().toString())) {
            lv.getItems().remove(lv.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void deleteListEntry() throws IOException {
        if (lv.getItems().size() > 0 && !lv.getSelectionModel().isEmpty()) {
            ConfirmDialog();
//            if(ConfirmDialog()){
//                if(!dc.deleteRecord(lv.getSelectionModel().getSelectedItem().toString())){
//                    //lv.getItems().remove(lv.getSelectionModel().getSelectedIndex());
//                }
//            }
        } else {
            System.out.println("empyt content");
        }
    }

    @FXML
    private void deleteTableEntry() throws ParseException {
        if (table.getItems().size() > 0 && !table.getSelectionModel().isEmpty()) {
            int selectedIndex = table.getSelectionModel().getSelectedIndex();
            Record r = (Record) table.getItems().get(selectedIndex);
//            // Record r=(Record) table.getSelectionModel().getSelectedItems(); //NOT WORKING
//            System.out.println("amt "+r.getAmount() +"  "+r.getTypee()+" "+r.getDate() );
//            System.out.println(r);
            if (!dc.deleteTracks(r.getDate(), r.getAmount())) {
                table.getItems().remove(selectedIndex);
                doBalance();
            }

        } else {
            System.out.println("empyt content");
        }
    }

    final GetTaskService service = new GetTaskService(id, null, null, null, String.valueOf(rowOffset));

    final Callback<DatePicker, DateCell> dayCellFactory
            = new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item.isAfter(LocalDate.now())) {
                                setDisable(true);
                                setStyle("-fx-background-color: #AAc0cb;");
                            }
                            //long p = ChronoUnit.DAYS.between(dp.getValue(), item);
                            //setTooltip(new Tooltip("You're about to stay for " + p + " days"));
                        }
                    };
                }
            };

    private void addDatePickers() {
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        dp = new DatePicker();
        dp2 = new DatePicker();
        dp.setConverter(converter);
        dp2.setConverter(converter);
        dp.setPromptText("dd-mm-yyyy");
        dp2.setPromptText("dd-mm-yyyy");
        dp.setDayCellFactory(dayCellFactory);
        dp2.setDayCellFactory(dayCellFactory);
        dateHolder.getChildren().add(dp);
        dateHolder.getChildren().add(dp2);
        dp.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                LocalDate value = dp.getValue();
                LocalDate value2 = dp2.getValue();
                //
                if (value != null && value2 != null && (value.isBefore(value2) || value.isEqual(value2))) {

                    //queryWithDate(id, String.valueOf(value), String.valueOf(value2), Integer.parseInt(rows.getSelectionModel().getSelectedItem().toString()));
                    service.setId(id);
                    service.setD1(String.valueOf(value));
                    service.setD2(String.valueOf(value2));
                    String rowLim = rows.getSelectionModel().getSelectedItem().toString();
                    if (!rowLim.equalsIgnoreCase("All")) {
                        service.setRowsLim(rowLim);
                    } else {
                        service.setRowsLim(null);
                    }
                    service.restart();
                }
            }
        });
        dp2.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                LocalDate value = dp.getValue();
                LocalDate value2 = dp2.getValue();
                if (value != null && value2 != null && (value.isBefore(value2) || value.isEqual(value2))) {
                    veilScreen();
                    service.setId(id);
                    service.setD1(String.valueOf(value));
                    service.setD2(String.valueOf(value2));
                    service.setRowsLim(rows.getSelectionModel().getSelectedItem().toString());
                    service.restart();
                    //queryWithDate(id, String.valueOf(value), String.valueOf(value2), Integer.parseInt(rows.getSelectionModel().getSelectedItem().toString()));
                }
            }
        });

    }

    private ObservableList<Record> queryWithDate(int recID, String d1, String d2, int limit) {
//        if(d1!=null && d2!=null){
//            dc=new DataClass();
//            return dc.populateTableData( String.valueOf(recID), d1, d2,String.valueOf(limit));
//        }
//        return null;
        dc = new DataClass();
        ObservableList<Record> populateTableData = dc.populateTableData(String.valueOf(recID), d1, d2, String.valueOf(limit));
        return populateTableData;
    }

    /**
     * Scales the node based on the standard letter, portrait paper to be
     * printed. 084.
     *
     * @param node The scene node to be printed. 085.
     */
    @FXML
    private void callPrinting(ActionEvent e) {
        print(parentAnchor);
    }

    public boolean print(final Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        node.getTransforms().add(new Scale(scaleX, scaleY));
        boolean success = false;
        PrinterJob job = PrinterJob.createPrinterJob();
        job.showPrintDialog(primaryStage);
        if (job != null) {
            success = job.printPage(node);
            if (success) {
                success = job.endJob();
            }
        }
        return success;
    }

    /**
     * handles window resizing
     */
    public void toogleMaximized() {
        Screen screen = null;//Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1).get(0);
        if (stage == null) {
            stage = (Stage) parentAnchor.getScene().getWindow();
        }
        if (!(stage.getX() > 0) || !(stage.getY() > 0)) {
            screen = Screen.getScreensForRectangle(20, 20, 1, 1).get(0);
        } else {
            screen = Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1).get(0);
        }
        if (maximized) {
            maximized = false;
            if (backupWindowBounds != null) {
                stage.setX(backupWindowBounds.getMinX());
                stage.setY(backupWindowBounds.getMinY());
                stage.setWidth(backupWindowBounds.getWidth());
                stage.setHeight(backupWindowBounds.getHeight());
            }
        } else {
            maximized = true;
            backupWindowBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            stage.setX(screen.getVisualBounds().getMinX());
            stage.setY(screen.getVisualBounds().getMinY());
            stage.setWidth(screen.getVisualBounds().getWidth());
            stage.setHeight(screen.getVisualBounds().getHeight());
        }
    }

    void veilScreen() {
        if (veil != null && parentAnchor.getChildren().contains(veil)) {
            parentAnchor.getChildren().remove(veil);
        }
        veil = new Region();
        veil.setPrefHeight(parentAnchor.getHeight());
        veil.setPrefWidth(parentAnchor.getWidth());
        veil.setStyle("-fx-background-color: rgba(0, 0, 150, 0.4)");
        veil.setCenterShape(true);
        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(150, 150);
        p.setProgress(-0.5);
        p.setCenterShape(true);
        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());
        table.itemsProperty().bind(service.valueProperty());
        parentAnchor.getChildren().addAll(veil, p);
    }

    @FXML
    private void login(ActionEvent e) throws IOException {
        String pasw = pass.getText();
        if (!pasw.isEmpty()) {
            if (dc.pass(user.getSelectionModel().getSelectedItem().toString(), pasw)) {
                service.setId(id = dc.recordId);
                //service.start();

                if (dc.getCurrr() != null) {
                    String[] cu = dc.getCurrr().split(" ");
                    System.out.println("cuuuuuu:::::::::: " + cu[0]);
                    currency = NumberFormat.getCurrencyInstance(new Locale(cu[0], cu[1]));
                } else {
                    currency = NumberFormat.getCurrencyInstance(new Locale("ha", "NG"));

                    //currency = NumberFormat.getCurrencyInstance(new Locale("hu", "HU"));
                }
                veilScreen();
                setLogInState();
                service.setId(id);
                service.setRowsLim(rows.getSelectionModel().getSelectedItem().toString());
                service.restart();
                if (service.getState().equals(State.SUCCEEDED)) {
                    doBalance();
                }

                //service.reset();
            } else {
                setOutParseValue("Invalid password");
                setOutParseIcon(new Image(STOP_IMG_LINK));
                showDilogStage("info", "infoOut.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);
                //JOptionPane.showMessageDialog(null, "Invalid password");
            }
        } else {
            setOutParseValue("Please Enter your Password");
            setOutParseIcon(new Image(WARNING_IMG_LINK));
            showDilogStage("info", "infoOut.fxml", Modality.APPLICATION_MODAL, StageStyle.TRANSPARENT, false);
            //JOptionPane.showMessageDialog(null, "Please Enter your Password");
        }
    }

    private void setLogInState() {
        loginState = true;
        prev.setDisable(false);
        next.setDisable(false);
        entryHolder.setDisable(false);
        rows.setDisable(false);
        dp.setVisible(true);
        dp2.setVisible(true);
        chart.setVisible(true);
        pass.setVisible(false);
        passLabel.setVisible(false);
        loginButton.setVisible(false);
    }

    private void setUnLogInState() {
        prev.setDisable(true);
        next.setDisable(true);
        entryHolder.setDisable(true);
        rows.setDisable(true);
        dp.setVisible(false);
        dp2.setVisible(false);
        chart.setVisible(false);
        pass.setVisible(true);
        passLabel.setVisible(true);
        loginButton.setVisible(true);
        //table.setPlaceholder(new ImageView(new Image("/budget/tracker/resource/images/login.png")));

    }

    private void strokeTransitionEffectCircle(Group root, Circle cir) {

        cir.setStroke(Color.DODGERBLUE);
        cir.setStrokeWidth(10);
        //root.getChildren().add(cir);
        StrokeTransition strokeTransition = StrokeTransitionBuilder.create()
                .duration(Duration.seconds(3))
                .shape(cir)
                .fromValue(Color.RED)
                .toValue(Color.DODGERBLUE)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        strokeTransition.play();
    }

    private void createF1CircledStage() {

        final Stage stage = new Stage(StageStyle.TRANSPARENT);
        Group rootGroup = new Group();
        Scene scene = new Scene(rootGroup, 420, 420, Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.show();

        // CREATION OF THE DRAGGER (CIRCLE)
        Circle circledStage = new Circle(210, 210, 200);
        circledStage.setFill(new RadialGradient(-0.3, 135, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[]{
            new Stop(0, Color.DARKGRAY),
            new Stop(1, Color.BLACK)
        }));

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

        // CREATE SIMPLE TEXT NODE
        Text nname, text = new Text("Budget Tracker"); //20, 110,
        text.setFill(Color.WHITESMOKE);
        text.setEffect(new Lighting());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFont(Font.font(Font.getDefault().getFamily(), 45));
        TextArea tx = new TextArea(doc);
//                tx.setFill(Color.DARKORANGE);
        tx.setEffect(new Lighting());
//                tx.setBoundsType(TextBoundsType.VISUAL);
        tx.setFont(Font.font(Font.getDefault().getFamily(), 45));
        tx.setEditable(false);
        tx.setWrapText(true);
        tx.setPrefWidth(300);
        tx.setPrefHeight(200);
        tx.setLayoutX(30);
        // USE A LAYOUT VBOX FOR EASIER POSITIONING OF THE VISUAL NODES ON SCENE
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(60, 0, 0, 20));
        vBox.setAlignment(Pos.TOP_RIGHT);
        Pane pane = new Pane(tx);

        nname = new Text("Farouq Abubakar Bahago");
        nname.setFill(Color.GOLDENROD);
        nname.setEffect(new Lighting());
        nname.setEffect(new DropShadow());
        nname.setBoundsType(TextBoundsType.VISUAL);
        nname.setFont(Font.font(Font.getDefault().getFamily(), 15));

        vBox.getChildren().addAll(text, pane, nname);

        rootGroup.getStylesheets().add("/budget/tracker/resource/css/css.css");

        strokeTransitionEffectCircle(rootGroup, circledStage);
        rootGroup.getChildren().addAll(circledStage, vBox);
        rootGroup.setFocusTraversable(true);
        Tooltip.install(rootGroup, new Tooltip("press ESC key to close"));
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent arg0) {
                if (arg0.getCode() == KeyCode.ESCAPE) {
                    stage.close();
                }
            }
        });
    }

    @FXML
    private void enterKeyOnPass(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER) {
            login(null);
        }
    }

    private void digitalClock() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Date time = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                timeLabel.setText(sdf.format(time));
            }
        }.start();
    }

    String doc = "This particular thesis is aimed at providing basic "
            + "software that will aid in the daily financial transaction"
            + " of households. Studies indicated that vast levels "
            + "of financial mismanagement occur in a number of "
            + "households(Particularly in Nigeria), as such becoming "
            + "an issue considering the situation of this current economy."
            + "This software assists in rectifying or minimizing this"
            + "particularly problem, which is vital to the circulation of fund in a"
            + "household. Moreover, the software will improve and aid the formation of "
            + "household budgets on a monthly basis."
            + "\n"
            + "\n"
            + "\tThe software functions as a budget tracker and a "
            + "record for future reference. The software can have many"
            + " users, and new users can register on the main interface"
            + "of the program.The users can log in when the program start."
            + "After login, the user can start transactions to add "
            + "expenses income and recurring costs. These transactions "
            + "can be saved for further reuse, and can be cancelled."
            + "The user can also use the software to find out how much"
            + "they are saving or losing over a period of time. The "
            + "application also allows the user to specify a date range"
            + " and see the net flow of money in and out ot thehouse "
            + "budget for that time period.";

}
