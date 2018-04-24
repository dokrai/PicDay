package fdi.ucm.picday;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChallengeBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_CHALLENGES = "table_challenges";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_PICTURES = "PICTURES";

    private static final String CREATE_DB = "CREATE TABLE " + TABLE_CHALLENGES + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT NOT NULL, " + COL_DESCRIPTION + " TEXT NOT NULL, " + COL_PICTURES + " TEXT NOT NULL);";

    public ChallengeBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CHALLENGES);
        onCreate(db);
    }
}
