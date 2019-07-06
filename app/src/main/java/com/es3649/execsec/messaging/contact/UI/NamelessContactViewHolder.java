package com.es3649.execsec.messaging.contact.UI;

import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.NamelessContact;
import com.es3649.execsec.messaging.contact.Contact;

/**
 * A view holder for NamelessContact
 *
 * Created by es3649 on 4/30/19.
 */

public class NamelessContactViewHolder extends ContactViewHolder {

    private ConstraintLayout topLayout;
    private TextView numberTextView;
    private EditText nameEditText;
    private NamelessContact nnContact;

    public NamelessContactViewHolder(View v) {
        super(v);

        topLayout = v.findViewById(R.id.rnvhParent);
        numberTextView = v.findViewById(R.id.rnvhNameLabel);
        nameEditText = v.findViewById(R.id.rnvhFirstNameEditText);

    }

    public void bind(Contact contact) {
        nnContact = (NamelessContact)contact;

        topLayout.setBackgroundColor(getUnresolvedColor());
        numberTextView.setText(nnContact.getNumber());

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                String[] names = name.split("[\t\n ]");

                if (names.length == 2) {
                    nnContact.setName(names[0], names[1]);
                    topLayout.setBackgroundColor(getResolvedColor());
                    return;
                }

                nnContact.setName(null, null);
                topLayout.setBackgroundColor(getUnresolvedColor());
            }

        });
    }
}
