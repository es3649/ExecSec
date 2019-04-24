package com.es3649.execsec.nlp;

import java.util.List;

/**
 * The Processor class takes text and parses them for intents
 * It loads processing data from local config preferences
 *
 * Created by es3649 on 4/24/19.
 */

public class Processor {

    public Processor() {}

    public Processor(String confFile) {
        this.confFile = confFile;
    }

    private String confFile;

    /**
     * This does all the work, honestly. It takes a string, and parses the content to
     * construct a list of intents.
     *
     * TODO sort the list by intent confidence, if applicable
     * @param processMe the string to process for intent
     * @return a list of possible inferred intents
     */
    public List<NLPIntent> process(String processMe) {
        return null;
    }
}
