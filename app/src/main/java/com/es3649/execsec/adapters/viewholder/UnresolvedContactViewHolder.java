package com.es3649.execsec.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.es3649.execsec.R;
import com.es3649.execsec.messaging.contact.UnresolvedContact;

/**
 * This is an abstract superclass for all contact resolution ViewHolders.
 *
 * Created by es3649 on 4/30/19.
 */

public abstract class UnresolvedContactViewHolder extends RecyclerView.ViewHolder {
    private int unresolvedColor;
    private int resolvedColor;

    UnresolvedContactViewHolder(View v) {
        super(v);
        unresolvedColor = v.getResources().getColor(R.color.colorDanger);
        resolvedColor = v.getResources().getColor(R.color.colorGood);
    }

    public abstract void bind(UnresolvedContact contact);

    int getResolvedColor() {return resolvedColor;}
    int getUnresolvedColor() {return unresolvedColor;}
}
