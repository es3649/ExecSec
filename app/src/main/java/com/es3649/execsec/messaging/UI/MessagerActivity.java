package com.es3649.execsec.messaging.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import com.es3649.execsec.R;
import com.es3649.execsec.data.database.DB_Proxy;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.messaging.Messager;
import com.es3649.execsec.messaging.contact.AmbiguousContact;
import com.es3649.execsec.messaging.contact.NamelessContact;
import com.es3649.execsec.messaging.contact.NoMatchContact;
import com.es3649.execsec.messaging.contact.Contact;
import com.es3649.execsec.messaging.contact.ResolvedContact;
import com.es3649.execsec.messaging.contact.UI.ContactResolutionFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagerActivity extends AppCompatActivity
        implements MessagerFragment.Listener, ContactResolutionFragment.Listener {

    private static final String TAG = "MessagerActivity";
    private static final String ARG_RECIPIENTS = "recipients";
    private static final String ARG_MESSAGE = "message";

    private DB_Proxy db = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

        String recipients = "";
        String message = "";

        // Check intent for data and populate the fragment
        if (savedInstanceState != null) {
            recipients = savedInstanceState.getString(ARG_RECIPIENTS, "");
            message = savedInstanceState.getString(ARG_MESSAGE, "");
        }

        // add the fragment
        MessagerFragment mf = MessagerFragment.newInstance(recipients, message);
        FragmentManager fm = getSupportFragmentManager();

        Fragment frag = fm.findFragmentById(R.id.mgsFragFrame);
        if (frag != null) {
            if (frag instanceof MessagerFragment) {
                // then leave it
                return;
            }
            throw new AssertionError("Found the wrong fragment on create");
        }

        fm.beginTransaction().add(R.id.mgsFragFrame, mf).commit();
    }

    @Override
    public void handleMessage(String recipientList, String message) {
        ArrayList<String> recipientNameList = new ArrayList<String>(
                Arrays.asList(recipientList.split(" *[,;\n]+ *")));

        db = new DB_Proxy(this);

        // TODO separate the recipientList, then move to the contact
        //  resolution fragment. Also start a dbProxy at this time
        ContactResolutionFragment frag = ContactResolutionFragment.newInstance(recipientNameList, message);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mgsFragFrame, frag).commit();
    }

    // OVERRIDE METHODS FOR ContactResolutionFragment.Listener
    @Override
    public void sendMessages(List<Person> recipients, List<String> messages) {
        Toast.makeText(this, getString(R.string.msgfSending), Toast.LENGTH_SHORT).show();

        sendMessage(recipients, messages);
        finish();
    }

    @Override
    public void cancel() {
        // give up and be done
        finish();
    }

    @Override
    public void back() {
        // TODO swap back to the previous fragment
        //  this means that we need to save the message
        //  and recipients string to rebuild the old frag
    }

    /**
     *
     * @param recipients the list of names/numbers given in the recipients box
     * @return a list of contacts (resolved or not) corresponding to the recipients in
     * the recipient list
     */
    @Override
    public List<Contact> getContacts(List<String> recipients) {
        if (db == null) {
            throw new AssertionError("DB was not initialized");
        }

        ArrayList<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < recipients.size(); i++) {

            // check if the recipient is a well foremed SMS address
            if (PhoneNumberUtils.isWellFormedSmsAddress(recipients.get(i))) {
                // then we can quite easily look it up directly
                Log.d(TAG, "Recipient is well formed SMS: " + recipients.get(i));
                Person p = db.lookupPerson(recipients.get(i));

                if (p == null) {
                    contacts.add(new NamelessContact(i, recipients.get(i)));
                }

                contacts.add(new ResolvedContact(p, i));

            // then it was a name, look it up with resolution
            } else {
                Log.d(TAG, "Recipient is not SMS form: " + recipients.get(i));
                Person[] pList = db.lookupPeopleByName(recipients.get(i));

                if (pList == null || pList.length == 0) {
                    Log.d(TAG, "Found no match for: " + recipients.get(i));
                    contacts.add(new NoMatchContact(i, recipients.get(i)));

                } else if (pList.length == 1) {
                    contacts.add(new ResolvedContact(pList[0], i));
                    Log.d(TAG, "Resolved address to: " + pList[0].getNumber());
                } else {
                    contacts.add(new AmbiguousContact(i, recipients.get(i), pList));
                }
            }
        }

        return contacts;
    }

    /**
     * Sends the message
     *
     * @param pList the list of people to send the message to
     * @param messageList the list of messages, these elements should correspond exactly
     *                    to the entries in the pList
     */
    private void sendMessage(List<Person> pList, List<String> messageList) {
        if (pList.size() != messageList.size()) {
            throw new AssertionError("MessageList and Person list had different sizes!");
        }

        Messager messager = new Messager(this);

        for (int i = 0; i < pList.size(); i++) {
            messager.sendMessage(pList.get(i).getNumber(),
                    messageList.get(i));
        }

        Toast.makeText(this, "Sent that sucker", Toast.LENGTH_SHORT).show();
        finish();
    }
}
