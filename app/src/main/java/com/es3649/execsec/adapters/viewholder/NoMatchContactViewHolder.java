package com.es3649.execsec.adapters.viewholder;

import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.NoMatchContact;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

/**
 *
 * Created by es3649 on 5/1/19.
 */

public class NoMatchContactViewHolder extends UnresolvedContactViewHolder {
    private ConstraintLayout topLayout;
    private TextView nameTextView;
    private EditText phoneEditText;
    private NoMatchContact nmContact;

    public NoMatchContactViewHolder(View v){
        super(v);

        topLayout = v.findViewById(R.id.rmvhParent);
        nameTextView = v.findViewById(R.id.rmvhTextView);
        phoneEditText = v.findViewById(R.id.rmvhEditText);
    }

    @Override
    public void bind(UnresolvedContact contact) {
        this.nmContact = (NoMatchContact)contact;

        topLayout.setBackgroundColor(getUnresolvedColor());
        nameTextView.setText(nmContact.getName());
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                if (nmContact.resolve(number)) {
                    // change the color
                    topLayout.setBackgroundColor(getResolvedColor());
                    return;
                }
                // else change the color back
                topLayout.setBackgroundColor(getUnresolvedColor());
            }

        });
    }
}
