package fdi.ucm.picday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ChallengeDB {

    private static final int VERSION = 1;
    private static final String NAME_DB = "challenge.db";

    private static final String TABLE_CHALLENGES = "table_challenges";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 1;
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final int NUM_COL_DESCRIPTION = 2;
    private static final String COL_PICTURES = "PICTURES";
    private static final int NUM_COL_PICTURES= 3;

    private SQLiteDatabase db;
    private ChallengeBaseSQLite challenges;

    public ChallengeDB(Context context) {
        challenges = new ChallengeBaseSQLite(context,NAME_DB,null,VERSION);
    }

    public void openForWrite() {
        db = challenges.getWritableDatabase();
    }

    public void openForRead() {
        db = challenges.getReadableDatabase();
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public long insertChallenge(Challenge challenge) {
        ContentValues content = new ContentValues();
        content.put(COL_NAME, challenge.getName());
        content.put(COL_DESCRIPTION, challenge.getDescription());
        //content.put(COL_PICTURES, challenge.getPictures());
        return db.insert(TABLE_CHALLENGES,null,content);
    }

    public int updateChallenge(int id, Challenge challenge) {
        ContentValues content = new ContentValues();
        content.put(COL_NAME, challenge.getName());
        content.put(COL_DESCRIPTION, challenge.getDescription());
        //content.put(COL_PICTURES, challenge.getPictures());
        return db.update(TABLE_CHALLENGES,content, COL_ID + " = " + id, null);
    }

    public int removeChallenge(String name) {
        return db.delete(TABLE_CHALLENGES,COL_NAME + " = " + name, null);
    }

    public Challenge getChallenge(String name) {
        Cursor cursor = db.query(TABLE_CHALLENGES, new String[] {COL_ID, COL_NAME, COL_DESCRIPTION, COL_PICTURES},
                COL_NAME + " LIKE \"" + name + "\"", null, null, null,COL_NAME);
        return cursorToChallenge(cursor);
    }

    public Challenge cursorToChallenge(Cursor cursor) {
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        Challenge challenge = new Challenge();
        challenge.setId(cursor.getInt(NUM_COL_ID));
        challenge.setName(cursor.getString(NUM_COL_NAME));
        challenge.setDescription(cursor.getString(NUM_COL_DESCRIPTION));
        //challenge.setPictures(new ArrayList<Picture>());
        cursor.close();
        return challenge;
    }

    public ArrayList<Challenge> getAllChallenges() {
        Cursor cursor = db.query(TABLE_CHALLENGES, new String[] {COL_ID, COL_NAME, COL_DESCRIPTION, COL_PICTURES}, null, null,
                null,null, COL_NAME);
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        ArrayList<Challenge> challengesList = new ArrayList<Challenge>();
        while (cursor.moveToNext()) {
            Challenge challenge = new Challenge();
            challenge.setId(cursor.getInt(NUM_COL_ID));
            challenge.setName(cursor.getString(NUM_COL_NAME));
            challenge.setDescription(cursor.getString(NUM_COL_DESCRIPTION));
            //challenge.setPictures(new ArrayList<Picture>());
            challengesList.add(challenge);
        }
        cursor.close();
        return challengesList;
    }
}
