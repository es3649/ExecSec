package com.es3649.execsec.serverproxy;

import java.io.InputStream;

/**
 * Server proxy pretends to be a server and hides all the http away
 * in its own secret place
 *
 * Created by es3649 on 4/27/19.
 */

public class ServerProxy {
    private static final String TAG = "ServerProxy";
    private static final String HTTP_ADDRESS_FORMATTER = "http://%s:%s";

    private String API;

    /**
     * sets the API
     * @param IP the ip address which will be called
     * @param port the port which is listening specifically for this task
     */
    public void setAPI(String IP, String port) {
        API = String.format(HTTP_ADDRESS_FORMATTER, IP, port);

    }

    /**
     * gets the API
     *
     * @return the URL with port number
     */
    public String getAPI() {return API;}

    /**
     * makes a call to the CSVServer and returns the csv data
     * @param authentication the auth token required to access the server
     * @return an InputStream with the CSV data inside
     */
    public InputStream getCSVData(String authentication) {

    }
}
