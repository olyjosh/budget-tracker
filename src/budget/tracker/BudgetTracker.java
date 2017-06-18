package budget.tracker;

import budget.tracker.controllers.HomeController;
import budget.tracker.database.DataClass;
import com.mysql.management.MysqldResource;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author OLYJOSH
 */
public class BudgetTracker extends Application {

    MysqldResource mysqldResource;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("HOME PATH ="+System.getProperty("user.home")+File.separatorChar+"Budget Tracker");
        
        DataClass dc = new DataClass();
        mysqldResource = DataClass.startDatabase(new File(System.getProperty("user.home")+File.separatorChar+"Budget Tracker"), 3307, DataClass.USERNAME, DataClass.PASSWORD);
        if (mysqldResource.isRunning()) {
            dc.createDataBaseAndTable();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/budget/tracker/resource/Home.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Budget Tracker For House Hold Activities");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

        
    }

}
