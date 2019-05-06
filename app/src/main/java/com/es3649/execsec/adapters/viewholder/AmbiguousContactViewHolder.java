package com.es3649.execsec.adapters.viewholder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.AmbiguousContact;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

/**
 *
 * Created by es3649 on 4/30/19.
 */

public class AmbiguousContactViewHolder extends UnresolvedContactViewHolder {
    public AmbiguousContactViewHolder(View v) {
        super(v);

        topLayout = v.findViewById(R.id.rcvhParent);
        optionSpinner = v.findViewById(R.id.rcvhSpinner);
        nameTextView = v.findViewById(R.id.rcvhTextView);

        // we need this to create an adapter
        ctx = v.getContext();
    }

    private ConstraintLayout topLayout;
    private Spinner optionSpinner;
    private TextView nameTextView;
    private AmbiguousContact abContact;
    private Context ctx;

    public void bind(UnresolvedContact contact) {
        abContact = (AmbiguousContact)contact;

        nameTextView.setText(abContact.getName());
        topLayout.setBackgroundColor(getUnresolvedColor());

        String[] nameList = new String[abContact.getPersonList().length];
        for (int i = 0; i < abContact.getPersonList().length; i++) {
            nameList[i] = abContact.getPersonList()[i].getFullName();
        }

        optionSpinner.setAdapter(new ArrayAdapter<String>(ctx,
                android.R.layout.simple_spinner_dropdown_item, nameList));

        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                topLayout.setBackgroundColor(getResolvedColor());
                abContact.resolve(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                topLayout.setBackgroundColor(getUnresolvedColor());
                abContact.resolve(-1);
            }
        });
    }
}
