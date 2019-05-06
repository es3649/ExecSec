package com.es3649.execsec.messaging.contact;

import com.es3649.execsec.data.model.Person;

/**
 * Represents a contact without a name.
 *
 * Created by es3649 on 4/30/19.
 */

public class NamelessContact extends UnresolvedContact {
    public NamelessContact(int idx, String number) {
        super(idx);
        this.number = number;
    }

    private String number;

    public String getNumber() {return number;}

    public void setName(String first, String last) {
        if (first == null || last == null) {
            super.setPerson(null);
            super.setResolved(false);
        }

        super.setPerson(new Person(first, last, getNumber()));
        super.setResolved(true);
    }
}
