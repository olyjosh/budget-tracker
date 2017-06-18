package budget.tracker.controllers;

/**
 *
 * @author OLYJOSH
 */
public class User {

    int userID;
    String dateCreated;
    String Password;
    double balance;
    static String currency;

    public User(int userID, String dateCreated, double balance, String curr) {
        this.userID = userID;
        this.dateCreated = dateCreated;
        this.balance = balance;
        this.currency=curr;
    }

    public static String getCurrency() {
        return currency;
    }

    public static void setCurrency(String currency) {
        User.currency = currency;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
