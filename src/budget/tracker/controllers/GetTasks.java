package budget.tracker.controllers;

import budget.tracker.database.DataClass;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author OLYJOSH
 */
public class GetTasks extends Task<ObservableList<Record>> {

    int id;
    String d1;
    String d2;
    String rowsLim;
    String NxtOrPre;
    DataClass dc;

    public GetTasks(int ids, String date1, String date2, String rows, String NxtOrPreRows) {
        id = ids;
        d1 = date1;
        d2 = date2;
        rowsLim = rows;
        NxtOrPre = NxtOrPreRows;
        dc = new DataClass();
    }

    @Override
    protected ObservableList<Record> call() throws Exception {

        int numRow = 0;
        if (!rowsLim.equals("All")) {
            numRow = Integer.parseInt(rowsLim);
        }
        ResultSet rs = dc.resultSetTableData(id, d1, d2, numRow, Integer.parseInt(NxtOrPre));
        ObservableList<Record> data = FXCollections.observableArrayList();
        try {
            int i = 1;
            rs.last();
            int lenght = rs.getRow();
            rs.beforeFirst();
            System.out.println("size of resultSet " + lenght);
            while (rs.next()) {
                updateProgress(i, lenght);
                Record rc = new Record();

                rc.balance.set(rs.getInt("balance"));
                rc.desc.set(rs.getString("descr"));
                rc.date.set(rs.getString("date"));

                if (rs.getInt("type") == 1) {
                    rc.typee.set("INCOME");
                    rc.amount.set(rs.getInt("amount"));
                } else {
                    rc.typee.set("EXPENDITURE");
                    rc.amount.set(-rs.getInt("amount"));
                }

                rc.refId.set(rs.getString("refrenceNo"));
                data.add(rc);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

        return data;
    }
}
