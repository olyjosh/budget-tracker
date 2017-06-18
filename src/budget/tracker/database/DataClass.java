package budget.tracker.database;

import budget.tracker.controllers.Record;
import com.mysql.management.MysqldResource;
import com.mysql.management.MysqldResourceI;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Oljursh
 */
public class DataClass {

//    static final String USERNAME = "root";
//    static final String PASSWORD = "sadiq";
       
    public static final String USERNAME = "sadeeq";
    public static final String PASSWORD = "bahago";
    public static final String DB_URL = "jdbc:mysql://localhost:3307/BudgetTrackerDB";
    // static final String DB_URL2 = "jdbc:mysql://localhost:3306/BudgetTrackerDBsssssss";

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    PreparedStatement ps = null;

    public int recordId;

    private int r;

    java.util.Date date = new java.util.Date();
    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    String now = formatter.format(date);

    StringBuilder build = null;
    String createDataBase = "createDatabaseIfNotExist";
    String createTableLogindb = "CREATE TABLE IF NOT EXISTS record ("
            + "  id int(11) NOT NULL AUTO_INCREMENT,"
            + "  name varchar(20) DEFAULT NULL,"
            + "  descr varchar(20) DEFAULT NULL,"
            + "  pass varchar(20) DEFAULT NULL,"
            + "  amount decimal(10,2) unsigned zerofill DEFAULT NULL,"
            + "  curr varchar(10) DEFAULT NULL,"
            + "  dateCreated timestamp NULL DEFAULT NULL,"
            + "  PRIMARY KEY (id)"
            + ")";
    String createTableTrack = "CREATE TABLE IF NOT EXISTS tracks ("
            + "  id int(11) NOT NULL AUTO_INCREMENT,"
            + "  recordID int(11) DEFAULT NULL,"
            + "  amount decimal(10,2) DEFAULT NULL,"
            + "  balance decimal(10,2) DEFAULT NULL,"
            + "  type tinyint(4) DEFAULT NULL,"
            + "  date timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',"
            + "  refrenceNo varchar(10) DEFAULT NULL,"
            + "  descr varchar(200) DEFAULT NULL,"
            + "  PRIMARY KEY (id)"
            + ")";

