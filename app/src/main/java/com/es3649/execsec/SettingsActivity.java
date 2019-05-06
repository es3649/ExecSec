package com.es3649.execsec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.es3649.execsec.data.database.DB_Proxy;

import java.util.Locale;

/**
 * TODO we need to be able to change settings for:
 * TODO | Message response templates
 * TODO | Loading people
 * TODO | changing password
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.setLoadLinLay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoaderActivity.class);
                startActivity(i);
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
    }

}
