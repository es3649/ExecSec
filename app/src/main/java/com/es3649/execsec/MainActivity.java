package com.es3649.execsec;

import android.app.FragmentManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainFragmentHolder, LoginFragment.newInstance("",""))
                .commit();
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
}
