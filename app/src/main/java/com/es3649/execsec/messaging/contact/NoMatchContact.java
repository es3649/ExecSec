package com.es3649.execsec.messaging.contact;

import android.telephony.PhoneNumberUtils;

import com.es3649.execsec.data.model.Person;

/**
 * Represents a name which had no matching number
 *
 * Created by es3649 on 4/30/19.
 */

public class NoMatchContact extends UnresolvedContact {
    public NoMatchContact(int idx, String name) {
        super(idx);
        this.name = name;
    }

    private String name;

    public boolean resolve(String number) {

        if (PhoneNumberUtils.isWellFormedSmsAddress(number)) {
            Person p = new Person(name, null, number);
            super.setPerson(p);
            super.setResolved(true);
            return true;
        } // else

        super.setPerson(null);
        super.setResolved(false);
        return false;
    }

    public String getName() {return name;}
}
