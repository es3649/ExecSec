package com.es3649.execsec;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

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
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Stuff", Toast.LENGTH_SHORT).show();
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
