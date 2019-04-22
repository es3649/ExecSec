package com.es3649.execsec.model;

/**
 * A Model singleton for statically storing application data.
 *
 * Created by es3649 on 4/21/19.
 */

public class Model {
    private static Model coreInstance = new Model();

    static Model getInstance() {
        return coreInstance;
    }

    private int transactionCount = 0;

    {
        // initialize the coreInstance
//        coreInstance = new Model();
    }

    private Model() {
    }

    public static boolean isLoggedIn() {
        return false;
    }

    public static void  login() {}

    public static int getTransactionCount() {
        return getInstance().transactionCount;
    }

    //TODO DON'T LEAVE THIS HERE
    public static void increment() {
        Model.getInstance().transactionCount++;
    }
}
