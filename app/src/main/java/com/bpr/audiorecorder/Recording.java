package com.bpr.audiorecorder;

import java.util.Date;

/**
 * Created by pr on 28/01/16.
 */

public class Recording {

    Date timestamp;
    int status;
    int duration;
    public static final int SAVED = 0;
    public static final int UPLOADED = 1;

    public Recording(int duration) {
        this.duration = duration;
        this.timestamp = new Date(System.currentTimeMillis());
        this.status = SAVED; //
    }

}
