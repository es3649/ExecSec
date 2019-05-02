package com.es3649.execsec;

import android.app.DialogFragment;
import android.app.FragmentManager;
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
import com.es3649.execsec.messaging.Messager;
import com.es3649.execsec.messaging.ResolveContactDialogFragment;
import com.es3649.execsec.messaging.contact.AmbiguousContact;
import com.es3649.execsec.messaging.contact.NamelessContact;
import com.es3649.execsec.messaging.contact.NoMatchContact;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

import java.util.ArrayList;
import java.util.List;

public class MessagerActivity extends AppCompatActivity
        implements ResolveContactDialogFragment.Listener {

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
        String[] recipientNameList = recipientsEditText.getText().toString().split("[,;\n]+");
        Person[] recipientList = new Person[recipientNameList.length];
        Log.d(TAG, String.format("Found %d contacts", recipientList.length));

        List<UnresolvedContact> unresolvedContacts = new ArrayList<>();

        for (int i = 0; i < recipientNameList.length; i++) {
            if (PhoneNumberUtils.isWellFormedSmsAddress(recipientNameList[i])) {
                Log.d(TAG, "Recipient is well formed SMS: " + recipientNameList[i]);
                Person p = db.lookupPerson(recipientNameList[i]);

                if (p == null) {
                    unresolvedContacts.add(new NamelessContact(i, recipientNameList[i]));
                }

                recipientList[i] = p;

            } else {
                Log.d(TAG, "Recipient is not SMS form: " + recipientNameList[i]);
                Person[] pList = db.lookupPeopleByName(recipientNameList[i]);

                if (pList.length == 0) {
                    Log.d(TAG, "Found no match for: " + recipientNameList[i]);
                    unresolvedContacts.add(new NoMatchContact(i, recipientNameList[i]));

                } else if (pList.length == 1) {
                    recipientList[i] = pList[0];
                    Log.d(TAG, "Resolved address to: " + recipientList[i].getNumber());
                } else {
                    unresolvedContacts.add(new AmbiguousContact(i, recipientNameList[i], pList));
                    recipientList[i] = null;
                }

            }
        }

        if (!resolve(unresolvedContacts, recipientList)) {
            Log.i(TAG, "Failed to resolve");
            return;
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
     * Creates a dialog for resolving contacts and requires the user to help us
     * figure out what they meant, because it was ambiguous or something.
     *
     * @param resolveThese the list of unresolved contacts to resolve
     * @param recipientList the destination list for the recipients to be resolved.
     * @return true if every contact was resolved, false if not.
     */
    private boolean resolve(List<UnresolvedContact> resolveThese, Person[] recipientList) {

        // TODO We will use an AlertDialog here (with a recycler view)
        // TODO | when the alert dialog returns negative, we will return false
        // TODO | when the alert dialog returns true, we will first make sure
        // TODO | that everything is resolved, if not, we toast and don't exit
        // https://developer.android.com/guide/topics/ui/dialogs

        if (resolveThese.size() == 0) {
            return true;
        }

        ResolveContactDialogFragment dialog = ResolveContactDialogFragment.newInstance(resolveThese, this);
        dialog.show(getSupportFragmentManager(), "ContactResolutionDialog");

        return false;
    }

    @Override
    public void onPositiveInteraction() {

    }

    @Override
    public void onNegativeInteraction() {

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
