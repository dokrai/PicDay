package fdi.ucm.picday;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Dres on 08/05/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper{

    //Database Version
    public static final int DATABASE_VERSION = 1;
    //DataBase name
    public static final String DATABASE_NAME = "PicDay.db";
    //DataBase tables
    public static final String TABLE_USUARIOS="usuarios";
    public static final String TABLE_CHALLENGES="challenges";

    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (user_name TEXT PRIMARY KEY, email TEXT UNIQUE, password TEXT, date TEXT)";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USUARIOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    public boolean userExists(User user){
        boolean encontrado = true;
        SQLiteDatabase bd = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE user_name = " + user.getUser_name() + " AND password = " + user.getPassword();
        try{
            Cursor fila = bd.rawQuery(query,null);
        }catch(SQLiteException e){
            encontrado = false;
        }
        return encontrado;
    }

    public boolean registerUser(User user){
        SQLiteDatabase bd = this.getWritableDatabase();
        return true;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()){
            db.close();
        }
    }
}
