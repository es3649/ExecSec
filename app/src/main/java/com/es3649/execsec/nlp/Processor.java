package com.es3649.execsec.nlp;

import com.es3649.execsec.nlp.model.ChunkParsedString;
import com.es3649.execsec.nlp.model.NLPTemplate;

import java.util.List;

/**
 * The Processor class takes text and parses them for intents
 * It loads processing data from local config preferences
 *
 * Created by es3649 on 4/24/19.
 */

public class Processor {

    public Processor() {
        this.conf = IntentConfig.getDefault();
    }

    public Processor(String confFile) {
        this.conf = IntentConfig.fromFile(confFile);
    }

    private IntentConfig conf;

    /**
     * This does all the work, honestly. It takes a string, and parses the content to
     * construct a list of intents.
     *
     * TODO sort the list by intent confidence, if applicable
     * @param processMe the string to process for intent
     * @return a list of possible inferred intents
     */
    public List<NLPIntent> process(String processMe) {
        ChunkParsedString wordList = chunkParse(processMe);

        return null;
    }

    /**
     * Performs a syntactic chunk parse of the string
     * @param toParse the string to parse
     * @return a chunk string of the string to parse
     */
    private ChunkParsedString chunkParse(String toParse) {
        SyntacticChunkParser scp = new SyntacticChunkParser();
        return scp.parse(toParse);
    }

    /**
     * Searches for intents within a string
     * @param stringList a word list // TODO should this be a ChunkParsedString?
     * @param structure the structure of the intend to find
     * @return The intent found in the string, if any
     */
    private NLPIntent findIntent(String[] stringList, IntentConfig.IntentStructure structure) {
        return null;
    }

    /**
     * Looks through the data to try to fill out the Template
     * @param data the data from which to fill the template
     *        // TODO should this be a string, or a ChunkParsedString?
     * @param template the template to be filled, it is used by reference
     * @return false
     */
    public boolean fillTemplate(String data, NLPTemplate template) {
        // TODO we look through the string for
        return false;
    }
}