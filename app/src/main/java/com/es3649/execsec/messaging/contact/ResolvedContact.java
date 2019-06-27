package com.es3649.execsec.messaging.contact;

import com.es3649.execsec.data.model.Person;

/**
 * ResolvedContact represents a contact for which there is not actually
 * anything to resolve, because we suppose to know exactly who it is
 */
public class ResolvedContact extends Contact {
    public ResolvedContact(Person p, int idx) {
        super(idx);
        this.setPerson(p);
        this.setResolved(true);
    }
}
