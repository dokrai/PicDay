package fdi.ucm.picday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

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
        SQLiteDatabase bd = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE user_name like '%" + user.getUser_name() + "%' AND password like '%" + user.getPassword() + "%'";
        try{
            Cursor fila = bd.rawQuery(query,null);
            if(fila.moveToFirst()){
                return true;
            }else{
                return false;
            }
        }catch(SQLiteException e){
            return false;
        }
    }

    public boolean registerUser(User user){
        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", user.getUser_name());
        contentValues.put("password", user.getPassword());
        contentValues.put("email", user.getEmail());
        contentValues.put("date", user.getDate());

        long result = bd.insert(TABLE_USUARIOS,null,contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()){
            db.close();
        }
    }
}
