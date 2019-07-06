package com.es3649.execsec.activities.settings.groups;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.es3649.execsec.R;
import com.es3649.execsec.data.model.Group;

class EditableGroupViewHolder extends RecyclerView.ViewHolder {
    private EditText name;
    private EditText range;
    private Group group;
    private DeleteListener dListener;
    private Button deleteButton;

    interface DeleteListener {
        void delete(int i);
    }

    EditableGroupViewHolder(View v, DeleteListener dListener) {
        super(v);

        this.dListener = dListener;

        name = v.findViewById(R.id.gmvhGroupName);
        range = v.findViewById(R.id.gmvhGroupRange);
        deleteButton = v.findViewById(R.id.gmvhDeleteButton);

    }

    void bind(Group g, final int pos) {
        name.setText(g.getName());
        range.setText(g.getRange());
        this.group = g;

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                group.setName(editable.toString());
            }
        });

        range.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                group.setRange(editable.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dListener.delete(pos);
            }
        });
    }
}
