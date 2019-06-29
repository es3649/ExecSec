package com.es3649.execsec.messaging.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.UI.viewholder.AmbiguousContactViewHolder;
import com.es3649.execsec.messaging.UI.viewholder.NamelessContactViewHolder;
import com.es3649.execsec.messaging.UI.viewholder.NoMatchContactViewHolder;
import com.es3649.execsec.messaging.UI.viewholder.ContactViewHolder;
import com.es3649.execsec.messaging.UI.viewholder.ResolvedContactViewholder;
import com.es3649.execsec.messaging.contact.AmbiguousContact;
import com.es3649.execsec.messaging.contact.NamelessContact;
import com.es3649.execsec.messaging.contact.NoMatchContact;
import com.es3649.execsec.messaging.contact.Contact;
import com.es3649.execsec.messaging.contact.ResolvedContact;

import java.util.List;

/**
 * Adapts UnresolvedContacts so that they can be resolved.
 *
 * Created by es3649 on 4/30/19.
 */

public class ContactResolutionAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    public ContactResolutionAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    private static final int NO_MATCH_CONTACT = 0;
    private static final int NAMELESS_CONTACT = 1;
    private static final int AMBIGUOUS_CONTACT = 2;
    private static final int RESOLVED_CONTACT = 3;
    private static final String TAG = "ResolutionAdapter";

    private List<Contact> contacts;

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public int getItemViewType(int position) {
        Contact uc = contacts.get(position);

        if (uc instanceof NoMatchContact) {
            return NO_MATCH_CONTACT;
        } else if (uc instanceof NamelessContact) {
            return NAMELESS_CONTACT;
        } else if (uc instanceof AmbiguousContact) {
            return AMBIGUOUS_CONTACT;
        } else if (uc instanceof ResolvedContact) {
            return RESOLVED_CONTACT;
        } else {
            Log.e(TAG, "Illegal contact type!");
            throw new AssertionError("Illegal contact type!");
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
        case NO_MATCH_CONTACT:
            return new NoMatchContactViewHolder(
                    inflater.inflate(R.layout.resolve_no_match_viewholder, parent, false));
        case NAMELESS_CONTACT:
            return new NamelessContactViewHolder(
                    inflater.inflate(R.layout.resolve_no_name_viewholder, parent, false));
        case AMBIGUOUS_CONTACT:
            return new AmbiguousContactViewHolder(
                    inflater.inflate(R.layout.resolve_conflict_viewholder, parent, false));
        case RESOLVED_CONTACT:
            return new ResolvedContactViewholder(
                    inflater.inflate(R.layout.resolved_contact_viewholder, parent, false));
        default:
            Log.e(TAG, "Binding illegal contact type!");
            throw new AssertionError("Binding illegal contact type!");
        }
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        // this should be the correct type, and this handled by onCreateViewHolder
        holder.bind(contacts.get(position));
    }
}
