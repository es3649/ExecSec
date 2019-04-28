package com.es3649.execsec.serverproxy;

/**
 * An exception which designates a server access failure.
 *
 * It has a couple constructors which just match the super.constructors
 *
 * Created by es3649 on 4/27/19.
 */

public class ServerAccessException extends RuntimeException {
    ServerAccessException(String message) {
        super(message);
    }

    ServerAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
