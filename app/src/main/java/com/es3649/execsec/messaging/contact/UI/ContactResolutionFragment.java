package com.es3649.execsec.messaging.contact.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.es3649.execsec.R;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.messaging.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * ContactResolutionFragment exists to provide confirmation for the recipients of a message
 * before sending it off. It also gives the user a chance to correct ambiguities that the
 * system detects in the input.
 *
 * Activities that contain this fragment must implement the
 * {@link Listener} interface to handle interaction events.
 *
 * Use the {@link ContactResolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactResolutionFragment extends Fragment {

    private static final String TAG = "ContactResolutionFrag";
    private static final String ARG_RECIPIENTS = "recipients";
    private static final String ARG_MESSAGE = "message";

    private ArrayList<String> mRecipients;
    private String mMessage;
    private List<Contact> contactList;

    private LinearLayoutManager linLay;

    private Listener mListener;

    public ContactResolutionFragment() {
        // Required empty public constructor
        mRecipients = new ArrayList<>();
        mMessage = "";
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipients the names/numbers of the contacts as given, to be looked up
     * @param message the message to send
     * @return A new instance of fragment ContactResolutionFragment.
     */
    public static ContactResolutionFragment newInstance(
            ArrayList<String> recipients, String message) {

        ContactResolutionFragment fragment = new ContactResolutionFragment();

        Bundle args = new Bundle();

        args.putStringArrayList(ARG_RECIPIENTS, recipients);
        args.putString(ARG_MESSAGE, message);

        fragment.setArguments(args);

        return fragment;
    }

    // LIFECYCLE HOOKS

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipients = getArguments().getStringArrayList(ARG_RECIPIENTS);
            mMessage = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contact_resolution, container, false);

        // wire some things up. There should be a recycler view
        RecyclerView rcv = v.findViewById(R.id.crfRecycler);

        contactList = mListener.getContacts(mRecipients);

        rcv.setAdapter(new ContactResolutionAdapter(contactList));
        rcv.setLayoutManager(linLay);

        v.findViewById(R.id.crfDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareAndSend();
            }
        });

        v.findViewById(R.id.crfBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.back();
            }
        });

        v.findViewById(R.id.crfCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancel();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ContactResolutionFragment.Listener");
        }

        linLay = new LinearLayoutManager(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // MAIN LOGIC

    /**
     * Constructs the message.
     * It can be as intense as making tag replacements for '%name' and such
     * TODO once we implement message previews, add this message back in.
     *  For now, '%' insertion tags won't work at all
     *
     * @param p the person the message goes to
     * @return the final text of the message
     */
    private String buildMessage(Person p, String messageText) {

        if (messageText.contains("%")) {
            messageText = doReplacements(messageText, p);
        }

        return messageText;
    }

    /**
     * Prepares the lists of messages and contacts to
     */
    private void prepareAndSend() {
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<Person> recipients = new ArrayList<>();

        for (int i = 0; i < contactList.size(); i++) {
            if (!contactList.get(i).isResolved()) {

                Log.i(TAG, "Not continuing because of unresolved contacts");
                return;
            }

            recipients.add(contactList.get(i).getPerson());
            messages.add(doReplacements(mMessage, contactList.get(i).getPerson()));
        }

        mListener.sendMessages(recipients, messages);
    }

    private static final String FIRST_NAME_TAG = "%name";

    /**
     * Does the hard work of replacing tags with values
     *
     * Currently supported tags are:
     * `%name` -> person's first name
     *
     * @param text the text on which to make the replacements
     * @param p the person whose info will be subbed in
     * @return the modified text
     */
    public String doReplacements(String text, Person p) {
        StringBuilder sbText = new StringBuilder(text);

        int pos = sbText.indexOf(FIRST_NAME_TAG);
        while (pos > -1) {
            sbText.replace(pos, pos + FIRST_NAME_TAG.length(), p.getName());
            pos = sbText.indexOf(FIRST_NAME_TAG);
        }

        return sbText.toString();
    }

    // LISTENER

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface Listener {
        List<Contact> getContacts(List<String> recipients);
        void sendMessages(List<Person> recipients, List<String> messages);
        void cancel();
        void back();
    }
}
