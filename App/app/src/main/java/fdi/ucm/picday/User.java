package fdi.ucm.picday;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User{

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

    public String getUser_name(){
        return this.user_name;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean log_in(Context context) {
        boolean encontrado = false;
        DataBaseHelper helper = new DataBaseHelper(context);
        encontrado = helper.userExists(this);
        helper.closeDB();
        return encontrado;
    }

    public int register(Context context) {
        DataBaseHelper helper = new DataBaseHelper(context);
        helper.registerUser(this);
        return -1;
    }


    private String today() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(now);
    }
}
