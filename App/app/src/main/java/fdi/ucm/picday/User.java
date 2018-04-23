package fdi.ucm.picday;

public class User {

    private String user_name = "admin";
    private String email = "admin@admin.com";
    private String password = "admin";

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
    }

    public User() {

    }

    public boolean log_in(String user_name, String password) {
        boolean logged = false;
        if (user_name.equals(this.user_name) && password.equals(this.password)) { //this will be a DB query (match user/password)
            logged = true;
            //load_user_DB(user_name);
        }
        return logged;
    }

    public int register(String user_name, String email, String password) {
        int correct;
        if (user_name.equals(this.user_name)) { //this will be a DB query (search the given user name to see if is already being used)
            correct = 1;
        }
        else if (password.length() < 6) { //check if the password is long enough
            correct = 2;
        }
        else {
            //add_user_DB();
            correct = 0;
        }
        return correct;
    }
}
