package com.es3649.execsec.serverproxy;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Server proxy pretends to be a server and hides all the http away
 * in its own secret place
 *
 * Created by es3649 on 4/27/19.
 */

public class ServerProxy {
    private static final String TAG = "ServerProxy";
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String AUTHENTICATION_HEADER = "Authentication";
    private static final String HTTP_ADDRESS_FORMATTER = "http://%s:%s";
    private static final String CONNECT_FAIL = "Failed to connect";


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
     * starts an HTTP connection from an API handle
     * @param APIHandle the API handle to use in the connection
     * @return an opened HTTP connection
     * @throws IOException if we have trouble connecting to the server
     */
    private HttpURLConnection getHttpURLConnection(String APIHandle) throws IOException {
        URL url = new URL(getAPI() + APIHandle);
        Log.d(TAG, String.format("Calling %s API at %s", APIHandle , url.toString()));
        return (HttpURLConnection)url.openConnection();
    }

    /**
     * Reads all bytes from an input stream, returning them as a single string
     * @param is the input stream to read from
     * @return a string representing all the bytes
     * @throws IOException if we can't read the InputStream
     */
    private String readAllBytes(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int len;
        while ((len = sr.read(buffer)) > 0) {
            sb.append(buffer, 0, len);
        }
        return sb.toString();
    }

    /**
     * makes a call to the CSVServer and returns the csv data
     * @param authentication the auth token required to access the server
     * @return an InputStream with the CSV data inside
     */
    public InputStream getCSVData(String authentication) throws ServerAccessException {
        try {
            HttpURLConnection http = getHttpURLConnection("/");

            http.setRequestMethod(GET_METHOD);
            http.setDoOutput(false);
            http.setDoInput(true);
            http.addRequestProperty(AUTHENTICATION_HEADER, authentication);

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "Successful request");
                return http.getInputStream();
            }

            throw new ServerAccessException(readAllBytes(http.getErrorStream()));

        } catch (ConnectException ex) {
            Log.e(TAG, CONNECT_FAIL, ex);
            throw new ServerAccessException("Failed to Connect");

        } catch (IOException ex) {
            Log.e(TAG, "Failed while parsing response", ex);
            throw new ServerAccessException("Caught IOException", ex);
        }
    }
}
