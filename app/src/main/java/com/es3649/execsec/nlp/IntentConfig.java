package com.es3649.execsec.nlp;

/**
 * IntentConfig contains configurations used in NLP processes to define intents.
 * IntentConfigs are designed be read from TODO
 *
 * TODO read from plists or json files?
 *
 * Created by es3649 on 4/24/19.
 */

class IntentConfig {
    IntentConfig() {}



    /**
     * TODO
     * This reads a default intent config from somewhere. plist or json?
     * @return a new IntentConfig from the file
     */
    static IntentConfig getDefault() {
        return null;
    }

    /**
     * Creates new IntentConfigs from a file
     *
     * In android, this might not be so helpful
     *
     * @param fileName the name of the file which holds the configs
     * @return a new IntentConfig from the file
     */
    static IntentConfig fromFile(String fileName) {
        return null;
    }

    public class IntentStructure {
        private String intentName;
        private String[][] wordClassList;

        public String[][] getWordClassList() {return wordClassList;}
        public String getIntentName() {return intentName;}
    }
}
