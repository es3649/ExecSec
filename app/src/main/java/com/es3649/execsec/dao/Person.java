package com.es3649.execsec.dao;

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
     * @param name the person's name
     * @param phoneNumber the person's phone number
     */
    public Person(String name, String phoneNumber) {
        this.setName(name);
        this.setNumber(phoneNumber);
    }

    private static final String TAG = "dao.Person";
    private String name;
    private String number;



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        // format the number
        this.number = PhoneNumberUtils.normalizeNumber(number);
        if (!PhoneNumberUtils.isWellFormedSmsAddress(this.getNumber())) {
            Log.e(TAG, String.format("The phone number `%s` is not dialable!",
                    this.getNumber()));
        }
    }
}
