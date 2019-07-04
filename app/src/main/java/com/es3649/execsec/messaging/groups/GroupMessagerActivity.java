package com.es3649.execsec.messaging.groups;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.es3649.execsec.R;
import com.es3649.execsec.Secrets;
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Group;
import com.es3649.execsec.messaging.Messager;
import com.es3649.execsec.messaging.UI.MessagerActivity;
import com.es3649.execsec.sheets.SheetsFetchTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GroupMessagerActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks,
        SheetsFetchTask.Listener {

    private static final String TAG = "GroupMessagerActivity";

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS_READONLY };
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private GoogleAccountCredential mCredential;

    private List<Group> gList;
    private String range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messager);

        gList = new DB_Proxy(this).getAllGroups();

        // wire up the recycler
        RecyclerView rcv = findViewById(R.id.gmsgRecycler);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(new GroupListAdapter());

        // get a credential
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    /**
     * Does the dirty work of fetching the group then starting the activity.
     * @param i the position of the group (in glist) to text
     */
    void textThisGroup(int i) {
        range = gList.get(i).getRange();
        getContactsFromSheet();
    }




    //-------------------------------------------//
    //----------- Google Sheets Logic -----------//
    //-------------------------------------------//

    private void getContactsFromSheet() {
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
                    .execute(Secrets.CALLINGS_SHEET_ID, range);
        }
    }

    @Override
    public void onSheetReady(List<List<Object>> sheet) {
        ArrayList<String> contacts = new ArrayList<>();

        for (List<Object> row : sheet) {
            for (Object val : row) {

                try {
                    contacts.add((String)val);
                } catch (Exception ex) {
                    Log.e(TAG, "Found a non string: "+((String) val.toString()));
                }

            }
        }

        Intent i = new Intent(this, Messager.class);
        i.putExtra(MessagerActivity.ARG_RECIPIENTS, TextUtils.join(",",contacts));
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
                GroupMessagerActivity.REQUEST_AUTHORIZATION);
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
                getContactsFromSheet();
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
                    getContactsFromSheet();
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
                        getContactsFromSheet();
                    }
                }
                break;

            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getContactsFromSheet();
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


    //--------------------------------------------//
    //---------- Adapter and Viewholder ----------//
    //--------------------------------------------//

    /**
     * An adapter class for a list of groups to message
     */
    private class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.StaticGroupViewHolder> {

        GroupListAdapter() {}

        @Override
        public StaticGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                                    R.layout.viewholder_group_static, parent, false);

            return new StaticGroupViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(StaticGroupViewHolder holder, int position) {
            holder.bind(gList.get(position), position);
        }

        /**
         * The viewholder for this recycler
         */
        private class StaticGroupViewHolder extends RecyclerView.ViewHolder {

            private Button submitButton;
            private TextView nameTextView;
            private TextView rangeTextView;


            StaticGroupViewHolder(View v) {
                super(v);

                nameTextView = v.findViewById(R.id.sgmvhGroupName);
                rangeTextView = v.findViewById(R.id.sgmvhGroupRange);
                submitButton = v.findViewById(R.id.sgmvhButton);
            }

            void bind(Group g, final int pos) {
                nameTextView.setText(g.getName());
                rangeTextView.setText(g.getRange());

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textThisGroup(pos);
                    }
                });
            }
        }
    }
}
