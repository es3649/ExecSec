package com.es3649.execsec.activities.settings.groups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.es3649.execsec.R;
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Group;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

public class GroupManagerActivity extends AppCompatActivity {

    private static final String TAG = "GroupManagerActivity";

    private GroupManagerAdapter gma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        RecyclerView rcv = findViewById(R.id.gmaRecycler);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        DB_Proxy db = new DB_Proxy(this);
        List<Group> gList = db.getAllGroups();
        gma = new GroupManagerAdapter(gList, db);
        rcv.setAdapter(gma);

        findViewById(R.id.gmaSaveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroups();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.group_menu, menu);

        menu.findItem(R.id.gmaMenuSave).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_save)
                .colorRes(R.color.colorPrimaryLighter)
                .actionBarSize());

        menu.findItem(R.id.gmaNemuNew).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_plus)
                .colorRes(R.color.colorPrimaryLighter)
                .actionBarSize());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.gmaMenuSave:
            saveGroups();
            return true;

        case R.id.gmaNemuNew:
            gma.newGroup();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    // TODO this is not working
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Stopping...");

//        saveGroups();
    }

    private void saveGroups() {
        DB_Proxy db = new DB_Proxy(this);

        for (Group g : gma.getGroupList()) {

            // TODO this is still spawning bogus groups
            //  and the reordering gets jacked up on refresh after new groups are saved
            if (g.empty()) {
                if (g.getId() != Group.BLANK_ID) {
                    // empty && id != BLANK delete
                    Log.d(TAG, "Deleteing group");
                    db.deleteGroup(g);
                }
                // if g empty and id == BLANK, then this is a useless group and we skip it

            } else if (g.getId() == Group.BLANK_ID) {
                // not empty and id == BLANK create, saving ID
                db.stashGroup(g);
                Log.d(TAG, "Stashing group");

            } else {
                // not empty and if != BLANK update
                Log.d(TAG, "Updating group");
                db.updateGroups(g);

            }
//            Log.d(TAG, String.format("Storing group-- Name: '%s' Range: '%s'",
//                        g.getName(), g.getRange()));
//            db.updateGroups(g);
        }

        Toast.makeText(this, R.string.gmaSaved, Toast.LENGTH_SHORT).show();
    }
}

