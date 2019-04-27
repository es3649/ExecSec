package com.es3649.csvserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * tbh this is a scrappy little server just for loading CSV files into
 * the app. I confess the style is not great
 */
public class CSVServer {
    private CSVServer() {}

    static Logger logger;

    static {
        Level logLevel = Level.FINEST;

        logger = Logger.getLogger("server");
        logger.setLevel(logLevel);
        logger.setUseParentHandlers(false);

//        try {
//            // try to set up the file handler, if it fails, the set up
//            // the console handler
//            FileHandler fileHandler = new FileHandler("log/serverlog.txt");
//            fileHandler.setLevel(logLevel);
//            fileHandler.setFormatter(new SimpleFormatter());
//            logger.addHandler(fileHandler);
//
//
//        } catch (IOException ex) {
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(logLevel);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // log the error we already got:
//            logger.log(Level.WARNING, "Failed to initialize file logger", ex);
//        }
    }

    /**
     * main begins hosting the Server
     * @param args command line arguments
     */
    public static void main(String args[]) {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            usage();
            return;
        }

        CSVServer s = new CSVServer();
        s.run(port);
    }

    private static final int MAX_WAITING_CONNS = 12;
    private HttpServer server;

    /**
     * runs the server
     * @param port the port number on which to host
     */
    private void run(int port) {
        // initialize the server
        System.out.println("Initializing server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(port),
                    MAX_WAITING_CONNS);
        } catch (IOException ex) {
            System.out.println("Failed to initialize");
            ex.printStackTrace();
            return;
        }
        // don't know what this does, apparently it's important
        server.setExecutor(null);

        // create the contexts
        System.out.println("Creating contexts");

        server.createContext("/", new CSVLoadHandler());

        System.out.println("Starting Server...");
        System.out.println();
        server.start();
    }

    /**
     * prints the usage information
     */
    private static void usage() {
        System.out.println("Usage: java main/Server <port number>");
    }
}
