package com.es3649.execsec.data.database;

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
    static final String DB_NAME = "com_es3649_execsec_WARD_PHONES";

    static final String PERSON_TABLE_NAME = "com_es3649_execsec_tbl_people";
    static final String P_NUMBER_COL_ID = "p_number";
    static final String P_GIVEN_NAME_COL_ID = "p_given_name";
    static final String P_SURNAME_COL_ID = "p_surname";

    static final String TRANSACTION_TABLE_NAME = "com_es3649_execsec_tbl_transactions";
    static final String T_NUMBER_PID = "t_transaction_id";
    static final String T_BLOB_COLUMN_ID = "t_json_blob";

    static final String ARCHIVE_TABLE_NAME = "com_es3649_execsec_tbl_archives";
    static final String A_PKEY_ID = "a_pkey";
    static final String A_NUMBER_COLUMN_ID = "a_number";
    static final String A_BLOB_COL_ID = "a_json_blob";

    static final String GROUP_TABLE_NAME = "com_es3649_execsec_tbl_groups";
    static final String G_PKEY_ID = "g_pkey";
    static final String G_NAME = "g_name";
    static final String G_RANGE = "g_range";


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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
        // construct person table
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                        "%s TEXT NOT NULL PRIMARY KEY," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL)", PERSON_TABLE_NAME,
                        P_NUMBER_COL_ID, P_GIVEN_NAME_COL_ID, P_SURNAME_COL_ID)
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

        // construct group table
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                        "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL)", GROUP_TABLE_NAME,
                        G_PKEY_ID, G_NAME, G_RANGE)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // drop tables and restart
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ARCHIVE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
