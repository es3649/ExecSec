package com.es3649.execsec.data.model;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Locale;

/**
 * Represents a person. They have a name and a phone number.
 * Doesn't it feel so CS101?
 *
 * Created by es3649 on 4/25/19.
 */

public class Person {
    // TODO internationalize this, publish it as a setting
    public static final String US_COUNTRY_CODE = "US";

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

    public String getFullName() {
        return String.format(Locale.getDefault(), "%s %s", getName(), getSurname());
    }

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
        Log.d(TAG, "Number: " + number);
        this.number = PhoneNumberUtils.formatNumberToE164(number, US_COUNTRY_CODE);
        Log.d(TAG, "After ToE164: " + this.number);
        if (this.number != null && !PhoneNumberUtils.isWellFormedSmsAddress(this.getNumber())) {
            Log.e(TAG, String.format("The phone number `%s` is not dialable! Going back to null",
                    this.getNumber()));
            this.number = null;
        }
    }

    @Override
    public String toString() {
        Gson g = new Gson();
        return g.toJson(this);
    }
}
