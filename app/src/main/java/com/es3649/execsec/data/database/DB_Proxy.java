package com.es3649.execsec.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.es3649.execsec.data.model.Group;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.nlp.conversationmanager.ScheduleTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A database proxy, which hides the database and abstracts all the SQL
 *
 * This is my reference page for this class:
 * https://www.androidauthority.com/how-to-store-data-locally-in-android-app-717190/
 *
 * TODO we need archival functions, and they need to update the transaction status to closed
 * TODO of they need to ensure that they are closed
 *
 * Created by es3649 on 4/25/19.
 */
public class DB_Proxy {
    public DB_Proxy(Context ctx){
        context = ctx;
    }

    private static final String TAG = "dao.DB_Proxy";
    private Context context;

    //----------------------------------------------------------------//
    //----------------------- Person Functions -----------------------//
    //----------------------------------------------------------------//

    /**
     * Stores a person in the local database
     * @param p the person to stash
     */
    public void stashPerson(Person p) {
        Log.d(TAG, "Stashing person with number " + p.getNumber());

        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_Helper.P_GIVEN_NAME_COL_ID, p.getName());
        values.put(DB_Helper.P_SURNAME_COL_ID, p.getSurname());
        values.put(DB_Helper.P_NUMBER_COL_ID, p.getNumber());

