package com.es3649.execsec.messaging.contact.UI;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.Contact;

/**
 * This is an abstract superclass for all contact resolution ViewHolders.
 *
 * Created by es3649 on 4/30/19.
 */

public abstract class ContactViewHolder extends RecyclerView.ViewHolder {
    private int unresolvedColor;
    private int resolvedColor;

    ContactViewHolder(View v) {
        super(v);
        unresolvedColor = v.getResources().getColor(R.color.colorDanger);
        resolvedColor = v.getResources().getColor(R.color.colorGood);
    }

    public abstract void bind(Contact contact);

    int getResolvedColor() {return resolvedColor;}
    int getUnresolvedColor() {return unresolvedColor;}
}
