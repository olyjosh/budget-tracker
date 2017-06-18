package budget.tracker.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 *
 * @author OLYJOSH
 */
public class Utility {

    public static ProgressIndicator p;

    static boolean passwordValidator(String pa1, String pa2, Label out, ProgressBar p) {
        if (pa1.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            p.setProgress(0.9);
            p.setStyle("-fx-accent: green;");
        } else if (pa1.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
            p.setStyle("-fx-accent: yellow;");
            p.setProgress(0.5);
        } else {
            p.setStyle("-fx-accent: red;");
            p.setProgress(0.3);
        }
        if (pa1.equals(pa2)) {
            return true;
        } else {
            out.setText("password does not match");
            return false;
        }

    }

    public static Region veilScreen(AnchorPane parentAnchor) {
        Region veil = null;
        if (parentAnchor.getChildren().contains(veil)) {
            parentAnchor.getChildren().remove(veil);
        }
        veil = new Region();
        veil.setPrefHeight(parentAnchor.getHeight());
        veil.setPrefWidth(parentAnchor.getWidth());
        veil.setStyle("-fx-background-color: rgba(0, 0, 150, 0.8)");
        veil.setCenterShape(true);
        p = new ProgressIndicator();
        p.setMaxSize(150, 150);
        p.setProgress(-0.5);
        p.setCenterShape(true);
        parentAnchor.getChildren().addAll(veil, p);
        return veil;
    }

}
