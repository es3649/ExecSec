package com.es3649.execsec;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.sheets.SheetsFetchTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * TODO we need to be able to change settings for:
 * TODO | Message response templates
 * TODO | Loading people
 * TODO | changing password
 */

public class SettingsActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        SheetsFetchTask.Listener {

    private static final String TAG = "SettingsActivity";

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private GoogleAccountCredential mCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.setLoadLinLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), LoaderActivity.class);
//                startActivity(i);
                Log.d(TAG, "Load Button Pressed");
                loadDBFromSheets();
            }
        });

        // TODO make this switch into an AlertDialog
        ((Switch)findViewById(R.id.setClearDBSafetySwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    findViewById(R.id.setClearDBLinLay).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DB_Proxy db = new DB_Proxy(getApplicationContext());
                            int deletedRecords = db.deletePeople();
                            Toast.makeText(getApplicationContext(),
                                    String.format(Locale.getDefault(),
                                            getString(R.string.setRecordsCleared),
                                            deletedRecords),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    findViewById(R.id.setClearDBLinLay).setOnClickListener(null);
                }
            }
        });

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    //--------------------------------------//
    //---------- DB Loading Logic ----------//
    //--------------------------------------//

    private void loadDBFromSheets() {
        Log.d(TAG, "Fetching sheets data");
        if (! isGooglePlayServicesAvailable()) {
            Log.d(TAG, "Play services unavailable");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d(TAG, "No account selected");
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Log.d(TAG, "Device offline");
            Toast.makeText(this, getString(R.string.setNoNetworkWarning), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Starting task");
            new SheetsFetchTask(mCredential, this)
                    .execute(Secrets.CALLINGS_SHEET_ID, Secrets.NAMES_AND_NUMBERS_RANGE);
        }
    }

    @Override
    public void onSheetReady(List<List<Object>> sheet) {
        DB_Proxy db = new DB_Proxy(this);
        int counter = 0;

        for (List row : sheet) {
            try {
                Person p = new Person((String)row.get(1), (String)row.get(0), (String)row.get(2));
                db.stashPerson(p);
                counter++;
            } catch (Exception ex) {
                Log.e(TAG, "Failed to add person: " + row.get(1) + "," + row.get(0), ex);
            }
        }

        Toast.makeText(this,
                String.format(getString(R.string.setDBPopulated), counter, sheet.size()),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fixPlayServicesAvailability(Exception ex) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this,
                ((GooglePlayServicesAvailabilityIOException) ex).getConnectionStatusCode(),
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void fixUserRecoverableAuthProblem(Exception e) {
        startActivityForResult(
                ((UserRecoverableAuthIOException) e).getIntent(),
                SettingsActivity.REQUEST_AUTHORIZATION);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            Dialog dialog = apiAvailability.getErrorDialog(
                    this, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
            dialog.show();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                loadDBFromSheets();
            } else {
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app need access to your Google Account (Google Sheets).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
        case REQUEST_GOOGLE_PLAY_SERVICES:
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, getString(R.string.setNeedGooglePlay), Toast.LENGTH_SHORT).show();
            } else {
                loadDBFromSheets();
            }
            break;
        case REQUEST_ACCOUNT_PICKER:
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, accountName);
                    editor.apply();
                    mCredential.setSelectedAccountName(accountName);
                    loadDBFromSheets();
                }
            }
            break;
        case REQUEST_AUTHORIZATION:
            if (resultCode == RESULT_OK) {
                loadDBFromSheets();
            }
            break;
        default:
            throw new RuntimeException("Got an invalid activity result");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {}
    public void onPermissionsDenied(int requestCode, List<String> perms) {}


}
