package com.es3649.execsec.messaging.contact.UI;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.Contact;

public class ResolvedContactViewholder extends ContactViewHolder {
    private ConstraintLayout topLayout;
    private TextView nameTextView;
    private TextView phoneTextView;

    public ResolvedContactViewholder(View v) {
        super(v);

        topLayout = v.findViewById(R.id.cvhTopLayout);
        nameTextView = v.findViewById(R.id.cvhName);
        phoneTextView = v.findViewById(R.id.cvhNumber);
    }

    @Override
    public void bind(Contact contact) {
        topLayout.setBackgroundColor(getResolvedColor());

        nameTextView.setText(contact.getPerson().getFullName());
        phoneTextView.setText(contact.getPerson().getNumber());

    }
}
