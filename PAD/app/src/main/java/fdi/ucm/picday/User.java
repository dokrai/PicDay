package fdi.ucm.picday;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User{

    private String user_name;
    private String email;
    private String password;
    private String date;
    private int points;
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
        points = 0;
    }

    public User(String user_name, String email, String password, int points){
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        date = today();
        this.points = points;
    }

    public User() {

    }
    
      public String getEmail(){
        return this.email;
    }

    public String getDate(){
        return this.date;
    }

    public String getUser_name(){
        return this.user_name;
    }

    public String getPassword(){
        return this.password;
    }

    public int getPoints() {return this.points;}

    public void setPoints(int points) {this.points = points;}

    public boolean log_in(Context context) {
        boolean encontrado = false;
        DataBaseHelper helper = new DataBaseHelper(context);
        encontrado = helper.userExists(this);
        helper.closeDB();
        return encontrado;
    }

     public int register(Context context) {
        DataBaseHelper helper = new DataBaseHelper(context);
        if (helper.registerUser(this)) {
            return 0;
        } else {
            return -1;

        }
    }

    private String today() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(now);
    }
}
