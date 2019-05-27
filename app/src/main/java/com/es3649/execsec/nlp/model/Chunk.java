package com.es3649.execsec.nlp.model;

/**
 * Created by es3649 on 5/26/19.
 *
 * Represents a single element in a syntactic chunk parse
 */

public class Chunk {

    /**
     * Constructs a new syntactic chunk
     * @param value
     * @param tag the tag on the block, should be null for an Outside chunk
     * @param IBO the IBO classification of the chunk (Inside: "I", Beginning: "B" , Outside: "O")
     */
    public Chunk(String value, String tag, String IBO) {
        _value = value;
        _tag = tag;
        _IBO = IBO;
    }

    public Chunk(String value) {
        _value = value;
        _tag = null;
        _IBO = "O";
    }

    private String _value;
    private String _tag;
    private String _IBO;


    // ------- getters -------//
    public String getTag() {
        return _tag;
    }

    public String getValue() {
        return _value;
    }

    public String getIBO() {
        return _IBO;
    }

    // ------- Overrides -------//

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chunk chunk = (Chunk) o;

        if (!_value.equals(chunk._value)) return false;
        if (_tag != null ? !_tag.equals(chunk._tag) : chunk._tag != null) return false;
        return _IBO.equals(chunk._IBO);
    }

    @Override
    public int hashCode() {
        int result = _value.hashCode();
        result = 31 * result + (_tag != null ? _tag.hashCode() : 0);
        result = result ^ _IBO.hashCode();
        return result;
    }

    @Override
    public String toString() {
        if (_tag == null) {
            return String.format("%s: %s_", _value, _IBO);
        }
        return String.format("%s: %s_%s", _value, _IBO, _tag);
    }
}
