package com.es3649.execsec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.interceptor.Messager;

public class MessagerActivity extends AppCompatActivity {

    private static final String TAG = "MessagerActivity";
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

    /**
     * Sends the message(s)
     */
    private void sendMessage() {
        DB_Proxy db = new DB_Proxy(this);
        String[] recipientNameList = recipientsEditText.getText().toString().split("[,\n ]*");
        Person[] recipientList = new Person[recipientNameList.length];

        for (int i = 0; i < recipientNameList.length; i++) {
            if (PhoneNumberUtils.isWellFormedSmsAddress(recipientNameList[i])) {
                Log.d(TAG, "Recipient is well formed SMS: " + recipientNameList[i]);
                Person p = db.lookupPerson(recipientNameList[i]);

                if (p == null) {
                    // then just whatever, at least it's dialable
                    p = new Person(null, null, recipientNameList[i]);
                    // TODO maybe do a warning?
                }

                recipientList[i] = p;

            } else {
                Log.d(TAG, "Recipient is not SMS form: " + recipientNameList[i]);
                Person[] pList = db.lookupPeopleByName(recipientNameList[i]);


                if (pList.length == 0) {
                    // TODO this is an error to report to the user

                } else if (pList.length == 1) {
                    recipientList[i] = pList[0];

                } else {
                    recipientList[i] = resolve(recipientNameList[i], pList);
                }

                Log.d(TAG, "Resolved address to: " + recipientList[i].getNumber());
            }
        }

        Messager messager = new Messager(this);
        for (Person recipient : recipientList) {
            messager.sendMessage(recipient.getNumber(),
                    buildMessage(recipient));
        }

        Toast.makeText(this, "Sent that sucker", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Resolves a lookup conflict, first by checking for unique, exact matches,
     * then by making the user choose one!
     *
     * @param name the name we intended to see
     * @param pList the list from which to resolve ambiguity
     * @return the person we actually meant to get in the first place
     */
    private Person resolve(String name, Person[] pList) {
        // TODO account for first and last name, vs. one or the other
        // TODO prompt the user to solve the problem

        // TODO We will use an AlertDialog here
        // https://developer.android.com/guide/topics/ui/dialogs


        return null;
    }

    /**
     * Constructs the message. This mostly involves getting the text from the edit text,
     * but it can also be as intense as making tag replacements for '%name' and such
     *
     * @param p the person the message goes to
     * @return the final text of the message
     */
    private String buildMessage(Person p) {
        String text = messageBodyEditText.getText().toString();

        if (text.contains("%")) {
            text = Messager.doReplacements(text, p);
        }

        return text;
    }
}
