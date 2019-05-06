package com.es3649.execsec.messaging.contact;

import com.es3649.execsec.data.model.Person;

/**
 * An unresolved contact.
 *
 * It is implemented by
 * Created by es3649 on 4/30/19.
 */

public abstract class UnresolvedContact {
    UnresolvedContact(int idx) {
        setResolved(false);
        resolution = null;
        index = idx;
    }

    private boolean resolved;
    private Person resolution;
    private int index;

    public boolean isResolved() {return resolved;}
    void setResolved(boolean resolutionState) {resolved = resolutionState;}

    public Person getPerson() {return resolution;}
    protected void setPerson(Person p) {resolution = p;}
    public int getIndex() {return index;}
}
