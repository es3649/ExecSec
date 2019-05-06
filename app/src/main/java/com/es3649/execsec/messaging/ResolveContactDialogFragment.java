package com.es3649.execsec.messaging;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.es3649.execsec.R;
import com.es3649.execsec.adapters.ContactResolutionAdapter;
import com.es3649.execsec.data.model.Person;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

import java.util.List;

/**
 *
 *
 * Created by es3649 on 5/1/19.
 */

public class ResolveContactDialogFragment extends DialogFragment {

    private static final String TAG = "RCDialogFragment";
    private Listener mListener;
    private List<UnresolvedContact> unresolvedContacts;
    private Context ctx;

    public static ResolveContactDialogFragment newInstance(List<UnresolvedContact> contacts, Context ctx) {
        ResolveContactDialogFragment rcdf = new ResolveContactDialogFragment();
        rcdf.unresolvedContacts = contacts;
        rcdf.ctx = ctx;
        return rcdf;
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setTitle(R.string.rdfLabel);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_with_recycler, null);
        builder.setView(v);

        RecyclerView rcv = v.findViewById(R.id.recycler);
        rcv.setAdapter(new ContactResolutionAdapter(unresolvedContacts));
        rcv.setLayoutManager(new LinearLayoutManager(ctx));

        builder.setNegativeButton(R.string.rdfNegative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onNegativeInteraction();
            }
        });

        builder.setPositiveButton(R.string.rdfPositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onPositiveInteraction();
            }
        });

        return builder.create();
    }

    public interface Listener {
        void onPositiveInteraction();
        void onNegativeInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (Listener)context;
        } catch (ClassCastException ex) {
            throw new RuntimeException("Context must implements my Listener");
        }
    }
}
