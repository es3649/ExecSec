package com.es3649.execsec.activities.settings.groups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.es3649.execsec.R;
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Group;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

public class GroupManagerActivity extends AppCompatActivity {

    private GroupManagerAdapter gma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        // TODO a RecyclerView here probably, let's try to keep it small though.
        //  ViewHolder: EditText for name of group, EditText for group range
        //  Apply button to sync changes

        RecyclerView rcv = findViewById(R.id.gmaRecycler);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        DB_Proxy db = new DB_Proxy(this);
        List<Group> gList = db.getAllGroups();
        gma = new GroupManagerAdapter(gList, db);
        rcv.setAdapter(gma);

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

        saveGroups();
    }

    private void saveGroups() {
        DB_Proxy db = new DB_Proxy(this);

        for (Group g : gma.getGroupList()) {
            db.updateGroups(g);
        }
    }
}
