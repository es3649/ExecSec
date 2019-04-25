package com.es3649.execsec.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Handles data access
 *
 * Created by es3649 on 4/25/19.
 */

class DB_Helper extends SQLiteOpenHelper {
    private static final int VERSION = 0;
    public static final String DB_NAME = "WARD_PHONES";

    public static final String PERSON_TABLE_NAME = "tbl_people";
    public static final String P_NUMBER_COL_ID = "p_number";
    public static final String P_NAME_COL_ID = "p_name";

    public static final String CONVERSATION_TABLE_NAME = "tbl_conversations";
    public static final String C_NUMBER_PID = "c_conversaiton_id";
    public static final String C_BLOB_COLUMN_ID = "c_json_blob";

    public DB_Helper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO look for something less injectable
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                        "%s TEXT NOT NULL PRIMARY KEY," +
                        "%s TEXT NOT NULL)", PERSON_TABLE_NAME,
                        P_NUMBER_COL_ID, P_NAME_COL_ID)
        );

        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                                "%s TEXT NOT NULL PRIMARY KEY," +
                                "%s TEXT NOT NULL)", CONVERSATION_TABLE_NAME,
                        C_NUMBER_PID, C_BLOB_COLUMN_ID)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // drop tables and restart
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
