package fdi.ucm.picday;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private String user_name;
    private String email;
    private String password;
    private String date;

    /*
        Constructor used when trying to log in.
     */
    public User(String user_name, String password){
        this.user_name = user_name;
        this.password = password;
    }

    /*
       Constructor used when trying to register.
    */
    public User(String user_name, String email, String password){
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        date = today();
    }

    public User() {

    }

    public boolean log_in() {
        return true;
        /*
        boolean logged = false;
        String query = "SELECT * FROM users WHERE userName='" + user_name + "' AND password='" + password + "'";
        try {
            Statement st = Login.conn_db.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) {
                logged = true;
                email = rs.getString(2);
                date = rs.getDate(4).toString();
            }
        } catch (SQLException e) {

        }
        return logged;*/
    }

    public int register() {
        int correct = -1;
        String query = "SELECT * FROM users WHERE userName='" + user_name + "'";
        String insert = "INSERT INTO users (userName, email, password, date) VALUES (?,?,?,?)";
        try {
            Statement st = Login.conn_db.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                correct = 1;
            }
        } catch (SQLException e) {

        }

        if (password.length() < 6) { //check if the password is long enough
            correct = 2;
        }
        else if (correct == -1){
            try {
                PreparedStatement pst = Login.conn_db.prepareStatement(insert);
                pst.setString(1, user_name);
                pst.setString(2, email);
                pst.setString(3, password);
                Date today = new Date();
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());
                pst.setDate(4, sqlDate);
                pst.execute();
            } catch (SQLException e) {

            }
            correct = 0;
        }
        return correct;
    }

    private String today() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(now);
    }
}
