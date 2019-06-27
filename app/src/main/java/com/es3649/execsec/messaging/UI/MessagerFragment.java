package com.es3649.execsec.messaging.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.es3649.execsec.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagerFragment.Listener} interface
 * to handle interaction events.
 * Use the {@link MessagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = "MessagerFragment";
    private static final String ARG_RECIPIENTS = "recipients";
    private static final String ARG_MESSAGE = "message";

    private String mMessage;
    private String mRecipients;

    private EditText recipientsEditText;
    private EditText messageBodyEditText;
    private Button sendButton;

    private Listener mListener;

    public MessagerFragment() {
        // Required empty public constructor
        mMessage = "";
        mRecipients = "";
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param recipients the recipients string
     * @param message the message string
     * @return A new instance of fragment MessagerFragment.
     */
    public static MessagerFragment newInstance(String recipients, String message) {
        MessagerFragment fragment = new MessagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPIENTS, recipients);
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipients = getArguments().getString(ARG_RECIPIENTS);
            mMessage = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messager, container, false);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonEnabled();
            }
        };

        recipientsEditText = v.findViewById(R.id.msgfRecipients);
        messageBodyEditText = v.findViewById(R.id.msgfMessageBody);
        sendButton = v.findViewById(R.id.msgfSendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO call the listener back
                mListener.handleMessage(recipientsEditText.getText().toString(),
                        messageBodyEditText.getText().toString());
            }
        });

        recipientsEditText.addTextChangedListener(tw);
        messageBodyEditText.addTextChangedListener(tw);

        // TODO check intent for data and populate the EditTexts
        if (savedInstanceState != null) {
            recipientsEditText.setText(mRecipients);
            messageBodyEditText.setText(mMessage);
        }

        return v;
    }

    private void updateButtonEnabled() {


        if (recipientsEditText.getText().toString().length() == 0
                || messageBodyEditText.getText().toString().length() == 0) {

            sendButton.setEnabled(false);
            return;
        }

        sendButton.setEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MessagerFragment.Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface Listener {
        void handleMessage(String recipientList, String message);
    }
}
