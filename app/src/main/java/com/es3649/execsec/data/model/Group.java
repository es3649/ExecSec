package com.es3649.execsec.data.model;

public class Group {

    public static final int BLANK_ID = -1;
    private String name;
    private String range;
    // an internal ID for the database
    private long id;

    public Group(String name, String range) {
        this.name = name;
        this.range = range;
        this.id = BLANK_ID;
    }

    public Group(String name, String range, long id) {
        this.name = name;
        this.range = range;
        this.id = id;
    }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRange() { return range; }
    public void setRange(String range) { this.range = range; }
    public long getId() { return id; }

    public boolean empty() {
        return (name == null || name.isEmpty())
                && (range == null || range.isEmpty());
    }
}
