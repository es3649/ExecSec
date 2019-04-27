package com.es3649.execsec.data.database;

import android.content.Context;

import com.es3649.execsec.data.model.Person;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * The purpose of this class is to take CSV data and
 * stash each row as a Database entry
 *
 * Created by es3649 on 4/26/19.
 */

public class DB_Loader {
    public DB_Loader(Context ctx) {
        this.ctx = ctx;
    }

    private Context ctx;

    /**
     * This is the main method. It takes csv data from an input stream,
     * parses it into people, then stores each of those people as database entries.
     *
     * It is expected that each line of the data has the following format:
     * <code>surname,given names,phone-number</code>
     *
     * @param csvData an input stream containing people in csv data
     * @param failures a string of entries (verbatim) which were not parsable
     * @return the number of failures
     */
    public int load(InputStream csvData, String failures) throws IOException {
        Scanner scanner = new Scanner(csvData);
        StringBuilder errLines = new StringBuilder();
        DB_Proxy db = new DB_Proxy(ctx);
        int counter = 0;

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] components = line.split(",");

            if (components.length != 3) {
                errLines.append(line);
                errLines.append("\n");
                counter++;
                continue;
            }

            // chop off any middle names from component 1
            components[1] = components[1].split(" ")[0];

            Person p = new Person(components[1], components[0], components[2]);
            db.stashPerson(p);
        }

        failures = errLines.toString();
        return counter;
    }
}