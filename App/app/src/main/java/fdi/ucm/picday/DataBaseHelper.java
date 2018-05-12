package fdi.ucm.picday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class DataBaseHelper extends SQLiteOpenHelper{

    //Database Version
    private static final int DATABASE_VERSION = 1;
    //DataBase name
    private static final String DATABASE_NAME = "PicDay.db";
    //DataBase tables
    private static final String TABLE_USUARIOS="usuarios";
    private static final String TABLE_CHALLENGES = "challenges";
    private static final String TABLE_FOTOS="fotos";
    private static final String TABLE_FOTOS_GUARDADAS="fotosGuardadas";

    //columnas usuarios
    private static final String COL_USER_NAME = "user_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_DATE = "date";

    //columnas challenges
    private static final String COL_ID_CHALLENGE = "id_challenge";
    private static final String COL_NAME = "name_challenge";
    private static final String COL_DESCRIPTION = "desc_challenge";
    private static final String COL_PICTURES = "pics";

    //columnas fotos
    private static final String COL_ID_FOTO = "id_foto";
    private static final String COL_IMG = "img";
    private static final String COL_OWNER="owner";
    private static final String COL_PIC_CHALLENGE="pic_challenge";
    private static final String COL_SCORE = "score_foto";
    private static final String COL_TIMES_SCORED = "times_scored";

    //columnas fotos guardadas
    private static final String COL_ID_USUARIO_GUARDADAS="id_user_guardada";
    private static final String COL_ID_FOTO_GUARDADAS="id_foto_guardada";

    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (" + COL_USER_NAME + " TEXT PRIMARY KEY, " + COL_EMAIL
                                                        + " TEXT UNIQUE, " + COL_PASSWORD + " TEXT, " + COL_DATE + " TEXT)";

    private static final String CREATE_TABLE_CHALLENGES = "CREATE TABLE " + TABLE_CHALLENGES + " (" + COL_ID_CHALLENGE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + COL_NAME + " TEXT NOT NULL, " + COL_DESCRIPTION + " TEXT NOT NULL, " + COL_PICTURES + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_FOTOS = "CREATE TABLE " + TABLE_FOTOS + " (" + COL_ID_FOTO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + COL_IMG + " BLOB, " + COL_OWNER + "TEXT NOT NULL" + COL_PIC_CHALLENGE + "TEXT NOT NULL" + COL_SCORE
                                                        + " INTEGER NOT NULL, " + COL_TIMES_SCORED + " INTEGER NOT NULL), FOREIGN KEY (" + COL_OWNER + ") REFERENCES "
                                                        + TABLE_USUARIOS + "(" + COL_USER_NAME + ");";

    private static final String CREATE_TABLE_GUARDADAS="CREATE TABLE " + TABLE_FOTOS_GUARDADAS + " (" + COL_ID_USUARIO_GUARDADAS + " TEXT PRIMARY KEY, " + COL_ID_FOTO_GUARDADAS
                                                        + " TEXT UNIQUE, FOREIGN KEY(" + COL_ID_USUARIO_GUARDADAS + ") REFERENCES " + TABLE_USUARIOS +"(" + COL_USER_NAME
                                                        + "), FOREIGN KEY(" + COL_ID_FOTO_GUARDADAS + ")REFERENCES " + TABLE_FOTOS + "(" + COL_ID_FOTO + "))";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_CHALLENGES);
        db.execSQL(CREATE_TABLE_FOTOS);
        db.execSQL(CREATE_TABLE_GUARDADAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTOS_GUARDADAS);
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

    public void guardarImagen(Picture img){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        Bitmap bitmap= img.getPic();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] blop = baos.toByteArray();
        SQLiteDatabase bd = this.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_FOTOS + "("+ COL_ID_FOTO +"," + COL_IMG + "," + COL_SCORE + "," + COL_TIMES_SCORED + ") VALUES (?,?,?,?)";
        SQLiteStatement insert = bd.compileStatement(sql);
        insert.bindDouble(1,img.getId());
        insert.bindBlob(2,blop);
        insert.bindDouble(3,img.getScore());
        insert.bindDouble(4,img.getTimes_scored());
    }

    public Bitmap buscarImagen(String id){
        SQLiteDatabase bd = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_FOTOS + " WHERE id like '%" + id + "%'";
        Cursor cursor = bd.rawQuery(sql,new String[]{});
        Bitmap bitmap = null;
        if(cursor.moveToFirst()){
            byte[]blop = cursor.getBlob(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(blop);
            bitmap = BitmapFactory.decodeStream(bais);
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        bd.close();
        return bitmap;
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
