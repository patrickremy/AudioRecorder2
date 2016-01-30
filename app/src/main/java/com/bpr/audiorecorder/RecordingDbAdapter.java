package com.bpr.audiorecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pr on 28/01/16.
 */
public class RecordingDbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_STATUS = "status";
    public static final String KEY_USERID = "userid";
    public static final String KEY_DURATION = "duration";

    private static final String TAG = "RecordingDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Patrick";
    private static final String SQLITE_TABLE = "Recording";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_TIMESTAMP + "," +
                    KEY_STATUS + "," +
                    KEY_USERID + "," +
                    KEY_DURATION +
                    ");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public RecordingDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public RecordingDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public void clearTables() {
        mDb.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        mDb.execSQL(DATABASE_CREATE);
    }

    public long insertRecording(Recording r) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIMESTAMP, r.timestamp.getTime());
        initialValues.put(KEY_STATUS, r.status);
        initialValues.put(KEY_DURATION, r.duration);
        initialValues.put(KEY_USERID, 0); // TODO, manage different user ids

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public Cursor fetchAllRecordings() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_TIMESTAMP, KEY_STATUS, KEY_DURATION, KEY_USERID},
                null, null, null, null, KEY_ROWID + " DESC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void updateStatus(String rowid, int status) {
        mDb.execSQL("UPDATE " + SQLITE_TABLE + " SET "+ KEY_STATUS + " = " + status + " WHERE " + KEY_ROWID + " = " + rowid);

    }

}
