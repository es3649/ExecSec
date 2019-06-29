package com.es3649.execsec.sheets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.es3649.execsec.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.util.List;

public class SheetsFetchTask extends AsyncTask<String, Integer, List<List<Object>>> {

    private static final String TAG = "SheetsFetchTask";

    private Sheets mService;
    private Exception mLastError;
    private ProgressDialog mProgress;
    private Listener mListener;

    public SheetsFetchTask(GoogleAccountCredential credential, Context ctx) {
        if (! (ctx instanceof Listener)) {
            throw new RuntimeException("Must implement Listener interface");
        }
        mListener = (Listener)ctx;

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        mService = new Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(ctx.getResources().getString(R.string.app_name))
                .build();

        // This is deprecated and stops all interaction with the app
        // but I think that's what I want...
        mProgress = new ProgressDialog(ctx);
        mProgress.setMessage(ctx.getResources().getString(R.string.sftLoading));
    }

    public interface Listener {
        void onSheetReady(List<List<Object>> sheet);
        void fixPlayServicesAvailability(Exception e);
        void fixUserRecoverableAuthProblem(Exception e);
    }

    /**
     * Gets data from a spreadsheet
     * @param strings accepts a variable number of strings.
     *                The first ought to be the spreadsheet ID to access
     *                The second should be the range to access
     * @return a 2D list representing that range in that sheet.
     */
    @Override
    protected List<List<Object>> doInBackground(String... strings) {
        try {
            if (strings.length < 2) {
                throw new IllegalArgumentException("Takes 2 arguments");
            }

            return getAPIData(strings[0], strings[1]);

        } catch (Exception ex) {
            mLastError = ex;
            Log.e(TAG, "Sheets fetch failed", ex);
            cancel(true);
            return null;
        }
    }

    private List<List<Object>> getAPIData(String sheetID, String range) throws IOException {
        return this.mService.spreadsheets().values()
                .get(sheetID, range).execute()
                .getValues();
    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<List<Object>> lists) {
        mProgress.hide();
        mListener.onSheetReady(lists);
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        Log.e(TAG, "Error occurred while fetching sheets");
        if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
            mListener.fixPlayServicesAvailability(mLastError);
        } else if (mLastError instanceof UserRecoverableAuthIOException) {
            mListener.fixUserRecoverableAuthProblem(mLastError);
        } else {
            // everything was failed
        }
    }
}
