package com.es3649.execsec.data.model;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

/**
 * Represents a person. They have a name and a phone number.
 * Doesn't it feel so CS101?
 *
 * Created by es3649 on 4/25/19.
 */

public class Person {

    /**
     * Constructs a new person object.
     * @param givenName the person's given name
     * @param surname the person's surname
     * @param phoneNumber the person's phone number
     */
    public Person(String givenName, String surname, String phoneNumber) {
        this.setName(givenName);
        this.setSurname(surname);
        this.setNumber(phoneNumber);
    }

    private static final String TAG = "dao.model.Person";
    private String name;
    private String surname;
    private String number;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        // format the number
        // TODO we might need to format this to add country codes
        this.number = PhoneNumberUtils.normalizeNumber(number);
        if (!PhoneNumberUtils.isWellFormedSmsAddress(this.getNumber())) {
            Log.e(TAG, String.format("The phone number `%s` is not dialable!",
                    this.getNumber()));
        }
    }
}
