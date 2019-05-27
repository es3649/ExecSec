package com.es3649.execsec.nlp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by es3649 on 5/26/19.
 *
 * Represents a string of Chunks after a syntactic chunk parse. It mostly just wraps List
 */

public class ChunkParsedString {

    public ChunkParsedString() {
        chunks = new ArrayList<>();
    }

    private List<Chunk> chunks;

    public Chunk get(int i) {
        return chunks.get(i);
    }

    public boolean add(Chunk c) {
        return chunks.add(c);
    }
}
