package com.es3649.execsec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessagerActivity extends AppCompatActivity {

    private EditText recipientsEditText;
    private EditText messageBodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonEnabled();
            }
        };

        recipientsEditText = findViewById(R.id.msgRecipients);
        messageBodyEditText = findViewById(R.id.msgMessageBody);

        recipientsEditText.addTextChangedListener(tw);
        messageBodyEditText.addTextChangedListener(tw);

        // TODO check intent for data and populate the EditTexts

        findViewById(R.id.msgSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {sendMessage();}
        });
    }

    private void updateButtonEnabled() {
        Button b = findViewById(R.id.msgSendButton);

        if (recipientsEditText.getText().toString().length() == 0
                || messageBodyEditText.getText().toString().length() == 0) {

            b.setEnabled(false);
            return;
        }

        b.setEnabled(true);
    }

    private void sendMessage() {
        Toast.makeText(this, "Sent that sucker", Toast.LENGTH_SHORT).show();
        finish();
    }
}
