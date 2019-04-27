package com.es3649.execsec.data.database;

/**
 * This is honestly just a little dataBall for returning lots of things at once
 *
 * Created by es3649 on 4/27/19.
 */
public class LoadResult {
    /**
     * @param total the total number of entries which were to be loaded
     * @param failed the number of entries which failed to load
     * @param error the verbatim string of entries which failed
     */
    public LoadResult(int total, int failed, String error) {
        this.total = total;
        this.failed = failed;
        this.error = error;
    }

    private int total;
    private int failed;
    private String error;

    public int getTotal() {return total;}
    public int getFailed() {return failed;}
    public String getError() {return error;}
    public int getSuccess() {return total - failed;}
}
