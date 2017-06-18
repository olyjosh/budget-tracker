package budget.tracker.controllers;

import java.text.ParseException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author OLYJOSH
 */
public class Record {

    //date,amount,balance,typee,bye,refId,
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleStringProperty name = new SimpleStringProperty();// as for record entry
    public SimpleStringProperty desc = new SimpleStringProperty();
    public SimpleStringProperty date = new SimpleStringProperty();
    public SimpleIntegerProperty amount = new SimpleIntegerProperty();
    public SimpleIntegerProperty balance = new SimpleIntegerProperty();
    public SimpleStringProperty typee = new SimpleStringProperty();
    public SimpleStringProperty refId = new SimpleStringProperty();

    public String getDate() throws ParseException {
//DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
//Date dat = (Date) formatter.parse(date.get());

        return String.valueOf(date.get());
    }

    public int getAmount() {
        return amount.get();
    }

    public int getBalance() {
        return balance.get();
    }

    public String getTypee() {
        return typee.get();
    }

    public String getRefId() {
        return refId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDesc() {
        return desc.get();
    }

}
