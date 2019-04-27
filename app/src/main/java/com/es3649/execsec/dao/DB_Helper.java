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
    private static final int VERSION = 4;
    static final String DB_NAME = "WARD_PHONES";

    static final String PERSON_TABLE_NAME = "tbl_people";
    static final String P_NUMBER_COL_ID = "p_number";
    static final String P_NAME_COL_ID = "p_name";

    static final String TRANSACTION_TABLE_NAME = "tbl_transactions";
    static final String T_NUMBER_PID = "t_transaction_id";
    static final String T_BLOB_COLUMN_ID = "t_json_blob";

    static final String ARCHIVE_TABLE_NAME = "tbl_archives";
    static final String A_PKEY_ID = "a_pkey";
    static final String A_NUMBER_COLUMN_ID = "a_number";
    static final String A_BLOB_COL_ID = "a_json_blob";


    DB_Helper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO look for something less injectable
        // TODO delete these later:
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ARCHIVE_TABLE_NAME);
        // construct person table
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                        "%s TEXT NOT NULL PRIMARY KEY," +
                        "%s TEXT NOT NULL)", PERSON_TABLE_NAME,
                        P_NUMBER_COL_ID, P_NAME_COL_ID)
        );

        // construct current transaction table
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                                "%s TEXT NOT NULL PRIMARY KEY," +
                                "%s TEXT NOT NULL)", TRANSACTION_TABLE_NAME,
                        T_NUMBER_PID, T_BLOB_COLUMN_ID)
        );

        // construct archive table
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                                "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                                "%s TEXT NOT NULL," +
                                "%s TEXT NOT NULL)", ARCHIVE_TABLE_NAME,
                        A_PKEY_ID, A_NUMBER_COLUMN_ID, A_BLOB_COL_ID)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // drop tables and restart
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ARCHIVE_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
