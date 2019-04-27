package com.es3649.csvserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * An HttpHandler to serve up a CSV File
 * Created by es3649 on 4/26/19.
 */

class CSVLoadHandler implements HttpHandler {
    CSVLoadHandler() {}

    private static final String REQUEST_METHOD = "GET";
    private static final String AUTHENTICATION_HEADER = "Authentication";

    public void handle (HttpExchange exchange) throws IOException {
        try {
            handleMethod(exchange);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void handleMethod(HttpExchange exchange) throws IOException {
        // get output stream
        OutputStream os = exchange.getResponseBody();

        CSVServer.logger.log(Level.INFO, "Event request URI is: " + exchange.getRequestURI().toString());

        // verify proper request method
        if (!exchange.getRequestMethod().equalsIgnoreCase(REQUEST_METHOD)) {
            CSVServer.logger.log(Level.INFO, "Failed for bad request (405)");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);

            beDone(os, "Bad Request Method");
            exchange.close();
            return;
        }

        // authenticate
        Headers headers = exchange.getRequestHeaders();
        CSVServer.logger.log(Level.FINEST, System.getenv("PWD"));
        String password = new Scanner(new File("com/es3649/csvserver/password")).nextLine();
        CSVServer.logger.log(Level.INFO, "Password is: "+password);

        if (!headers.containsKey(AUTHENTICATION_HEADER)) {
            String givenPwd = headers.getFirst(AUTHENTICATION_HEADER);
            CSVServer.logger.log(Level.INFO, "Received password: "+givenPwd);
            if (!givenPwd.equals(password)) {

                CSVServer.logger.log(Level.INFO, "No Authentication received (401)");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);

                beDone(os, "Failed to Authenticate");
                exchange.close();
                return;
            }
        }

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        FileInputStream fis = new FileInputStream("com/es3649/csvserver/file.csv");
        copy(fis, os);

        CSVServer.logger.log(Level.INFO, "Success (200)");
        os.close();
        exchange.close();
    }

    private void beDone(OutputStream os, String message) throws IOException {
        PrintWriter pw = new PrintWriter(os);
        pw.write(message);

        pw.close();
        os.close();
    }

    private void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int count = buf.length;
        while (count >= buf.length) {
            count = is.read(buf);
            os.write(buf, 0, count);
        }
    }
}
