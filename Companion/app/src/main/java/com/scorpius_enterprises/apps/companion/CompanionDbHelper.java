package com.scorpius_enterprises.apps.companion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rickm on 2/7/2017.
 */

public class CompanionDbHelper extends SQLiteOpenHelper
{
    public static final int    DATABASE_VERSION = 1;
    public static final String DATABASE_NAME    = "PriorityTask.db";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                                                  CompanionDataContract.PriorityTask.TABLE_NAME +
                                                  " (" +
                                                  CompanionDataContract.PriorityTask._ID +
                                                  " INTEGER PRIMARY KEY," +
                                                  CompanionDataContract.PriorityTask.COLUMN_NAME +
                                                  " TEXT," +
                                                  CompanionDataContract.PriorityTask.COLUMN_PRIORITY +
                                                  " INTEGER," +
                                                  CompanionDataContract.PriorityTask.COLUMN_CREATED +
                                                  " INTEGER," +
                                                  CompanionDataContract.PriorityTask.COLUMN_LAST_FIRED +
                                                  " INTEGER)";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " +
                                                  CompanionDataContract.PriorityTask.TABLE_NAME;

    public CompanionDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }
}
