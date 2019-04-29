package com.es3649.execsec;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.es3649.execsec.model.Model;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainFragmentHolder, LoginFragment.newInstance("",""))
                .commit();

        Iconify.with(new FontAwesomeModule());
        requestSmsPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Stuff", Toast.LENGTH_SHORT).show();
        invalidateOptionsMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentHolder, TransactionCountFragment.newInstance(new TransactionCountFragment.Listener() {
                    @Override
                    public void onFragmentInteraction(Uri uri) {
                        Toast.makeText(getApplicationContext(), "+1", Toast.LENGTH_SHORT).show();
                    }
                }))
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (Model.isLoggedIn()) {
            // inflate the options menu
            getMenuInflater().inflate(R.menu.top_menu, menu);

            // add the settings button
            menu.findItem(R.id.menuTopSettingsButton).setIcon(
                    new IconDrawable(this, FontAwesomeIcons.fa_cogs)
                    .colorRes(R.color.colorPrimaryLighter)
                    .actionBarSize());

            menu.findItem(R.id.menuNewMessageButton).setIcon(
                    new IconDrawable(this, FontAwesomeIcons.fa_pencil_square)
                    .colorRes(R.color.colorPrimaryLighter)
                    .actionBarSize());
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
        case R.id.menuTopSettingsButton:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        case R.id.menuNewMessageButton:
            startActivity(new Intent(this, MessagerActivity.class));
            return true;

        default:
            Log.e(TAG, "bad menu choice");
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Rends a permission request to the screen to read and send text messages.
     *
     * I copied this from stack overflow.
     * https://stackoverflow.com/questions/33347809/android-marshmallow-sms-received-permission
     */
    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }
}
