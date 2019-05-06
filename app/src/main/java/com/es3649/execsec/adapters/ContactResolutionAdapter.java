package com.es3649.execsec.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.es3649.execsec.R;
import com.es3649.execsec.adapters.viewholder.AmbiguousContactViewHolder;
import com.es3649.execsec.adapters.viewholder.NamelessContactViewHolder;
import com.es3649.execsec.adapters.viewholder.NoMatchContactViewHolder;
import com.es3649.execsec.adapters.viewholder.UnresolvedContactViewHolder;
import com.es3649.execsec.messaging.contact.AmbiguousContact;
import com.es3649.execsec.messaging.contact.NamelessContact;
import com.es3649.execsec.messaging.contact.NoMatchContact;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

import java.util.List;

/**
 * Adapts UnresolvedContacts so that they can be resolved.
 *
 * Created by es3649 on 4/30/19.
 */

public class ContactResolutionAdapter extends RecyclerView.Adapter<UnresolvedContactViewHolder> {

    public ContactResolutionAdapter(List<UnresolvedContact> unresolvedContacts) {
        this.unresolvedContacts = unresolvedContacts;
    }

    private static final int NO_MATCH_CONTACT = 0;
    private static final int NAMELESS_CONTACT = 1;
    private static final int AMBIGUOUS_CONTACT = 2;
    private static final String TAG = "ResolutionAdapter";

    private List<UnresolvedContact> unresolvedContacts;

    @Override
    public int getItemCount() {
        return unresolvedContacts.size();
    }

    @Override
    public int getItemViewType(int position) {
        UnresolvedContact uc = unresolvedContacts.get(position);

        if (uc instanceof NoMatchContact) {
            return NO_MATCH_CONTACT;
        } else if (uc instanceof NamelessContact) {
            return NAMELESS_CONTACT;
        } else if (uc instanceof AmbiguousContact) {
            return AMBIGUOUS_CONTACT;
        } else {
            Log.e(TAG, "Illegal contact type!");
            throw new AssertionError("Illegal contact type!");
        }
    }

    @Override
    public UnresolvedContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        default:
            Log.e(TAG, "Binding illegal contact type!");
            throw new AssertionError("Binding illegal contact type!");
        }
    }

    @Override
    public void onBindViewHolder(UnresolvedContactViewHolder holder, int position) {
        // this should be the correct type, and this handled by onCreateViewHolder
        holder.bind(unresolvedContacts.get(position));
    }
}