    public void createDataBaseAndTable() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            Properties p = new Properties();
            p.put(createDataBase, "true");
            p.put("user", USERNAME);
            p.put("password", PASSWORD);
            Connection connection1 = null;
            Statement statement1 = null;
            try {
                //Class.forName(DRIVER);
                connection1 = DriverManager.getConnection(DB_URL, p);
                statement1 = connection1.createStatement();
                statement1.executeUpdate(createTableLogindb);
                statement1.executeUpdate(createTableTrack);
//                if(getPassWord()==null){
//                    String default_password = "insert into logindb(password) values('"+hashPw+"')";
//                    statement1.executeUpdate(default_password);  
//                }        
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection1.close();
            statement1.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnections() {
        try {

            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObservableList<Record> getTransactionFx(long accNo) {
        ObservableList<Record> buildData = null;
        connectToDatabase();
        try {
            ps = connection.prepareStatement("select date,amount,balance,typee,bye,refId,operator from transaction where AccountNo=? ORDER BY date DESC");
            ps.setLong(1, accNo);
            resultSet = ps.executeQuery();
            buildData = buildData(resultSet);

            ps.close();
            closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return buildData;
    }

//    DefaultTableModel buildTableModel(ResultSet rs)throws SQLException {
//
//    ResultSetMetaData metaData = rs.getMetaData();
//    Vector<String> columnNames = new Vector<String>();
//    int columnCount = metaData.getColumnCount();
//    for (int column = 1; column <= columnCount; column++) {
//        columnNames.add(metaData.getColumnName(column));
//    }
//    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
//    while (rs.next()) {
//        Vector<Object> vector = new Vector<Object>();
//        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
//            vector.add(rs.getObject(columnIndex));
//        }
//        data.add(vector);
//    }
//    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
//        @Override
//        public boolean isCellEditable(int row, int column) {
//            return false;
//        }
//    };              
//    return  tableModel;
//    }
    public ObservableList<XYChart.Series<String, Number>> plotLines;
    public XYChart.Series<String, Number> seriesInc, seriesExp;// = new XYChart.Series<String,Number>();
    //XYChart.Series<String,Number> [] plotLines= new XYChart.Series<String,Number>[2];    

    public ObservableList<Record> buildData(ResultSet rs) {
        ObservableList<Record> data = FXCollections.observableArrayList();
        seriesExp = new XYChart.Series<String, Number>();
        seriesInc = new XYChart.Series<String, Number>();

        try {

            while (rs.next()) {
                Record rc = new Record();
                rc.balance.set(rs.getInt("balance"));
                rc.desc.set(rs.getString("descr"));
                String date = rs.getString("date");
                rc.date.set(date);
                if (rs.getInt("type") == 1) {
                    rc.typee.set("INCOME");
                    int aInt = rs.getInt("amount");
                    rc.amount.set(rs.getInt("amount"));
                    seriesInc.getData().add(new XYChart.Data<String, Number>(date, aInt));
                } else {
                    rc.typee.set("EXPENDITURE");
                    int aInt = -rs.getInt("amount");
                    rc.amount.set(aInt);
                    seriesExp.getData().add(new XYChart.Data<String, Number>(date, aInt));
                }
                seriesExp.setName("Expenditure");
                seriesInc.setName("Income");
                rc.refId.set(rs.getString("refrenceNo"));
                data.add(rc);
            }
            plotLines = FXCollections.observableArrayList();

            plotLines.add(seriesInc);
            plotLines.add(seriesExp);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return data;
    }

    public ObservableList<XYChart.Series<String, Number>> buildPlotLineSeries(ResultSet rs) {
        ObservableList<Record> data = FXCollections.observableArrayList();
        seriesExp = new XYChart.Series<String, Number>();
        seriesInc = new XYChart.Series<String, Number>();

        try {

            while (rs.next()) {
                Record rc = new Record();
                String date = rs.getString("date");
                rc.date.set(date);
                System.out.println(date + "--");
                if (rs.getInt("type") == 1) {
                    int aInt = rs.getInt("amount");
                    seriesInc.getData().add(new XYChart.Data<String, Number>(date, aInt));
                    System.out.print(aInt);
                } else {
                    int aInt = -rs.getInt("amount");
                    seriesExp.getData().add(new XYChart.Data<String, Number>(date, aInt));
                    System.out.print(aInt);
                }
                seriesExp.setName("Expenditure");
                seriesInc.setName("Income");
            }
            plotLines = FXCollections.observableArrayList();
            plotLines.add(seriesInc);
            plotLines.add(seriesExp);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return plotLines;
    }

    public boolean createNewRec(String name, String pass, String desc, double start,String curr) {
        connectToDatabase();

        boolean state = false;
        try {
            ps = connection.prepareStatement("insert into record(name,pass,descr,amount,curr) values (?,?,?,?,?)");
            ps.setString(1, name);
            ps.setString(2, pass);
            ps.setString(3, desc);
            ps.setDouble(4, start);
            ps.setString(5, curr);
            r = ps.executeUpdate();
//            ps.close();
//            closeConnections();
            if (r == 1) {
                ps = connection.prepareStatement("select LAST_INSERT_ID() as last_id from record");
                ResultSet rs = ps.executeQuery();
                int aInt = 0;//= rs.getInt("last_id");
                while (rs.next()) {
                    aInt = rs.getInt("last_id");
                };
                addEntry(aInt, start, "#STARTING CAPITAL", r, "");
                state = true;
            }
            ps.close();
            closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }

    public ObservableList<Record> populateTableData(String id, String d1, String d2, String lim) {
        ObservableList<Record> buildData = null;
        System.out.println(id + d1 + d2 + lim);
        connectToDatabase();
        try {
            //ps = connection.prepareStatement("select date,amount,balance,typee,bye,refId,operator from transaction where AccountNo=? ORDER BY date DESC");
            String q = "select * from tracks where recordID=? ORDER BY date DESC";

            if (d1 != null && d2 != null) {
                //q="select * FROM tracks WHERE recordID=? AND date >=? AND date <=? ORDER BY date DESC";
                q = "select * FROM tracks WHERE recordID=? AND date BETWEEN ? AND ? ORDER BY date DESC";
                ps = connection.prepareStatement(q);
                ps.setString(1, id);
                ps.setString(2, d1);
                ps.setString(3, d2);

            } else {
                ps = connection.prepareStatement(q);
                ps.setString(1, id);
            }
            resultSet = ps.executeQuery();
            buildData = buildData(resultSet);
            ps.close();
            closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buildData;
    }

    public List recordEntryList;

    public ObservableList<Record> recordEntryModel() {
        recordEntryList = new LinkedList();
        ObservableList<Record> buildData = FXCollections.observableArrayList();
        String query = null;

        connectToDatabase();
        try {
            ps = connection.prepareStatement("select * from record ORDER BY dateCreated DESC");
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Record rc = new Record();
                rc.name.set(resultSet.getString("name"));
                recordEntryList.add(resultSet.getString("name"));
                buildData.add(rc);
            }

            //buildData = buildData(resultSet);  
            ps.close();
            closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buildData;
    }

    public void setRecordId(int recId) {
        recordId = recId;
    }

    public ResultSet resultSetTableData(int id, String d1, String d2, int lim, int offset) {
        ObservableList<Record> buildData = null;
        System.out.println(id + d1 + d2 + lim);
        connectToDatabase();
        try {
            //ps = connection.prepareStatement("select date,amount,balance,typee,bye,refId,operator from transaction where AccountNo=? ORDER BY date DESC");
            String q = "SELECT * FROM tracks WHERE recordID=? ORDER BY date DESC LIMIT ?,?";
            if (d1 != null && d2 != null) {
                q = "SELECT * FROM tracks WHERE recordID=? AND date >=? AND date <=? ORDER BY date DESC LIMIT ?,?";
                ps = connection.prepareStatement(q);
                ps.setInt(1, id);
                ps.setString(2, d1);
                ps.setString(3, d2);
                ps.setInt(4, offset);
                ps.setInt(5, lim);
            } else {
                ps = connection.prepareStatement(q);
                ps.setInt(1, id);
                ps.setInt(2, offset);
                ps.setInt(3, lim);
            }
            resultSet = ps.executeQuery();
            buildData = buildData(resultSet);
            //ps.close();
            //closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }

    public boolean addEntry(int id, double amount, String desc, int type, String refNo) {
        boolean status = false;
        connectToDatabase();
        try {
            ps = connection.prepareStatement("select balance from tracks where recordID=?");
            ps.setInt(1, id);
            resultSet = ps.executeQuery();
            double balance = 0d;
            while (resultSet.next()) {
                balance = resultSet.getDouble("balance");
                ps.clearParameters();
            }

            if (type == 1) {
                balance += amount;
            } else {
                balance -= amount;
            }
            ps = connection.prepareStatement("insert into tracks(recordID,amount,balance,type,refrenceNo,descr,date)"
                    + " values(?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setDouble(2, amount);
            ps.setDouble(3, balance);
            ps.setInt(4, type);
            ps.setString(5, refNo);
            ps.setString(6, desc);
            ps.setString(7, now);
            status = ps.execute();
            System.out.println("Done deal");
            System.out.println(status);
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public String currr;

    public String getCurrr() {
        return currr;
    }
    public boolean pass(String user, String pass) {
        connectToDatabase();
        String q = "select id,pass,curr from record where name=? limit 1";
        String getPass;
        try {
            ps = connection.prepareStatement(q);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                getPass = rs.getString("pass");
                if (pass.equals(getPass)) {
                    recordId = rs.getInt("id");
                    currr = rs.getString("curr");
                    System.out.println("--------------------------currency "+currr);
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean confirmPass(String user, String pass) {
        connectToDatabase();
        String q = "select pass from record where name=? limit 1";
        String getPass;
        try {
            ps = connection.prepareStatement(q);
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                getPass = rs.getString("pass");
                if (pass.equals(getPass)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteRecord(String name) {

        boolean status = true;
        connectToDatabase();

        try {
            ps = connection.prepareStatement("DELETE FROM record where name=?");
            ps.setString(1, name);
            status = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean deleteTracks(String date, double amount) {

        boolean status = true;
        connectToDatabase();

        try {
            double balan = 0;
            int lastId = 0;
            ps = connection.prepareStatement("select balance from tracks where recordID=? ORDER BY id DESC LIMIT 1");
            ps.setInt(1, recordId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastId = rs.getInt("id");
                balan = rs.getDouble("balance");
            }
            if (rs != null) {
//                    if(add){balan+=amount;
//                    }else{balan-=amount;}
                balan += amount;
                ps = connection.prepareStatement("update tracks set balance=? where id=?");
                ps.setDouble(1, balan);
                ps.setInt(2, lastId);

                ps = connection.prepareStatement("DELETE FROM tracks where date=?");
                ps.setString(1, date);
                status = ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

//     public boolean editTrack(String date, double amount,String desc,String ref,int select) {
//
//        boolean status = true;
//        connectToDatabase();
//
//        try {
//            double balan = 0;
//            ResultSet rs = ps.executeQuery();
//
//                
//                //recordID,amount,balance,type,refrenceNo,descr,date
//                ps = connection.prepareStatement("update tracks set balance=?, where id=?");
//                ps.setDouble(1, balan);
//                ps.setInt(2, lastId);
//
//                ps = connection.prepareStatement("DELETE FROM tracks where date=?");
//                ps.setString(1, date);
//                status = ps.execute();
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return status;
//    }
    
    public double balance() {
        connectToDatabase();
        double balan = 0;
        try {

            ps = connection.prepareStatement("select balance from tracks where recordID=? ORDER BY id DESC LIMIT 1");
            ps.setInt(1, recordId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                balan = rs.getDouble("balance");
            }
            return balan;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balan;
    }

//        public boolean deleteTracks(){
//        
//        }
    public void changePass(String newPass, String name) {
        connectToDatabase();
        try {
            ps = connection.prepareStatement("UPDATE record SET pass=? WHERE name=? ");
            ps.setString(2, name);
            ps.setString(1, newPass);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DataClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static MysqldResource startDatabase(File databaseDir, int port, String userName, String password) {
        MysqldResource mysqldResource = new MysqldResource(databaseDir);
        Map<String, String> database_options = new HashMap<String, String>();
        database_options.put(MysqldResourceI.PORT, Integer.toString(port));
        database_options.put(MysqldResourceI.INITIALIZE_USER, "true");
        database_options.put(MysqldResourceI.INITIALIZE_USER_NAME, userName);
        database_options.put(MysqldResourceI.INITIALIZE_PASSWORD, password);
//database_options.put("--storage-engine", "INNODB");
//database_options.put("character_set_client_handshake", "false");**
//database_options.put("character_set_connection", "utf8");
//database_options.put("character_set_database", "utf8");
//database_options.put("character_set_results", "utf8");

//database_options.put("character-set-system", "latin1");
//database_options.put("default-character-set", "utf8");
        database_options.put("relay-log", "MysqldResource-relay-bin");//--relay-log=MysqldResource-relay-bin
//database_options.put("socket", "mysql.sock");
        database_options.put("max_allowed_packet", "100M");//4294967296
        database_options.put("read_buffer_size", "268435456");
        database_options.put("bulk_insert_buffer_size", "268435456");
//database_options.put("character_set_client_handshake", "false");2048576000
//database_options.put("character_set_connection", "utf8");
//database_options.put("character_set_database", "utf8");
//database_options.put("character_set_results", "utf8");
        database_options.put("character_set_server", "latin1");//

        database_options.put("ft_min_word_len", "1");
        database_options.put("ft_stopword_file", "");
//database_options.put("character-set-system", "latin1");
//database_options.put("default-character-set", "utf8");
        database_options.put("wait_timeout", "614880000");//7 days befor connection abort
        database_options.put("max_connections", "20000");

        mysqldResource.start("mysqld-olofumark", database_options);
        

        /*Map<String, String> map = mysqldResource.getServerOptions();
         Set<String> setd = map.keySet();
         Object[] objectx = setd.toArray();
         Collection<String> set = map.values();
         Object[] objects = set.toArray();
         for (int a = 0; a < objects.length; a++) {
         UtilitiesHome.logDebug(String.valueOf(objectx[a]) + " === " + map.get(objectx[a].toString()));
         }*/
        return mysqldResource;
    }

}
