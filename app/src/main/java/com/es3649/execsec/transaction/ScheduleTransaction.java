package com.es3649.execsec.transaction;

import java.util.Date;

/**
 * Contains a scheduling transaction.
 * A scheduling transaction has a lot of data about an appointment which is currently
 * being scheduled.
 *
 * Created by es3649 on 4/24/19.
 */

public class ScheduleTransaction {
    private static final int ACTIVE_STATUS = 0;
    private static final int INACTIVE_STATUS = 1;
    private static final int STALE_STATUS = 2;
    private static final int CLOSED_STATUS = 3;

    private String givenName;
    private String surname;
    private int telephoneNumber;
    private int status;
    private Date appointment;
    private Conversation conversation;
}
