package budget.tracker.controllers;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author OLYJOSH
 */
public class GetTaskService extends Service<ObservableList<Record>> {

    /**
     * Create and return the task for fetching the data. Note that this method
     * is called on the background thread (all other code in this application is
     * on the JavaFX Application Thread!).
     *
     * @return A task
     */
    int id;
    String d1;
    String d2;
    String rowsLim;
    String rowOffseeet;

    public void setId(int id) {
        this.id = id;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public void setRowOffseeet(String rowOffseeet) {
        this.rowOffseeet = rowOffseeet;
    }

    public void setRowsLim(String rowsLim) {
        this.rowsLim = rowsLim;
    }

    public GetTaskService(int ids, String date1, String date2, String rows, String rowOffset) {
        //for limited row lenght
        id = ids;
        d1 = date1;
        d2 = date2;
        rowsLim = rows;
        rowOffseeet = rowOffset;
    }

    public GetTaskService(int ids, String date1, String date2) {
        // for all row lenght
        id = ids;
        d1 = date1;
        d2 = date2;
    }

    @Override
    protected Task createTask() {
        return new GetTasks(id, d1, d2, rowsLim, rowOffseeet);
    }

}
