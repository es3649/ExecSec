package com.es3649.execsec.messaging.contact;

import com.es3649.execsec.data.model.Person;

/**
 * Represents a contact to which many possible people may apply
 *
 * Created by es3649 on 4/30/19.
 */

public class AmbiguousContact extends Contact {
    public AmbiguousContact(int idx, String name, Person[] personList) {
        super(idx);
        this.pList = personList;
        this.name = name;
    }

    private Person[] pList;
    private String name;

    public void resolve(int idx) {
        if (idx < 0 || idx >= pList.length) {
            super.setPerson(null);
            super.setResolved(false);
            return;
        }
        super.setPerson(pList[idx]);
        super.setResolved(true);
    }

    public Person[] getPersonList() {
        return pList;
    }

    public String getName() {
        return name;
    }
}