        db.insert(DB_Helper.PERSON_TABLE_NAME, null, values);
    }

    public Person lookupPerson(String phoneNumber) {
        Log.d(TAG, "looking for " + phoneNumber);
        String[] projection = {DB_Helper.P_NUMBER_COL_ID,
                DB_Helper.P_GIVEN_NAME_COL_ID, DB_Helper.P_SURNAME_COL_ID};

        String selection = DB_Helper.P_NUMBER_COL_ID + " = ?";
        // standardize phone number format to E164
        String[] selectionArgs = {PhoneNumberUtils.formatNumberToE164(phoneNumber, Person.US_COUNTRY_CODE)};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();

        Cursor cursor = db.query(DB_Helper.PERSON_TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 1) Log.e(TAG, "Found multiple person DB entries for "+ phoneNumber);
        else if (cursor.getCount() == 0) {
            Log.d(TAG, "Person not found");
            return null;
        }

        // the person takes the arguments (name, number)
        // the database column order is number, given name, surname
        cursor.moveToNext();
        Person p = new Person(cursor.getString(1), cursor.getString(2), cursor.getString(0));
        cursor.close();
//        Log.d(TAG, "Found person: " + p.toString());
        return p;
    }



    public Person[] lookupPeopleByName(String name) {
        Log.d(TAG, "Looking for " + name);
        String[] nameList = name.split(" +");
        switch(nameList.length) {
            default:
                Log.e(TAG, String.format("Name '%s' has %d space separated parts",
                        name, nameList.length));
                // I intentionally left out the break here
            case 2:
                Log.d(TAG, "Determined it was a 2 part-name");
                return lookupMultiName(new String[] {nameList[0], nameList[1]});
            case 1:
                Log.d(TAG, "Determined it was a 1 part-name");
                return lookupOneName(name);
        }
    }

    private Person[] lookupOneName(String name) {
        String selection = DB_Helper.P_GIVEN_NAME_COL_ID + " = ? OR " +
                DB_Helper.P_SURNAME_COL_ID + " = ?";
        String[] selectionArgs = {name, name};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();
        Person[] result = doNameLookups(db, selection, selectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Exact matching query returned %d entries", result.length));
            return result;
        }

        // try a like query
        selection = DB_Helper.P_GIVEN_NAME_COL_ID + " LIKE ? OR " +
                DB_Helper.P_SURNAME_COL_ID + " LIKE ?";
        selectionArgs = makeLikeable(selectionArgs);

        result = doNameLookups(db, selection, selectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Like matching query returned %d entries", result.length));
            return result;
        }

        return null;
    }

    private Person[] lookupMultiName(String[] names) {
        // try an exact query with AND
        String selection = DB_Helper.P_GIVEN_NAME_COL_ID + " = ? AND " +
                DB_Helper.P_SURNAME_COL_ID + " = ?";
        String[] selectionArgs = {names[0], names[1]};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();
        Person[] result = doNameLookups(db, selection, selectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Exact matching AND query returned %d entries", result.length));
            return result;
        }

        // try a like query with AND
        selection = DB_Helper.P_GIVEN_NAME_COL_ID + " LIKE ? AND " +
                DB_Helper.P_SURNAME_COL_ID + " LIKE ?";
        String[] likableSelectionArgs = makeLikeable(selectionArgs);

        result = doNameLookups(db, selection, likableSelectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Like matching AND query returned %d entries", result.length));
            return result;
        }

        // try an exact query with OR
        selection = DB_Helper.P_GIVEN_NAME_COL_ID + " = ? OR " +
                DB_Helper.P_SURNAME_COL_ID + " = ?";
        result = doNameLookups(db, selection, selectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Exact matching OR query returned %d entries", result.length));
            return result;
        }

        // try a like query with OR
        selection = DB_Helper.P_GIVEN_NAME_COL_ID + " LIKE ? AND " +
                DB_Helper.P_SURNAME_COL_ID + " LIKE ?";
        result = doNameLookups(db, selection, likableSelectionArgs);

        if (result != null && result.length > 0 ) {
            Log.d(TAG, String.format("Like matching OR query returned %d entries", result.length));
            return result;
        }

        return null;
    }

    /**
     * Does all the dirty work of connecting to the db
     * @param db the database to query
     * @param selection the selection string for the query
     * @param selectionArgs the arguments for the selection
     * @return the list of people which were found in the query
     */
    private Person[] doNameLookups(SQLiteDatabase db, String selection, String[] selectionArgs) {
        String[] projection = {DB_Helper.P_NUMBER_COL_ID,
                DB_Helper.P_GIVEN_NAME_COL_ID, DB_Helper.P_SURNAME_COL_ID};

        Cursor cursor;
        try {
            cursor = db.query(DB_Helper.PERSON_TABLE_NAME, projection,
                    selection, selectionArgs, null, null, null);
        } catch (Exception ex) {
            Log.e(TAG, "SQL failure", ex);
            return null;
        }

        Person[] result = processPersonResults(cursor);
        cursor.close();
        return result;
    }

    private Person[] processPersonResults(Cursor cursor) {
        Person[] result = new Person[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Person p = new Person(cursor.getString(1), cursor.getString(2), cursor.getString(0));

            result[i] = p;
        }
        return result;
    }

    /**
     * Makes a string more useful for sql like clauses by adding the '%' character.
     *
     * @param likeMe ta list of strings to be modified
     * @return the same list of strings with each one modified as described.
     */
    private String[] makeLikeable(String likeMe[]) {
        for (int i = 0; i < likeMe.length; i++) {
            likeMe[i] = "%" + likeMe[i] + "%";
        }
        return likeMe;
    }

    /**
     * deletes all the people in the peopleDB.
     * @return the number of deleted records
     */
    public int deletePeople() {
        Log.i(TAG, "Deleting people");

        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        return db.delete(DB_Helper.PERSON_TABLE_NAME, null, null);
    }

    //---------------------------------------------------------------//
    //-------------------- Transaction Functions --------------------//
    //---------------------------------------------------------------//

    /**
     * Stashes a transaction in the database
     * @param st the transaction to stash, it will be stashed as active
     */
    public void stashTransaction(ScheduleTransaction st) {
        Log.d(TAG, String.format("Stashing transaction for %s and status %d",
                st.getWithWho().getNumber(), st.getStatus()));

        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_Helper.T_NUMBER_PID, st.getWithWho().getNumber());
        values.put(DB_Helper.T_BLOB_COLUMN_ID, st.serialize());

        db.insert(DB_Helper.TRANSACTION_TABLE_NAME, null, values);
    }


    public void deleteTransaction(String phoneNumber) {
        // TODO
    }

    /**
     * Looks up the current transaction associated with the given number
     * @param phoneNumber lookup the transaction with this number
     * @return the transaction. May be null if nothing is found.
     */
    @Nullable
    public ScheduleTransaction lookupTransaction(String phoneNumber) {
        String json = lookupTransactionJson(phoneNumber);

        if (json == null) { return null; }
        return ScheduleTransaction.fromJson(json);
    }

    @Nullable
    public ScheduleTransaction[] lookupArchivedTransaction(String phoneNumber) {
        // TODO
        return null;
    }

    /**
     * Updates a Transaction entry with the new transaction
     * @param phoneNumber updates the transaction associated with this number
     * @param st the new transaction to store.
     */
    public void updateTransaction(String phoneNumber, ScheduleTransaction st) {
        final String whereClause = DB_Helper.T_NUMBER_PID + " = ? ";

        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_Helper.T_BLOB_COLUMN_ID, st.serialize());

        String[] whereArgs = new String[1];
        whereArgs[1] = phoneNumber;

        db.update(DB_Helper.TRANSACTION_TABLE_NAME, values, whereClause, whereArgs);
    }

    public void archiveTransaction(String phoneNumber) {
        // TODO
    }

    /**
     * internal intermediary for fetching the json for a certain transaction
     * from the current transaction.
     * @param phoneNumber the phone number of the current transaction to lookup.
     * @return the json of the related transaction
     */
    private String lookupTransactionJson(String phoneNumber) {
        Log.d(TAG, "Looking up transaction for " + phoneNumber);

        Log.d(TAG, "looking for " + phoneNumber);
        String[] projection = {DB_Helper.T_BLOB_COLUMN_ID};

        String selection = DB_Helper.T_NUMBER_PID + " = ?";
        String[] selectionArgs = {phoneNumber};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();

        Cursor cursor = db.query(DB_Helper.TRANSACTION_TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 1) Log.e(TAG, "Found multiple current-transaction DB entries for "+ phoneNumber);
        else if (cursor.getCount() == 0) return null;

        String json = cursor.getString(0);
        cursor.close();
        return json;
    }

    /**
     * Archives a transaction, but it only takes string representations of the values
     * instead of object representations.
     * @param phoneNumber the number of the current transaction to archive
     * @param transactionJson the json of the transaction to archive
     */
    private void stashArchiveStrings(String phoneNumber, String transactionJson) {
        Log.d(TAG, "Archiving transaction with for " + phoneNumber);

        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_Helper.A_NUMBER_COLUMN_ID, phoneNumber);
        values.put(DB_Helper.A_BLOB_COL_ID, transactionJson);

        db.insert(DB_Helper.ARCHIVE_TABLE_NAME, null, values);
    }

    /**
     * Fetches ALL transactions in the database, both current and archived.
     * @return A list of all transactions, guaranteed not null.
     */
    public List<ScheduleTransaction> getTransactions() {
        List<ScheduleTransaction> result = getCurrentTransactions();
        result.addAll(getArchivedTransactions());
        return result;
    }

    /**
     *
     * @return A list of the current transactions, guaranteed not null.
     */
    public List<ScheduleTransaction> getCurrentTransactions() {
        String[] projection = {DB_Helper.T_BLOB_COLUMN_ID};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();
        Cursor cursor = db.query(DB_Helper.TRANSACTION_TABLE_NAME, projection,
                null, null, null, null, null);

        List<ScheduleTransaction> result = new ArrayList<ScheduleTransaction>();

        // return empty list if no results
        if (cursor.getCount() == 0) return result;

        do {
            // get the json out and inflate it
            result.add(ScheduleTransaction.fromJson(cursor.getString(0)));
        } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    /**
     *
     * @return A list of archived transactions, guaranteed not null.
     */
    public List<ScheduleTransaction> getArchivedTransactions() {
        String[] projection = {DB_Helper.A_BLOB_COL_ID};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();
        Cursor cursor = db.query(DB_Helper.ARCHIVE_TABLE_NAME, projection,
                null, null, null, null, null);

        List<ScheduleTransaction> result = new ArrayList<ScheduleTransaction>();

        // return empty list if no results
        if (cursor.getCount() == 0) return result;

        do {
            // get the json out and inflate it
            result.add(ScheduleTransaction.fromJson(cursor.getString(0)));
        } while (cursor.moveToNext());

        cursor.close();
        return result;
    }

    //-------------------------------------//
    //---------- Group Functions ----------//
    //-------------------------------------//

    /**
     * @return all of the group entries from the DB
     */
    public List<Group> getAllGroups() {
        String[] projection = {DB_Helper.G_PKEY_ID, DB_Helper.G_NAME, DB_Helper.G_RANGE};

        SQLiteDatabase db = new DB_Helper(context).getReadableDatabase();
        Cursor cursor = db.query(DB_Helper.GROUP_TABLE_NAME, projection,
                null, null, null, null, null);

        List<Group> result = new ArrayList<>();

        // return empty is no results
        if(cursor.getCount() == 0) return result;

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            Group g = new Group(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
            result.add(g);

            Log.d(TAG, String.format("Pulled group-- Name: '%s' Range: '%s'",
                    g.getName(), g.getRange()));

        }

        cursor.close();
        return result;
    }

    /**
     * @param g the group to stash, the internal id is ignored
     * @return a copy of the same group, but with an updated id
     */
    public Group stashGroup(Group g) {
        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_Helper.G_NAME, g.getName());
        values.put(DB_Helper.G_RANGE, g.getName());

        long id = db.insert(DB_Helper.GROUP_TABLE_NAME, null, values);

        return new Group(g.getName(), g.getRange(), (int)id);
    }

    /**
     * Deletes a group from the database (given the groupID)
     * @param g the group to delete
     * @return the number of entries deleted
     */
    public int deleteGroup(Group g) {
        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();

        String whereClause = DB_Helper.G_PKEY_ID + " = ?";
        String[] whereParams = { Long.toString(g.getId()) };

        return db.delete(DB_Helper.GROUP_TABLE_NAME, whereClause, whereParams);
    }

    /**
     * Updates the group with the ID of the given group to contain the data
     * in this group
     * @param g the group to update (with updated information)
     * @return the number of entries updated
     */
    public int updateGroups(Group g) {
        SQLiteDatabase db = new DB_Helper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DB_Helper.G_NAME, g.getName());
        values.put(DB_Helper.G_RANGE, g.getRange());
        values.put(DB_Helper.G_PKEY_ID, g.getId());

        String whereClause = DB_Helper.G_PKEY_ID + " = ?";
        String[] whereParams = { Long.toString(g.getId()) };

        return db.update(DB_Helper.GROUP_TABLE_NAME, values, whereClause, whereParams);
    }
}
